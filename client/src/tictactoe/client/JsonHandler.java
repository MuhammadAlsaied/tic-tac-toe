package tictactoe.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.DataInputStream;
import tictactoe.client.gui.SignupScreen;

/**
 *
 * @author Tharwat
 */
public class JsonHandler {

    private App app;
    private DataInputStream dataInputStream;

    JsonHandler(App app) {
        app = app;
    }

    public void handle(JsonObject request) {
        String requestType = request.get("type").getAsString();
        JsonObject requestData = request.getAsJsonObject("data");
        switch (requestType) {
            case "signup-error":
                SignupScreen signupScreen = (SignupScreen) app.screens.get("signup");
                signupScreen.showSignupFailedPopup();
                break;
            case "signup-success":
                app.showAlert("Welcome :D", "Sign up successful.\nLogin to play :D");
                app.setScreen("signin");
                System.out.println("test setscreen after showalert");

                break;
            case "signin-success":
                handleSigninSuccess(requestData);
                app.setScreen("main");
                break;
            case "signin-error":
                app.showAlert("Could not login", requestData.get("msg").getAsString());
                break;
        }
    }

    private void handleSigninSuccess(JsonObject requestData) {

        Player myData = getPlayerFromJson(requestData);
        app.setMyData(myData);
//        /JsonArray array  = JsonParser.parseString(requestData.)
    }

    private Player getPlayerFromJson(JsonObject requestData) {
        return new Player(requestData.get("firstName").getAsString(),
                requestData.get("lastName").getAsString(),
                requestData.get("email").getAsString(),
                Integer.parseInt(requestData.get("points").getAsString())
        );
    }
}
