/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe.client.gui;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import tictactoe.client.App;
import tictactoe.client.Player;

public class PlayerListScreen extends Pane {

    private App app;
    private GridPane gridPane;
    private int playersListCounter;

    public PlayerListScreen(App app) {
        this.app = app;
        gridPane = new GridPane();
        gridPane.setHgap(30);
        Region rectBack = new Region();
        rectBack.setPrefSize(498, 460);
        rectBack.setId("rectBack");
        rectBack.setLayoutX(400.0);
        rectBack.setLayoutY(150.0);
        rectBack.setMaxHeight(450.0);

        ScrollPane scrollPane = new ScrollPane(gridPane);
        scrollPane.setId("scrolPanePlaylistScreen");
        scrollPane.setFocusTraversable(false);
        scrollPane.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setLayoutX(450.0);
        scrollPane.setLayoutY(150.0);
        scrollPane.setMaxHeight(450.0);

        setId("stack");
        ////////////////////////////////////////////////////
        Button exit = new Button("EXIT");
        exit.setId("ExitFromGame");
        exit.setLayoutX(280);
        exit.setLayoutY(650);
        exit.setPrefSize(110, 10);
        exit.setOnAction((t) -> {
            app.exit();
        });
        Button back = new Button("Back");
        back.setPrefSize(110, 10);
        back.setId("BackToMain");
        back.setOnAction((event) -> {
            app.setScreen("main");
            App.inMultiplayerGame = false;
            App.opposingPlayerId = -1;
            App.opposingPlayerName = "";
        });

        HBox hBox = new HBox(150, back, exit);

        VBox v = new VBox(hBox);
        v.setId("vbox");
        v.setLayoutX(460);
        v.setLayoutY(530);

        ////////////////////////////////////////////////////
        getChildren().addAll(rectBack, v, scrollPane);

    }

    public void setPlayersListCounter(int playersListCounter) {
        this.playersListCounter = playersListCounter;
    }

    public void addPlayersToList(JsonArray playerList, Color color) {
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
            invite2.setId("challengeScrolPaneMainScreen");
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
            playerName.setId("playerName");
            Circle cir2 = new Circle(150.0f, 150.0f, 5.f);
            cir2.setFill(color);
            gridPane.add(cir2, 0, playersListCounter);
            gridPane.add(invite2, 3, playersListCounter);
            gridPane.add(score2, 2, playersListCounter);
            gridPane.add(playerName, 1, playersListCounter);
            playersListCounter++;
        }
    }
}
