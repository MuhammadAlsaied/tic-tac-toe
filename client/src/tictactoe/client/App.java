package tictactoe.client;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.stage.StageStyle;
import tictactoe.client.gui.*;

public class App extends Application {

    private HashMap<String, Pane> screens = new HashMap<>();
    private Scene mainScene;
    private Socket s;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private JsonHandler jsonHandler;
    private Stage pStage;
    private Player currentPlayer;
    private double xOffset;
    private double yOffset;
    public static boolean inMultiplayerGame = false;
    public static int opposingPlayerId = -1;
    public static String opposingPlayerName = "";

    public App() {

        addScreens();
        jsonHandler = new JsonHandler(this);
        try {
            s = new Socket(Config.SERVER_IP, Config.PORT);
            dataInputStream = new DataInputStream(s.getInputStream());
            dataOutputStream = new DataOutputStream(s.getOutputStream());
        } catch (IOException e) {
            showAlert("Connection Error.", "Please check your connection and try again.");
        }
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    while (true) {

                        String line = dataInputStream.readUTF();
                        if (line != null) {
                            JsonObject obj = JsonParser.parseString(line).getAsJsonObject();
                            System.out.println(obj);
                            jsonHandler.handle(obj);
                        }
                    }
                } catch (IOException ex) {
                    showAlert("You are disconnected!", "Please check your connection and try again.");
                    setScreen("signin");
                    Platform.exit();
                    ex.printStackTrace();

                }

            }
        }).start();
    }

    public DataOutputStream getDataOutputStream() {
        return dataOutputStream;
    }

    private void addScreens() {
        screens.put("main", new MainScreen(this));
        screens.put("signin", new SigninScreen(this));
        screens.put("signup", new SignupScreen(this));
        screens.put("hardLuck", new HardLuckScreen(this));
        screens.put("invitation", new InvitationScreen(this));
        screens.put("multiOnlinePlayers", new MultiOnlinePlayers(this));       
        screens.put("levels", new LevelsScreen(this));
        screens.put("youWin", new YouWinScreen(this));
        screens.put("playWithComputerEasyGameBoard", new PlayWithComputerEasyGameBoardScreen(this));
        screens.put("playWithComputerNormalGameBoard", new PlayWithComputerNormalGameBoardScreen(this));
        screens.put("playWithComputerHARDGameBoard", new PlayWithComputerHARDGameBoardScreen(this));
        screens.put("nooneIsTheWinner", new NooneIsTheWinnerScreen(this));
        screens.put("playerList", new PlayerListScreen(this));

    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
        System.out.println("current player" + this.currentPlayer);
    }

    public void setScreen(String screenName) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                mainScene.setRoot(screens.get(screenName));
            }
        });
    }

    public Pane getScreen(String screen) {
        return screens.get(screen);
    }

    /////////////////////error handling methods////////////////////////////////
    public void showAlert(String title, String msg) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setTitle(title);
                a.setHeaderText("");
                a.setContentText(msg);
                a.show();
            }
        });
    }

    public void exit() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "signout");
        try {
            dataOutputStream.writeUTF(jsonObject.toString());

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Platform.exit();
        System.exit(0);
    }

    //////////////////////////////////////////////////////////////////////////
    @Override
    public void start(Stage primaryStage) throws InterruptedException {
        pStage = primaryStage;
        primaryStage.setFullScreen(true);
        primaryStage.setTitle("TIC TAC TOE!");


        mainScene = new Scene(screens.get("multiOnlinePlayers"), 1350, 700);
        mainScene.getStylesheets().add(getClass().getResource("/css/style.css").toString());
        primaryStage.setScene(mainScene);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
        mainScene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                makePaneDraggable(primaryStage);
            }
        });
        pStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                if (inMultiplayerGame) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("type", "terminated-game");
                    try {
                        dataOutputStream.writeUTF(jsonObject.toString());

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                exit();
            }
        });
    }

    @Override

    public void init() throws Exception {
        super.init();
    }

    public void sendInvitation(int playerId) {
        JsonObject request = new JsonObject();
        JsonObject data = new JsonObject();
        request.add("data", data);
        request.addProperty("type", "invitation");
        data.addProperty("invited_player_id", playerId);
        try {
            System.out.println("SENT JSON INVITATION: " + request);
            getDataOutputStream().writeUTF(request.toString());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void makePaneDraggable(Stage primaryStage) {
        screens.forEach((key, value) -> {
            value.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    xOffset = event.getSceneX();
                    yOffset = event.getSceneY();
                }
            });
            value.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    primaryStage.setX(event.getScreenX() - xOffset);
                    primaryStage.setY(event.getScreenY() - yOffset);
                }
            });
        });
    }
    
    public void addPointsLocalGame(int points){
        JsonObject request = new JsonObject();
        JsonObject data = new JsonObject();
        request.add("data", data);
        request.addProperty("type", "won-local-game");
        data.addProperty("added-points", points);
        try {
            getDataOutputStream().writeUTF(request.toString());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

}
