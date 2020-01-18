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
import tictactoe.server.db.DatabaseManager;

/**
 *
 * @author muhammad and Ayman Magdy
 */
public class Server {

    private ServerSocket serverSocket;

    private final HashMap<Integer, User> onlinePlayers = new HashMap<>();
    private final HashMap<Integer, User> offlinePlayers = new HashMap<>();

    public final Set<ClientThread> arr = new HashSet<ClientThread>();

    Comparator<Player> playerComparbleByPoints = (o1, o2) -> {
        int diff = o1.getPoints() - o2.getPoints();
        if (diff == 0) {
            if (o1.getId() < o2.getId()) {
                return 1;
            } else {
                return -1;
            }
        }
        return diff;
    };

    private final TreeSet<Player> sortedOnlinePlayersbyPoints = new TreeSet<>(playerComparbleByPoints);

    private final TreeSet<Player> sortedOfflinePlayersbyPoints = new TreeSet<>(playerComparbleByPoints);

    // private final HashSet<Socket> unloggedInUsers = new HashSet<>();
    JsonHandler jsonHandler = null;

    private DatabaseManager databaseManager;

    public Server() {
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
            while (true) {
                Socket socket = serverSocket.accept();
                //  unloggedInUsers.add(socket);
                new ClientThread(new User(socket)).start();
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
            try {
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
            } catch (IOException ex) {
                ex.printStackTrace();
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
        private DataOutputStream dataOutputStream;
        private User user;

        public ClientThread(User user) {
            this.user = user;
            this.socket = user.socket;
            try {
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
            } catch (Exception ex) {
                ex.printStackTrace();
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
                        System.out.println(line);
                        if (request.get("type").getAsString().equals("signout")) {

                            System.out.println("user" + user.toString() + " player:" + user.player + " logging of");

                            if (user.player != null) {
                                System.out.println(user.player.getFirstName() + " logging off");
                                removeFromOnlinePlayers(user.player.getId());
                            }
                            break;
                        } else {
                            jsonHandler.handle(request, user);
                        }
                    }
                }
            } catch (IOException ex) {

                ex.printStackTrace();
            }
        }

        public void closeClient() {
            try {
                dataInputStream.close();
                user.dataOutputStream.close();
                socket.close();
                stop();

            } catch (IOException ex) {
                ex.printStackTrace();
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

    public void addToOnlinePlayers(int id, User newUser) {

        User user = offlinePlayers.remove(id);

        JsonObject response = new JsonObject();
        JsonObject data = new JsonObject();
        response.addProperty("type", "online-player");

        response.add("data", data);
        JsonArray onlineUsers = getSortedOnlinePlayersAsJson();
        JsonArray offlineUsers = getSortedOfflinePlayersAsJson();
        data.add("online-players", onlineUsers);
        data.add("offline-players", offlineUsers);
        onlinePlayers.put(id, newUser);
        sortedOfflinePlayersbyPoints.remove(user.player);
        sortedOnlinePlayersbyPoints.add(newUser.player);
        newUser.player.setOnline(true);
        sendToAllOnlinePlayers(response);
    }

    public void sendToAllOnlinePlayers(JsonObject req) {
        onlinePlayers.forEach((key, value) -> {
            try {
                System.out.println("k:" + key + " v:" + value);
                value.dataOutputStream.writeUTF(req.toString());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    public void removeFromOnlinePlayers(int id) {
        User user = onlinePlayers.remove(id);
        user.socket = null;
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

    public void addNewOfflinePlayer(Player player) {
        offlinePlayers.put(player.getId(), new User(player));

    }

    public User getOnlinePlayerById(int id) {
        return onlinePlayers.get(id);
    }

    public void turnOff() {

        for (ClientThread clientThread : arr) {
            clientThread.closeClient();
            try {
                serverSocket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
    }

    public static Server turnOn() {
        Server server = new Server();
        return server;
    }

}
