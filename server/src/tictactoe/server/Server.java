package tictactoe.server;

import com.google.gson.JsonArray;
import tictactoe.server.models.Player;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author muhammad
 */
public class Server {

    private ServerSocket serverSocket;

    private final HashMap<Integer, User> onlinePlayers = new HashMap<>();
    private final HashSet<Socket> unloggedInUsers = new HashSet<>();
    JsonHandler jsonHandler = new JsonHandler(this);

    public Server() {
        try {

            serverSocket = new ServerSocket(Config.PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                Player player = null;
                User user = new User(socket, player);
                unloggedInUsers.add(socket);
                new ClientThread(user).start();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public class User {

        private final Socket socket;
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
            this.socket = user.socket;
            this.user = user;
            try {
                dataInputStream = new DataInputStream(socket.getInputStream());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void run() {
            while (true) {
                try {
                    String line = dataInputStream.readUTF();
                    if (line != null) {
                        JsonObject request = JsonParser.parseString(line).getAsJsonObject();
                        System.out.println(line);
                        jsonHandler.handle(request, user);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

    }

    public JsonArray getOnlinePlayersAsJson() {
        JsonArray players = new JsonArray();
        onlinePlayers.forEach((key, value) -> {
            players.add(value.player.asJson());
        });
        return players;
    }

    public void addToOnlinePlayers(int id, User user) {
        onlinePlayers.put(id, user);
    }

    public void removeFromOnlinePlayers(int id) {
        onlinePlayers.remove(id);
    }

    public static void main(String[] args) {
        new Server();
    }
}
