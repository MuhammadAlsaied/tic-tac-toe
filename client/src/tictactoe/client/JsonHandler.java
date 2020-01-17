package tictactoe.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.DataInputStream;
import tictactoe.client.gui.InvitationScreen;
import tictactoe.client.gui.MainScreen;
import tictactoe.client.gui.MultiOnlinePlayers;
import tictactoe.client.gui.SignupScreen;

/**
 *
 * @author Tharwat
 */
public class JsonHandler {

    private App app;
    private DataInputStream dataInputStream;
    SignupScreen signupScreen;
    InvitationScreen invitationScreen;
    MultiOnlinePlayers multiOnlinePlayers;
    MainScreen mainScreen;

    JsonHandler(App a) {
        app = a;
        signupScreen = (SignupScreen) app.getScreen("signup");
        invitationScreen = (InvitationScreen) app.getScreen("invitation");
        multiOnlinePlayers = (MultiOnlinePlayers) app.getScreen("multiOnlinePlayers");
        mainScreen = (MainScreen) app.getScreen("main");

    }

    public void handle(JsonObject request) {
        String requestType = request.get("type").getAsString();
        JsonObject requestData = request.getAsJsonObject("data");
        switch (requestType) {
            case "signup-error":
                signupScreen.showSignupFailedPopup();
                break;
            case "signup-success":
                app.showAlert("Welcome :D", "Sign up successful.\nLogin to play :D");
                app.setScreen("signin");
                break;
            case "signin-success":
                app.setScreen("main");
                mainScreen.clearPlayersListPane();
                JsonArray onlinePlayerList = requestData.getAsJsonArray("online-players");
                JsonArray offlinePlayerList = requestData.getAsJsonArray("offline-players");
                mainScreen.addPlayersToOnlineList(onlinePlayerList);
                mainScreen.addPlayersToOfflineList(offlinePlayerList);
                System.out.println(onlinePlayerList);
                break;
            case "signin-error":
                app.showAlert("Could not login", requestData.get("msg").getAsString());
                break;
            case "invitation":
                int challengerId = requestData.get("inviter_player_id").getAsInt();

//                String challengerName = ;
                invitationScreen.setInvitation(challengerId, "Challenger");
                break;
            case "accept-invitation":
                int ch = requestData.get("invited_player_id").getAsInt();
                multiOnlinePlayers.invitationAccepted(ch, "challenger");
                break;
        }
    }
}
