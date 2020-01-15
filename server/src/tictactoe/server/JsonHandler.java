package tictactoe.server;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.sql.SQLException;
import tictactoe.server.Server.User;
import tictactoe.server.db.DatabaseManager;
import tictactoe.server.models.Game;
import tictactoe.server.models.Player;

/**
 *
 * @author muhammad and Ayman Magdy
 */
public class JsonHandler {

    private DatabaseManager databaseManager;
    private final Server server;

    public JsonHandler(Server server) {
        this.server = server;
        try {
            this.databaseManager = server.getDatabaseManager();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void handle(JsonObject request, User user) {
        String requestType = request.get("type").getAsString();

        JsonObject requestData = request.getAsJsonObject("data");
        JsonObject response = null;
        switch (requestType) {
            case "signup":
                response = handleSignup(requestData, user);
                break;
            case "signin":
                response = handleSignin(requestData, user);
                break;
            case "invitation":
                response = handleInvitation(request, user);
                break;
            case "accept-invitation":
                response = handleInvitationAccept(request, user);
                break;
        }
        if (response != null) {
            try {
                user.getDataOutputStream().writeUTF(response.toString());
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
    }

    private JsonObject handleSignup(JsonObject requestData, User user) {
        JsonObject response = new JsonObject();
        JsonObject data = new JsonObject();
        response.add("data", data);

        String firstName = requestData.get("firstName").getAsString();
        String lastName = requestData.get("lastName").getAsString();
        String email = requestData.get("email").getAsString();
        String password = requestData.get("password").getAsString();
        {
            try {
                Player player = null;
                try {
                    player = databaseManager.signUp(firstName, lastName, email, password);
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
                if (player != null) {
                    response.addProperty("type", "signup-success");
                    server.addNewOfflinePlayer(player);
                } else {
                    response.addProperty("type", "signup-error");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        }
        return response;
    }

    private JsonObject handleSignin(JsonObject requestData, User user) {
        JsonObject response = new JsonObject();
        JsonObject data = new JsonObject();
        response.add("data", data);

        String email = requestData.get("email").getAsString();
        String password = requestData.get("password").getAsString();
        Player player = databaseManager.signIn(email, password);
        if (player == null) {
            response.addProperty("type", "signin-error");
            data.addProperty("msg", "wrong email or password");
        } else {
            user.setPlayer(player);
            server.addToOnlinePlayers(player.getId(), user);
            response.addProperty("type", "signin-success");
            JsonArray onlineUsers = server.getSortedOnlinePlayersAsJson();
            JsonArray offlineUsers = server.getSortedOfflinePlayersAsJson();
            data.add("online-players", onlineUsers);
            data.add("offline-players", offlineUsers);
            data.add("my-data", player.asJson());
        }
        return response;
    }

    private JsonObject handleInvitation(JsonObject request, User user) {
        JsonObject reqData = request.get("data").getAsJsonObject();
        User opponentUser = server.getOnlinePlayerById(reqData.get("invited_player_id").getAsInt());

        if (opponentUser.getPlayer().isOnline() && opponentUser.getPlayer().getCurrentGame() == null) {
            JsonObject response = new JsonObject();
            JsonObject data = new JsonObject();
            response.add("data", data);
            response.addProperty("type", "invitation");

            data.addProperty("inviter_player_id", user.getPlayer().getId());
            try {
                opponentUser.getDataOutputStream().writeUTF(response.toString());
            } catch (IOException iOException) {
                iOException.printStackTrace();
            }
        }

        return null;
    }

    private JsonObject handleInvitationAccept(JsonObject request, User user) {
        JsonObject reqData = request.get("data").getAsJsonObject();
        User invitingPlayer = server.getOnlinePlayerById(reqData.get("inviting_player_id").getAsInt());

        if (invitingPlayer.getPlayer().isOnline() && invitingPlayer.getPlayer().getCurrentGame() == null) {
            Game game = new Game(invitingPlayer.getPlayer(), user.getPlayer());
            invitingPlayer.getPlayer().setCurrentGame(game);
            user.getPlayer().setCurrentGame(game);
            
            JsonObject response = new JsonObject();
            JsonObject data = new JsonObject();
            response.add("data", data);
            response.addProperty("type", "accept-invitation");

            data.addProperty("invited_player_id", user.getPlayer().getId());
            try {
                invitingPlayer.getDataOutputStream().writeUTF(response.toString());
            } catch (IOException iOException) {
                iOException.printStackTrace();
            }
        }
        return null;
    }
}
