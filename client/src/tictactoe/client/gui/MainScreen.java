package tictactoe.client.gui;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.Comparator;
import java.util.TreeSet;
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
    private final TreeSet<Player> sortedOnlinePlayersbyPoints = new TreeSet<>(playerComparatorByPoints);
    private final TreeSet<Player> sortedOfflinePlayersbyPoints = new TreeSet<>(playerComparatorByPoints);
    private GridPane gridPane;
    private Player player;
    private App app;

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
        challengePlayer.setPrefSize(280, 50);
        challengePlayer.setId("playerButton");
        HBox buttonBox = new HBox(20, challengeComp, challengePlayer);
        buttonBox.setAlignment(Pos.CENTER_LEFT);
        buttonBox.setLayoutX(80);
        buttonBox.setLayoutY(350);
        gridPane = new GridPane();
        gridPane.setId("GridMain");
        gridPane.setHgap(50);

        gridPane.setPrefSize(495.2, 250.0);
        Button send = new Button();
        send.setText("send");
        send.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
            }
        });
        send.setId("sendChatMainScreen");
        send.setLayoutX(1050);
        send.setLayoutY(700);
        TextArea ta = new TextArea(" ");
        send.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                JsonObject response = new JsonObject();
                JsonObject data = new JsonObject();
                response.add("data", data);
                response.addProperty("type", "global_chat_message");
                data.addProperty("message", ta.getText());
                try {
                    app.getDataOutputStream().writeUTF(response.toString());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        });
        Button exit = new Button("EXIT");
        exit.setId("back");
        exit.setLayoutX(280);
        exit.setLayoutY(650);
        exit.setPrefSize(150, 50);
        exit.setOnAction((t) -> {
            app.exit();
        });
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
        ta.setId("ta");
        ta.setLayoutX(800);
        ta.setLayoutY(420);
        ta.setMaxWidth(220.0);
        ta.setMaxHeight(250.0);

        TextArea text = new TextArea("");

        text.setPromptText("Enter your Msg ");
        text.setLayoutX(800);
        text.setLayoutY(700);
        text.setMaxWidth(220.0);
        text.setMaxHeight(10.5);

        Image img2 = new Image(getClass().getResourceAsStream("/images/k.png"));
        Label labelk = new Label();
        labelk.setGraphic(new ImageView(img2));

        labelk.setLayoutX(760);
        labelk.setLayoutY(20);
        labelk.setMaxSize(50.0, 50.0);

        labelk.setFont(new Font("Arial", 24));

        getChildren().addAll(buttonBox, text, ta, send, v, labelk, exit);
        setId("MainScreenPane");
    }

    public void clearPlayersListPane() {
        gridPane.getChildren().clear();
    }

    public void addPlayersToOnlineList(JsonArray onlinePlayerList) {
        playersListCounter = 0;
        for (int i = 0; i < onlinePlayerList.size(); i++) {
            JsonObject jsonPlayer = onlinePlayerList.get(i).getAsJsonObject();
            if (app.getCurrentPlayer().getId() == jsonPlayer.get("id").getAsInt()) {
                /*skips iteration if the player is me*/
                continue;
            }
            player = new Player();
            player.setFirstName(jsonPlayer.get("firstName").toString());
            player.setPoints(jsonPlayer.get("points").getAsInt());
            player.setId(jsonPlayer.get("id").getAsInt());
            sortedOnlinePlayersbyPoints.add(player);

            ToggleButton invite2 = new ToggleButton("Challenge");
            invite2.setId("challengeScrolPaneMainScreen");
            invite2.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    JsonObject request = new JsonObject();
                    JsonObject data = new JsonObject();
                    request.add("data", data);
                    request.addProperty("type", "invitation");
                    data.addProperty("invited_player_id", player.getId());
                    try {
                        app.getDataOutputStream().writeUTF(request.toString());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });

            Label score2 = new Label(Integer.toString(player.getPoints()));
            score2.setId("scoreLabel");
            Label playerName = new Label(player.getFirstName());
            Circle cir2 = new Circle(150.0f, 150.0f, 5.f);
            cir2.setFill(Color.GREEN);
            gridPane.add(cir2, 0, playersListCounter);
            gridPane.add(invite2, 3, playersListCounter);
            gridPane.add(score2, 2, playersListCounter);
            gridPane.add(playerName, 1, playersListCounter);
            playersListCounter++;

        }
    }

    public void addPlayersToOfflineList(JsonArray offlinePlayerList) {
        for (int i = 0; i < offlinePlayerList.size(); i++) {
            JsonObject jsonPlayer = offlinePlayerList.get(i).getAsJsonObject();
            if (app.getCurrentPlayer().getId() == jsonPlayer.get("id").getAsInt()) {
                /*skips iteration if the player is me*/
                continue;
            }
            player = new Player();
            player.setFirstName(jsonPlayer.get("firstName").toString());
            player.setPoints(jsonPlayer.get("points").getAsInt());
            player.setId(jsonPlayer.get("id").getAsInt());
            sortedOfflinePlayersbyPoints.add(player);

            ToggleButton invite2 = new ToggleButton("Challenge");
            invite2.setId("offlineChallengeScrolPaneMainScreen");
            Label score2 = new Label(Integer.toString(player.getPoints()));
            score2.setId("scoreLabel");
            Label playerName = new Label(player.getFirstName());
            Circle cir2 = new Circle(150.0f, 150.0f, 5.f);
            cir2.setFill(Color.RED);
            gridPane.add(cir2, 0, playersListCounter);
            gridPane.add(invite2, 3, playersListCounter);
            gridPane.add(score2, 2, playersListCounter);
            gridPane.add(playerName, 1, playersListCounter);
            playersListCounter++;
        }
    }

}
