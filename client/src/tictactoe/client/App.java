package tictactoe.client;

import com.google.gson.JsonObject;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import tictactoe.client.gui.*;

public class App extends Application {

    HashMap<String, Pane> screens = new HashMap<>();
    Scene mainScene;

    private JsonObject jsonObject;
    private JsonObject data;
    private Socket s;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    public App() {
        try {
            data = new JsonObject();
            data.addProperty("firstName", "mohummad");
            data.addProperty("lastName", "saied");
            data.addProperty("email", "saied@gmail.com");
            data.addProperty("password", "298347923");
            jsonObject = new JsonObject();
            jsonObject.addProperty("type", "signup");
            jsonObject.add("data", data);

            s = new Socket(Config.SERVER_IP, Config.PORT);
            dataInputStream = new DataInputStream(s.getInputStream());
            dataOutputStream = new DataOutputStream(s.getOutputStream());
            String str = jsonObject.toString();
            System.out.println(str);
            dataOutputStream.write(str.getBytes());
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addScreens() {
        screens.put("signin", new SigninScreen(this));
        screens.put("signup", new SignupScreen(this));
        screens.put("hardLuck", new HardLuckScreen(this));
    }

    public void setScreen(String screenName) {
        mainScene.setRoot(screens.get(screenName));
    }

    @Override
    public void start(Stage primaryStage) throws InterruptedException {
        addScreens();
        primaryStage.setTitle("TIC TAC TOE!");

        mainScene = new Scene(screens.get("signin"), 1350, 700);
        mainScene.getStylesheets().add(getClass().getResource("/css/style.css").toString());
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    @Override
    public void init() throws Exception {
        super.init();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

}
