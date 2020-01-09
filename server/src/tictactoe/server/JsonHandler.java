package tictactoe.server;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.sql.SQLException;
import tictactoe.server.Server.User;
import tictactoe.server.db.DatabaseManager;
import tictactoe.server.models.Player;

/**
 *
 * @author muhammad
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
}
