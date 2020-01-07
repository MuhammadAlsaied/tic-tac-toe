package tictactoe.server;

import tictactoe.server.models.Player;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author muhammad
 */
public class Server {

    private ServerSocket serverSocket;

    private final HashMap<Integer, User> OnlinePlayers = new HashMap<>();
    private final HashSet<Socket> unloggedInUsers = new HashSet<>();
    JsonHandler jsonHandler = new JsonHandler();

    public Server() {
        try {

            serverSocket = new ServerSocket(Config.PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                Player player = new Player();
                User user = new User(socket, player);
                unloggedInUsers.add(socket);
                new ClientThread(user).start();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private class User {

        private final Socket socket;
        private final Player player;

        public User(Socket socket, Player player) {
            this.socket = socket;
            this.player = player;
        }

        public Socket getSocket() {
            return socket;
        }

        public Player getPlayer() {
            return player;
        }

    }

    private class ClientThread extends Thread {

        private final Socket socket;
        private DataInputStream dataInputStream;
        private PrintStream printStream;

        public ClientThread(User userClientThread) {
            this.socket = userClientThread.socket;
            try {
                dataInputStream = new DataInputStream(socket.getInputStream());
                printStream = new PrintStream(socket.getOutputStream());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void run() {
            while (true) {
                try {
                    String line = dataInputStream.readLine();
                    if (line != null) {
                        JsonObject request = JsonParser.parseString(line).getAsJsonObject();

                        jsonHandler.handle(request);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

    }
    
    // Checking if the user is online
    public boolean isOnline(Player isOnlinePlayer) throws ClassNotFoundException, SQLException{
        if (isOnlinePlayer.isSignedIn()) {
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
        new Server();
    }
}
