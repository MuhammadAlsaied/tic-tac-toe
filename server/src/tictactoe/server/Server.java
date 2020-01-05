package tictactoe.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author muhammad
 */
public class Server {

    private ServerSocket serverSocket;
    private static final ArrayList<Client> clients = new ArrayList<>();

    public Server() {
        try {
            serverSocket = new ServerSocket(Config.PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                clients.add(new Client(socket, new Player()));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private class Client {

        private final Socket socket;
        private final Player player;

        public Client(Socket socket, Player player) {
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
}
