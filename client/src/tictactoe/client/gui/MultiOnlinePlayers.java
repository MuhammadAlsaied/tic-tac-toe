/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe.client.gui;

import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.Random;
import java.util.Vector;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import tictactoe.client.App;

/**
 *
 * @author KeR
 */
public class MultiOnlinePlayers extends Pane {

    GridPane stack;
    private final App app;
    Random rand;
    public static boolean turn = true;
    int counter, cpupos;
    private String line, thisPlayerLetter, opponenetPlayerLetter, challengerName;
    Vector<Label> l = new Vector<>();
    private boolean isEnded = false;

    public MultiOnlinePlayers(App app) {
        stack = new GridPane();
        this.app = app;
        setId("stackGameboard");
        for (int i = 0; i < 9; i++) {
            l.add(new Label("_"));
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int x = i * 3 + j;
                stack.add(l.get(x), j, i);
            }
        }

        rand = new Random();
        counter = 0;
        resetGame();
        stack.setId("stack");
        stack.setPadding(new Insets(40, 0, 0, 50));
        stack.setHgap(150);
        stack.setVgap(-20);
        stack.setPrefSize(750, 700);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int x = i * 3 + j;
                l.get(x).setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (isEnded) {
                            return;
                        }
                        Label currentLabel = (Label) event.getSource();
                        if (turn && currentLabel.getText().equals("_")) {
                            currentLabel.setText(thisPlayerLetter);
                            currentLabel.setId(thisPlayerLetter);
                            turn = false;
                            counter++;
                            stack.requestLayout();
                            sendMoveToServer(x);
                            checkWinner();
                        }
                    }
                });

            }
        }
        setId("stackGameboard");
        Label label1 = new Label("PLAYER1");
        Label label2 = new Label("PLAYER2");

        HBox hbox = new HBox(385, label1, label2);
        hbox.setLayoutX(70);
        hbox.setLayoutY(25);

        TextArea ta = new TextArea(" ");
        ta.setId("ta");
        ta.setLayoutX(890);
        ta.setLayoutY(400);
        ta.setMaxWidth(220.0);
        ta.setMaxHeight(150.0);

        TextArea text = new TextArea("");
        text.setId("text");
        text.setPromptText("Enter your Msg ");
        text.setLayoutX(890);
        text.setLayoutY(600);
        text.setMaxWidth(220.0);
        text.setMaxHeight(10.5);

        Button send = new Button();
        send.setText("send");
        send.setId("send");
        send.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                JsonObject response = new JsonObject();
                JsonObject data = new JsonObject();
                response.add("data", data);
                response.addProperty("type", "Message_sent");
                data.addProperty("msg", ta.getText());
                try {
                    app.getDataOutputStream().writeUTF(response.toString());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }

        });
        send.setLayoutX(1140);
        send.setLayoutY(600);
        getChildren().addAll(stack, hbox, text, ta, send);
        stack.setId("stacklolo");
    }

    private void resetGame() {
        for (int i = 0; i < l.size(); i++) {
            l.get(i).setText("_");
            l.get(i).setId("label");
        }
        counter = 0;
    }

    private void checkWinner() {
        for (int x = 0; x < 8; x++) {
            line = null;
            switch (x) {
                case 0:
                    line = l.get(0).getText() + l.get(1).getText() + l.get(2).getText();

                    break;
                case 1:
                    line = l.get(3).getText() + l.get(4).getText() + l.get(5).getText();
                    break;
                case 2:
                    line = l.get(6).getText() + l.get(7).getText() + l.get(8).getText();
                    break;
                case 3:

                    line = l.get(0).getText() + l.get(3).getText() + l.get(6).getText();
                    break;
                case 4:
                    line = l.get(1).getText() + l.get(4).getText() + l.get(7).getText();
                    break;
                case 5:
                    line = l.get(2).getText() + l.get(5).getText() + l.get(8).getText();
                    break;
                case 6:
                    line = l.get(0).getText() + l.get(4).getText() + l.get(8).getText();
                    break;
                case 7:
                    line = l.get(2).getText() + l.get(4).getText() + l.get(6).getText();
                    break;
            }
            if (line.equals("XXX") || line.equals("OOO")) {
                isEnded = true;
                if (line.contains(thisPlayerLetter)) {
                    app.setScreen("youWin");
                } else {
                    app.setScreen("hardLuck");
                }
                counter = 0;
                resetGame();
                return;
            }
            if (counter == 9) { /*in case of draw*/
                PauseTransition pause = new PauseTransition(Duration.seconds(2));
                pause.setOnFinished((ActionEvent event) -> {
                    app.setScreen("nooneIsTheWinner");
                    counter = 0;
                    resetGame();
                });
                pause.play();
            }
        }

    

    

    public void setOpponentMoveFromServer(String position) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                int moveFromServer = 0;
                switch (position) {
                    case "upper_left":
                        moveFromServer = 0;
                        break;
                    case "up":
                        moveFromServer = 1;
                        break;
                    case "upper_right":
                        moveFromServer = 2;
                        break;
                    case "left":
                        moveFromServer = 3;
                        break;
                    case "center":
                        moveFromServer = 4;
                        break;
                    case "right":
                        moveFromServer = 5;
                        break;
                    case "lower_left":
                        moveFromServer = 6;
                        break;
                    case "down":
                        moveFromServer = 7;
                        break;
                    case "lower_right":
                        moveFromServer = 8;
                        break;
                }
                if (turn == false) {
                    counter++;
                    l.get(moveFromServer).setText(opponenetPlayerLetter);
                    l.get(moveFromServer).setId(opponenetPlayerLetter);
                    checkWinner();
                    turn = true;
//                    stack.requestLayout();
                }
            }
        });

    }

    private void sendMoveToServer(int position) {
        String moveToServer = null;
        JsonObject request = new JsonObject();
        JsonObject data = new JsonObject();
        request.add("data", data);
        request.addProperty("type", "game-move");
        switch (position) {
            case 0:
                moveToServer = "upper_left";
                break;
            case 1:
                moveToServer = "up";
                break;
            case 2:
                moveToServer = "upper_right";
                break;
            case 3:
                moveToServer = "left";
                break;
            case 4:
                moveToServer = "center";
                break;
            case 5:
                moveToServer = "right";
                break;
            case 6:
                moveToServer = "lower_left";
                break;
            case 7:
                moveToServer = "down";
                break;
            case 8:
                moveToServer = "lower_right";
                break;
        }
        data.addProperty("move", thisPlayerLetter);
        data.addProperty("position", moveToServer);
        try {
            System.out.println(request);
            app.getDataOutputStream().writeUTF(request.toString());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void invitationAcceptedSetInviterSide(String challengerName) {
        /*Inviter Side the X*/
        System.out.println("test 1");
        this.challengerName = challengerName;
        System.out.println("test 2");
        System.out.println("test 3");
        opponenetPlayerLetter = "O";
        thisPlayerLetter = "X";
        System.out.println("test 4" + thisPlayerLetter);
        MultiOnlinePlayers.turn = true;
        System.out.println("test 5");
        System.out.println("turn: " + turn + "thisPlayerLetter: " + thisPlayerLetter + "opponentPlayerLetter: " + opponenetPlayerLetter);
        System.out.println("test 6");
        app.setScreen("multiOnlinePlayers");

    }

    public void acceptInvitationInvitedSide(int challengerId, String challengerName) {
        /*Invited Side the O*/
        this.challengerName = challengerName;
        MultiOnlinePlayers.turn = false;
        opponenetPlayerLetter = "X";
        thisPlayerLetter = "O";
        System.out.println("Accept this letter: " + thisPlayerLetter);

        System.out.println("turn: " + turn + "thisPlayerLetter: " + thisPlayerLetter + "opponentPlayerLetter: " + opponenetPlayerLetter);
    }
}
