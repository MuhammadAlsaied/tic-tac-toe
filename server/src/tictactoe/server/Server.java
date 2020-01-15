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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeSet;
import tictactoe.server.db.DatabaseManager;

/**
 *
 * @author muhammad and Ayman Magdy
 */
public class Server {

    private ServerSocket serverSocket;

    private final HashMap<Integer, User> onlinePlayers = new HashMap<>();
    private final HashMap<Integer, User> offlinePlayers = new HashMap<>();
    private final TreeSet<Player> sortedOnlinePlayersbyPoints = new TreeSet<>((o1, o2) -> {
        return o1.getPoints() - o2.getPoints();
    });
    private final TreeSet<Player> sortedOfflinePlayersbyPoints = new TreeSet<>((o1, o2) -> {
        return o1.getPoints() - o2.getPoints();
    });
    private final HashSet<Socket> unloggedInUsers = new HashSet<>();
    JsonHandler jsonHandler = new JsonHandler(this);

    private DatabaseManager databaseManager;

    public Server() {
        try {
            this.databaseManager = new DatabaseManager();
            Collection<Player> players = databaseManager.getAllPlayers();
            sortedOfflinePlayersbyPoints.addAll(players);

            Iterator<Player> iterator = players.iterator();

            while (iterator.hasNext()) {
                Player player = iterator.next();
                offlinePlayers.put(player.getId(), new User(player));
            }

            serverSocket = new ServerSocket(Config.PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                unloggedInUsers.add(socket);
                new ClientThread(socket).start();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
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
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        public User() {
        }

        public User(Socket socket) {
            this.socket = socket;
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

        public ClientThread(Socket socket) {
            this.socket = socket;
            try {
                dataInputStream = new DataInputStream(socket.getInputStream());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        public void setUser(User user) {
            this.user = user;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    String line = dataInputStream.readUTF();
                    if (line != null) {
                        JsonObject request = JsonParser.parseString(line).getAsJsonObject();
                        System.out.println(line);
                        if (request.get("type").getAsString().equals("signout")) {
                            removeFromOnlinePlayers(user.getPlayer().getId());
                            stop();
                        } else {
                            jsonHandler.handle(request, user);
                        }

                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
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

    public void addToOnlinePlayers(int id) {

        User user = offlinePlayers.remove(id);
        onlinePlayers.put(id, user);
        sortedOfflinePlayersbyPoints.remove(user.player);
        sortedOnlinePlayersbyPoints.add(user.player);
        user.player.setOnline(true);

        JsonObject response = new JsonObject();
        JsonObject data = new JsonObject();
        response.addProperty("type", "online-player");

        response.add("data", data);
        data.add("player", user.player.asJson());
        sendToAllOnlinePlayers(response);
    }

    public void sendToAllOnlinePlayers(JsonObject req) {
        onlinePlayers.forEach((key, value) -> {
            try {
                value.dataOutputStream.writeUTF(req.toString());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    public void removeFromOnlinePlayers(int id) {
        User user = onlinePlayers.remove(id);
        offlinePlayers.put(id, user);
        sortedOnlinePlayersbyPoints.remove(user.player);
        sortedOfflinePlayersbyPoints.add(user.player);
        user.player.setOnline(false);

        JsonObject response = new JsonObject();
        JsonObject data = new JsonObject();
        response.addProperty("type", "offline-player");

        response.add("data", data);
        data.add("player", user.player.asJson());
        sendToAllOnlinePlayers(response);
    }
    
    public void addNewOfflinePlayer(Player player){
        offlinePlayers.put(player.getId(), new User(player));
        
    }
    
    public User getOnlinePlayerById(int id){
        return onlinePlayers.get(id);
    }

    public static void main(String[] args) {
        new Server();
    }
}
