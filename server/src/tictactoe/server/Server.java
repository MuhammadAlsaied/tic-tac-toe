package tictactoe.server;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author muhammad
 */
public class Server {

    private ServerSocket serverSocket;

    private final ArrayList<User> OnlinePlayers = new ArrayList<>();
    private final ArrayList<Socket> unloggedInUsers = new ArrayList<>();
    private DatabaseManger databaseManger;

    public Server() {
        try {
            databaseManger = new DatabaseManger();

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

        public ClientThread(Socket socket) {
            this.socket = socket;
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
                        System.out.println(line);
                        JsonObject request = JsonParser.parseString(line).getAsJsonObject();

                        String rquestType = request.get("type").getAsString();

                        JsonObject requestData = request.getAsJsonObject("data");
                        switch (rquestType) {
                            case "signup":
                                System.out.println(rquestType);
                                String firstName = requestData.get("firstName").getAsString();
                                String lastName = requestData.get("lastName").getAsString();
                                String email = requestData.get("email").getAsString();
                                String password = requestData.get("password").getAsString();
                                 {
                                    try {
                                        databaseManger.signUp(firstName, lastName, email, password);
                                    } catch (SQLException ex) {
                                        ex.printStackTrace();
                                    }
                                }
                                break;
                        }
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

    }

    public static void main(String[] args) {
        new Server();
    }
}
