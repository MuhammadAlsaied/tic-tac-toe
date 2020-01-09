/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe.client;

import com.google.gson.JsonObject;
import java.io.DataInputStream;
import javafx.application.Platform;
import tictactoe.client.gui.SignupScreen;

/**
 *
 * @author Tharwat
 */
public class JsonHandler {

    private App app;
    private DataInputStream dataInputStream;

    JsonHandler(App a) {
        app = a;
    }

    public void handle(JsonObject request) {
        String requestType = request.get("type").getAsString();
        JsonObject requestData = request.getAsJsonObject("data");
        switch (requestType) {
            case "signup-error":
                SignupScreen signupScreen =(SignupScreen) app.screens.get("signup");
                signupScreen.showSignupFailedPopup();
                break;
            case "signup-success":
                app.showAlert("Welcome :D", "Sign up successful.\nLogin to play :D");
                app.setScreen("signin");
                System.out.println("test setscreen after showalert");

                break;
            case "signin-success":
                break;
            case "signin-error":
                app.showAlert("Could not login", requestData.get("msg").getAsString());
                break;
        }
    }
}
