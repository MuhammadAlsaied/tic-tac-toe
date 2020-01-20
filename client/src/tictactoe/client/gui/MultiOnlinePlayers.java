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
import javafx.scene.layout.VBox;
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
    Label label1, label2;
    private TextArea chatTextArea;
    private TextArea chatMessageArea;

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
        label1 = new Label("PLAYER1");
        label1.setPrefWidth(150);
        label2 = new Label("PLAYER2");
        label2.setPrefWidth(150);

        HBox hbox = new HBox(350, label1, label2);
        hbox.setLayoutX(70);
        hbox.setLayoutY(25);

        chatTextArea = new TextArea(" ");
        chatTextArea.setId("ta");
        chatTextArea.setEditable(false);
        chatTextArea.setLayoutX(890);
        chatTextArea.setLayoutY(420);
        chatTextArea.setMaxWidth(220.0);
        chatTextArea.setMaxHeight(250.0);
        chatTextArea.setWrapText(true);

        chatMessageArea = new TextArea("");
        chatMessageArea.setId("text");
        chatMessageArea.setPromptText("Enter your Msg ");
        chatMessageArea.setLayoutX(890);
        chatMessageArea.setLayoutY(680);
        chatMessageArea.setMaxWidth(220.0);
        chatMessageArea.setMaxHeight(10.5);
        chatMessageArea.setWrapText(true);

        Button send = new Button();
        send.setText("send");
        send.setId("send");
        send.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (chatMessageArea.getText().isEmpty()) {
                    return;
                }
                JsonObject response = new JsonObject();
                JsonObject data = new JsonObject();
                response.add("data", data);
                response.addProperty("type", "game-message");
                data.addProperty("msg", chatMessageArea.getText());
                chatTextArea.setText(chatTextArea.getText() + "\n" + app.getCurrentPlayer().getFirstName() + ": " + chatMessageArea.getText());
                chatMessageArea.setText("");
                try {
                    app.getDataOutputStream().writeUTF(response.toString());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }

        });
        send.setLayoutX(1140);
        send.setLayoutY(680);
        ///////////////////////////////////////////////////////////
            Button exit = new Button("EXIT");
        exit.setId("ExitFromGame");
        exit.setLayoutX(280);
        exit.setLayoutY(650);
        exit.setPrefSize(110, 10);
        exit.setOnAction((t) -> {
            app.exit();
        });
        Button pause = new Button("PAUSE");
        pause.setPrefSize(110, 10);
        pause.setId("BackToMain");
        pause.setOnAction((event) -> {
            
        });

        HBox hBox = new HBox(100, pause, exit);

        VBox v = new VBox(40, hBox);
        v.setId("vbox");
        v.setLayoutX(1000);
        v.setLayoutY(150);
        ///////////////////////////////////////////////////////////
        getChildren().addAll(stack, hbox,v, chatMessageArea, chatTextArea, send);
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
                    JsonObject request = new JsonObject();
                    JsonObject data = new JsonObject();
                    request.addProperty("type", "multiplayer-game-end");
                    request.add("data", data);
                    data.addProperty("winner-id", app.getCurrentPlayer().getId());
                    try {
                        app.getDataOutputStream().writeUTF(request.toString());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    app.setScreen("hardLuck");
                }

                counter = 0;
                resetGame();
                return;
            }
            if (counter == 9) {
                /*in case of draw*/
                PauseTransition pause = new PauseTransition(Duration.seconds(2));
                pause.setOnFinished((ActionEvent event) -> {
                    app.setScreen("nooneIsTheWinner");
                    counter = 0;
                    resetGame();
                });
                pause.play();
            }
        }
    }

    public void setOpponentMoveFromServer(String position) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                int moveFromServer = getGamePositionAsIndex(position);

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

    public void invitationAcceptedSetInviterSide(String challengerName, int opposingPlayerId) {
        App.inMultiplayerGame = true;
        App.opposingPlayerId = opposingPlayerId;
        App.opposingPlayerName = challengerName;
        label1.setText(app.getCurrentPlayer().getFirstName());
        label2.setText(challengerName);
        /*Inviter Side the X*/
        System.out.println("test 1");
        this.challengerName = challengerName;
        System.out.println("test 2");
        System.out.println("test 3");
        opponenetPlayerLetter = "O";
        thisPlayerLetter = "X";
        System.out.println("test 4" + thisPlayerLetter);
        isEnded = false;
        MultiOnlinePlayers.turn = true;
        chatTextArea.setText("");
        chatMessageArea.setText("");
        System.out.println("test 5");
        System.out.println("turn: " + turn + "thisPlayerLetter: " + thisPlayerLetter + "opponentPlayerLetter: " + opponenetPlayerLetter);
        System.out.println("test 6");
        app.setScreen("multiOnlinePlayers");
    }

    public void acceptInvitationInvitedSide(int challengerId, String challengerName) {
        /*Invited Side the O*/
        App.inMultiplayerGame = true;
        App.opposingPlayerName = challengerName;
        App.opposingPlayerId = challengerId;
        this.challengerName = challengerName;
        label1.setText(challengerName);
        label2.setText(app.getCurrentPlayer().getFirstName());
        isEnded = false;
        MultiOnlinePlayers.turn = false;
        chatTextArea.setText("");
        chatMessageArea.setText("");
        opponenetPlayerLetter = "X";
        thisPlayerLetter = "O";
        System.out.println("Accept this letter: " + thisPlayerLetter);
        System.out.println("turn: " + turn + "thisPlayerLetter: " + thisPlayerLetter + "opponentPlayerLetter: " + opponenetPlayerLetter);
    }


    public void setNewMsg(String msg) {
        chatTextArea.appendText(challengerName + ": " + msg + "\n");
    }

    public void setGameCoordinates(JsonObject gameCoordinates, int playerX_id) {
        counter = 0;
        gameCoordinates.keySet().forEach((position) -> {
            String label = gameCoordinates.get(position).getAsString();
            int index = getGamePositionAsIndex(position);
            if (label.equals("-")) {
                l.get(index).setText("_");
                l.get(index).setId("label");
            } else {
                counter++;
                l.get(index).setText(label);
                l.get(index).setId(label);
            }

        });
        System.out.println("counter: " + counter);
        if (playerX_id == app.getCurrentPlayer().getId()) {
            opponenetPlayerLetter = "O";
            thisPlayerLetter = "X";
            label1.setText(app.getCurrentPlayer().getFirstName());
            label2.setText(challengerName);
            turn = counter % 2 == 0;
        } else {
            opponenetPlayerLetter = "X";
            thisPlayerLetter = "O";
            label1.setText(challengerName);
            label2.setText(app.getCurrentPlayer().getFirstName());
            turn = counter % 2 != 0;
        }
        System.out.println("turn: " + turn);
    }

    private int getGamePositionAsIndex(String position) {
        int index = 0;
        switch (position) {
            case "upper_left":
                index = 0;
                break;
            case "up":
                index = 1;
                break;
            case "upper_right":
                index = 2;
                break;
            case "left":
                index = 3;
                break;
            case "center":
                index = 4;
                break;
            case "right":
                index = 5;
                break;
            case "lower_left":
                index = 6;
                break;
            case "down":
                index = 7;
                break;
            case "lower_right":
                index = 8;
                break;
        }
        return index;
    }
}
