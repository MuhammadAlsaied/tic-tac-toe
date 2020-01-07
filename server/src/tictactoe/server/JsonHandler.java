package tictactoe.server;

import com.google.gson.JsonObject;
import java.sql.SQLException;
import tictactoe.server.db.DatabaseManger;

/**
 *
 * @author muhammad
 */
public class JsonHandler {

    private DatabaseManger databaseManger;

    public JsonHandler() {
        try {
            this.databaseManger = new DatabaseManger();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void handle(JsonObject request) { // to take a new param (user)
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
}
