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
            this.databaseManager = new DatabaseManager();
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
                Player invitingPlayer = new Player();
                response = handleInvitation(user.getPlayer(), invitingPlayer);
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
                boolean success = databaseManager.signUp(firstName, lastName, email, password);
                if (success) {
                    response.addProperty("type", "signup-success");
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
            JsonArray onlineUsers = server.getOnlinePlayersAsJson();
            server.addToOnlinePlayers(player.getId(), user);
            response.addProperty("type", "signin-success");
            data.add("online-players", onlineUsers);
            data.add("my-data", player.asJson());
        }
        return response;
    }
    
    private JsonObject handleInvitation(Player inviter, Player invited){
        JsonObject response = new JsonObject();
        JsonObject data = new JsonObject();
        response.add("invitation", data);
        
        if (!server.isOnlinePlayer(invited)) {
            response.addProperty("type", "invitation-error");
            data.addProperty("msg", "the invited player is not online");
        }
        
        if (invited.getCurrentGame() != null) {
            response.addProperty("type", "invitation-error");
            data.addProperty("msg", "user is playing with someone else at the moment");
        }
        
        if (server.isBusyPlayer(invited)) {
            response.addProperty("type", "invitation-error");
            data.addProperty("msg", "user is playing with is busy");
        }
        
        if (server.isOnlinePlayer(invited) && server.isFreePlayer(invited) && invited.getCurrentGame() == null) {
            Game playNewGame = new Game(inviter, invited);
            inviter.setCurrentGame(playNewGame);
            invited.setCurrentGame(playNewGame);
            JsonArray palyersPlaying = server.getInvitationsPlayersAsJson();
            server.addToInvitationsPlayers(inviter, invited);
            response.addProperty("invitation", "invitation-succeed");
            data.add("invited-players", palyersPlaying);
            data.add("inviter-player", inviter.asJson());
            data.add("invited-player", invited.asJson());
        } 
        
        return response;
    }
}
