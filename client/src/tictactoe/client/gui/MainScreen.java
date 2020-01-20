package tictactoe.client.gui;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.Comparator;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import tictactoe.client.App;
import tictactoe.client.Player;

public class MainScreen extends Pane {

    Comparator<Player> playerComparatorByPoints = (o1, o2) -> {
        int diff = o1.getPoints() - o2.getPoints();
        if (diff == 0) {
            if (o1.getId() < o2.getId()) {
                return 1;
            } else {
                return -1;
            }
        }
        return diff;
    };

    private int playersListCounter;
    private GridPane gridPane;
    private App app;
    private TextArea chatMessageArea, chatTextArea;
    private Player currentPlayer;
    private Label welcome;
    private Label points;
    private Label playerName2;
    private Label points2;

    public MainScreen(App app) {
        this.app = app;
        ToggleButton challengeComp = new ToggleButton("CHALLENGE COMPUTER");
        challengeComp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                app.setScreen("levels");
            }
        });
        challengeComp.setPrefSize(280, 50);
        challengeComp.setId("compButton");
        ToggleButton challengePlayer = new ToggleButton("CHALLENGE PLAYER");
        challengePlayer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                app.setScreen("playerList");
            }
        });
        challengePlayer.setPrefSize(280, 50);
        challengePlayer.setId("playerButton");
        HBox buttonBox = new HBox(20, challengeComp, challengePlayer);
        buttonBox.setAlignment(Pos.CENTER_LEFT);
        buttonBox.setLayoutX(80);
        buttonBox.setLayoutY(450);
        gridPane = new GridPane();
        gridPane.setId("GridMain");
        gridPane.setHgap(30);
        gridPane.setPrefSize(495.2, 250.0);

        //////////////////////////////////////////////////////////
        Button exit = new Button("EXIT");
        exit.setId("ExitFromGame");
        exit.setPrefSize(110, 10);
        exit.setOnAction((t) -> {
            app.exit();
        });

        HBox hBox = new HBox(exit);
        hBox.setLayoutX(1100);
        hBox.setLayoutY(300);
        /////////////////////////////////////////////////////////////////

        ScrollPane scrollPane = new ScrollPane(gridPane);
        scrollPane.setId("scrollPane1");
        scrollPane.setFocusTraversable(false);

        scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        scrollPane.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);

        VBox v = new VBox();
        v.getChildren().add(scrollPane);
        v.setLayoutX(930);
        v.setLayoutY(0);
        
        playerName2 = new Label();
        playerName2.setLayoutX(200);
        playerName2.setLayoutY(260);
        
        welcome = new Label();
        welcome.setId("welcome");
        welcome.setLayoutX(150);
        welcome.setLayoutY(200);
        
        points2 = new Label();
        points2.setLayoutX(500);
        points2.setLayoutY(250);

        points = new Label();
        points.setId("welcome");
        points.setLayoutX(450);
        points.setLayoutY(200);

        //String variable="WELCOME " + ;
        chatTextArea = new TextArea("");
        chatTextArea.setId("ta");
        chatTextArea.setEditable(false);
        chatTextArea.setLayoutX(800);
        chatTextArea.setLayoutY(420);
        chatTextArea.setMaxWidth(220.0);
        chatTextArea.setMaxHeight(250.0);
        chatTextArea.setWrapText(true);

        chatMessageArea = new TextArea("");

        chatMessageArea.setPromptText("Enter your Msg ");
        chatMessageArea.setLayoutX(800);
        chatMessageArea.setLayoutY(700);
        chatMessageArea.setMaxWidth(220.0);
        chatMessageArea.setMaxHeight(10.5);
        chatMessageArea.setWrapText(true);

        Button send = new Button();
        send.setText("send");
        send.setId("sendChatMainScreen");
        send.setLayoutX(1050);
        send.setLayoutY(700);

        send.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                sendGlobalMsg(chatMessageArea.getText());
                chatMessageArea.clear();
            }
        });


        getChildren().addAll(buttonBox, chatMessageArea, chatTextArea, send, v, hBox, welcome, points , playerName2 , points2);
        setId("MainScreenPane");
    }

    public void setPlayersListCounter(int playersListCounter) {
        this.playersListCounter = playersListCounter;
    }

    public void clearPlayersListPane() {
        gridPane.getChildren().clear();
    }

    public void addPlayersToList(JsonArray playerList, String type, Color color) {
        for (int i = 0; i < playerList.size(); i++) {
            JsonObject jsonPlayer = playerList.get(i).getAsJsonObject();
            if (jsonPlayer.get("id").getAsInt() == app.getCurrentPlayer().getId()) {
                /*skips iteration if the player is me*/
                continue;
            }
            Player player = new Player();
            player.setLastName(jsonPlayer.get("lastName").getAsString());
            player.setPoints(jsonPlayer.get("points").getAsInt());
            player.setId(jsonPlayer.get("id").getAsInt());
            ToggleButton invite2 = new ToggleButton("Challenge");
            if(type.equals("online")){
            invite2.setId("challengeScrolPaneMainScreen");
            }
            else if (type.equals("offline")){
                invite2.setId("offlineChallengeScrolPaneMainScreen");
            }
            invite2.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    app.sendInvitation(player.getId());
                }
            });

            Label score2 = new Label(Integer.toString(player.getPoints()));
            score2.setId("scoreLabel");
            Label playerName = new Label(player.getLastName());
            playerName.setPrefWidth(100);
            Circle cir2 = new Circle(150.0f, 150.0f, 5.f);
            cir2.setFill(color);
            gridPane.add(cir2, 0, playersListCounter);
            gridPane.add(invite2, 3, playersListCounter);
            gridPane.add(score2, 2, playersListCounter);
            gridPane.add(playerName, 1, playersListCounter);
            playersListCounter++;
        }
    }

    public void sendGlobalMsg(String msg) {
        JsonObject request = new JsonObject();
        JsonObject data = new JsonObject();
        request.add("data", data);
        request.addProperty("type", "global_chat_message");
        data.addProperty("sender", app.getCurrentPlayer().getFirstName().toString());
        data.addProperty("message", msg.toString());

        try {
            System.out.println("SENT JSON GLOBAL MESSAGE: " + request);
            app.getDataOutputStream().writeUTF(request.toString());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void setGlobalMsgFromServer(String sender, String msg) {
        chatTextArea.appendText(sender + ": " + msg + "\n");
    }

    public void setWelcomePlayer(String playerName, int totalPoints) {
        playerName2.setText(playerName);
        playerName2.setId("welcome1");
        points2.setText(Integer.toString(totalPoints));
        points2.setId("scoreLabel");
        welcome.setText("Welcome,");
        points.setText("Points");

    }
}
