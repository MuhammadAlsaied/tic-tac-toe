package tictactoe.server;

import com.google.gson.JsonArray;
import tictactoe.server.models.Player;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import tictactoe.server.db.DatabaseManager;
import tictactoe.server.models.Game;

/**
 *
 * @author muhammad and Ayman Magdy
 */
public class Server extends Thread {

    private ServerSocket serverSocket;

    private final HashMap<Integer, User> onlinePlayers = new HashMap<>();
    private final HashMap<Integer, User> offlinePlayers = new HashMap<>();
    public final Set<ClientThread> clientThreads = new HashSet<ClientThread>();

    Comparator<Player> playerComparatorByPoints = (o1, o2) -> {
        int diff = o2.getPoints() - o1.getPoints();
        if (diff == 0) {
            diff = o1.getId() - o2.getId();
        }
        return diff;
    };

    private final TreeSet<Player> sortedOnlinePlayersbyPoints = new TreeSet<>(playerComparatorByPoints);

    private final TreeSet<Player> sortedOfflinePlayersbyPoints = new TreeSet<>(playerComparatorByPoints);

    // private final HashSet<Socket> unloggedInUsers = new HashSet<>();
    JsonHandler jsonHandler = null;

    private DatabaseManager databaseManager;
    private ServerMain app;

    public Server(ServerMain app) {
        this.app = app;
        try {
            this.databaseManager = new DatabaseManager();
            this.jsonHandler = new JsonHandler(this);
            Collection<Player> players = databaseManager.getAllPlayers();
            sortedOfflinePlayersbyPoints.addAll(players);
            Iterator<Player> iterator = players.iterator();

            while (iterator.hasNext()) {
                Player player = iterator.next();
                offlinePlayers.put(player.getId(), new User(player));
            }
            serverSocket = new ServerSocket(Config.PORT);
            setPlayerList();
        } catch (Exception ex) {
            app.guiLog("issue has been fitched with the server connection.");
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                ClientThread clientThread = new ClientThread(new User(socket));
                clientThread.start();
                clientThreads.add(clientThread);
            } catch (IOException ex) {
                app.guiLog("issue has been fitched with server socket.");
            }
        }
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public class User {

        private Socket socket;
        private Player player;
        private DataOutputStream dataOutputStream;

        public User(Socket socket, Player player) {
            this.socket = socket;
            this.player = player;
            try {
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                app.guiLog("A new player has logged on.");
            } catch (IOException ex) {
                app.guiLog("issue has been fitched with establishing the data output stream.");
            }
        }

        public User() {
        }

        public User(Socket socket) {
            this.socket = socket;
            try {
                app.guiLog("A new connection has been established.");
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
            } catch (IOException ex) {
                app.guiLog("issue has been fetched with establishing the data output stream.");
            }
        }

        public User(Player player) {
            this.player = player;
        }

        public void setPlayer(Player player) {
            this.player = player;
        }

        public Socket getSocket() {
            return socket;
        }

        public Player getPlayer() {
            return player;
        }

        public DataOutputStream getDataOutputStream() {
            return dataOutputStream;
        }

    }

    private class ClientThread extends Thread {

        private final Socket socket;
        private DataInputStream dataInputStream;
        private User user;

        public ClientThread(User user) {
            this.user = user;
            this.socket = user.socket;
            try {
                dataInputStream = new DataInputStream(socket.getInputStream());
            } catch (Exception ex) {
                app.guiLog("issue has been fitched with establishing the data input stream.");
            }
        }

        public void setUser(User user) {
            this.user = user;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    String line = dataInputStream.readUTF();
                    if (line != null) {
                        JsonObject request = JsonParser.parseString(line).getAsJsonObject();
                        app.guiLog(line);
                        if (request.get("type").getAsString().equals("signout")) {

                            app.guiLog("user" + user.toString() + " player:" + user.player + " logging off");
                            
                            handleClosedPlayer();
                            break;
                        } else {
                            jsonHandler.handle(request, user);
                        }
                    }
                }
            } catch (IOException ex) {
                app.guiLog("NO DATA.");
                handleClosedPlayer();
            }
        }

        public void closeClient() {
            try {
                app.guiLog("one of the users has logged off");
                dataInputStream.close();
                user.dataOutputStream.close();
                socket.close();
                stop();

            } catch (IOException ex) {
                app.guiLog("issue has been fetched with closing the data output/input stream.");
            }
        }

        private void handleClosedPlayer() {
            if (user.player != null) {
                if (user.player.getCurrentGame() != null) {
                    Game currentGame = user.player.getCurrentGame();
                    currentGame.setGameStatus(Game.Status.terminated);
                    User secondUser = new User();
                    if (user.player.getId() == currentGame.getPlayerX().getId()) {
                        secondUser = onlinePlayers.get(currentGame.getPlayerO().getId());
                    } else {
                        secondUser = onlinePlayers.get(currentGame.getPlayerX().getId());
                    }

                    try {
                        if (currentGame.getGameId() > 0) {
                            databaseManager.updateGame(currentGame);
                        } else {
                            databaseManager.insertGame(currentGame);
                        }
                    } catch (ClassNotFoundException ex) {
                        app.guiLog("Connection error with the databse.");
                    }
                    JsonObject alertTerminatedGame = new JsonObject();
                    alertTerminatedGame.addProperty("type", "terminated-game");
                    JsonObject data = new JsonObject();
                    alertTerminatedGame.add("data", data);
                    currentGame.getPlayerX().setCurrentGame(null);
                    currentGame.getPlayerO().setCurrentGame(null);
                    try {
                        secondUser.dataOutputStream.writeUTF(alertTerminatedGame.toString());

                    } catch (Exception e) {
                        app.guiLog("Issue fitched writing to the output stream");
                    }
                }
                removeFromOnlinePlayers(user.player.getId()); // call here 
            }
            setPlayerList();
        }
    }

    public JsonArray getSortedOnlinePlayersAsJson() {
        JsonArray players = new JsonArray();
        sortedOnlinePlayersbyPoints.forEach((player) -> {
            players.add(player.asJson());
        });
        return players;
    }

    public JsonArray getSortedOfflinePlayersAsJson() {
        JsonArray players = new JsonArray();
        sortedOfflinePlayersbyPoints.forEach((player) -> {
            players.add(player.asJson());
        });
        return players;
    }

    public void addToOnlinePlayers(int id, User newUser) {
        User user = offlinePlayers.remove(id);
        newUser.player.setOnline(true);
        onlinePlayers.put(id, newUser);
        sortedOfflinePlayersbyPoints.remove(user.player);
        sortedOnlinePlayersbyPoints.add(newUser.player);
        newUser.player.setOnline(true);
        sendUpdatedPlayerList();
        app.guiLog("New player has been added to the online players list");
        setChatPlayerStatus("online", user.getPlayer().getFirstName());
    }

    public void sendToAllOnlinePlayers(JsonObject req) {
        onlinePlayers.forEach((key, value) -> {
            try {
                app.guiLog("k:" + key + " v:" + value);
                value.dataOutputStream.writeUTF(req.toString());
            } catch (Exception ex) {
                app.guiLog("Issue fitched with writing into the output stream");
            }
        });
    }
    
    public void setChatPlayerStatus(String status, String playerName){
        //    [player is now online.]     
        //    [player is now offline.]
        JsonObject request = new JsonObject();
        JsonObject data = new JsonObject();
        request.add("data", data);
        request.addProperty("type", "global_chat_message");
        data.addProperty("sender", "");
        
        if (status.equals("offline")) {
            data.addProperty("message", "[" + playerName +" is now offline.]");
        }
        else if (status.equals("online")) {
            data.addProperty("message", "[" + playerName +" is now online.]");
        }
        
        sendToAllOnlinePlayers(request);
    }


    public void removeFromOnlinePlayers(int id) {
        User user = onlinePlayers.remove(id);
        user.socket = null;
        offlinePlayers.put(id, user);
        sortedOnlinePlayersbyPoints.remove(user.player);
        sortedOfflinePlayersbyPoints.add(user.player);
        user.player.setOnline(false);
        sendUpdatedPlayerList();
        setChatPlayerStatus("offline", user.getPlayer().getFirstName());
    }

    public void sendUpdatedPlayerList() {
        JsonObject data = new JsonObject();
        JsonObject response = new JsonObject();
        response.addProperty("type", "update-player-list");
        response.add("data", data);
        JsonArray onlineUsers = getSortedOnlinePlayersAsJson();
        JsonArray offlineUsers = getSortedOfflinePlayersAsJson();
        data.add("online-players", onlineUsers);
        data.add("offline-players", offlineUsers);
        sendToAllOnlinePlayers(response);
    }

    public void addNewOfflinePlayer(Player player) {
        offlinePlayers.put(player.getId(), new User(player));
    }

    public User getOnlinePlayerById(int id) {
        return onlinePlayers.get(id);
    }

    public void turnOff() {
        for (ClientThread clientThread : clientThreads) {
            clientThread.closeClient();
        }
        try {
            serverSocket.close();
        } catch (IOException ex) {
            app.guiLog("Issue has been fetched closing the server socket.");
        }
    }

    public void setPlayerList() {
        Platform.runLater(() -> {
            app.clearPlayersListPane();
            app.setPlayersListCounter(0);
            app.addPlayersToList(getSortedOnlinePlayersAsJson(), Color.GREEN);
            app.addPlayersToList(getSortedOfflinePlayersAsJson(), Color.RED);
        });
    }

}
