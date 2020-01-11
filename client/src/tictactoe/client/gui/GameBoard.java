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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import tictactoe.client.App;

/**
 *
 * @author sabreen
 */
public class GameBoard extends Pane {

    private App app;
    boolean[] textLabelflag;
    Random rand;
    boolean flag = true, userflag = true;
    int i, j, y, x, counter, cpupos;
    String line;
    Vector<Label> l = new Vector<>();

    public GameBoard(App app) {
        for (int i = 0; i < 9; i++) {
            l.add(new Label("_"));
        }
        flag = true;
        reetGame();
        rand = new Random();
        counter = 0;
        this.app = app;
        GridPane stack = new GridPane();
        stack.setId("stack");
        stack.setPadding(new Insets(40, 0, 0, 50));
        stack.setHgap(150);
        stack.setVgap(-20);
        stack.setPrefSize(700, 700);
        for (int i = 0; i < l.size(); i++) {
            l.get(i).setId("label");
        }
        for (i = 0; i < 3; i++) {
            for (j = 0; j < 3; j++) {
                int x = i * 3 + j;
                l.get(x).setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {

                        if (flag && textLabelflag[x]) {
                            l.get(x).setText("X");
                            l.get(x).setId("x");
                            flag = false;
                            textLabelflag[x] = false;
                            counter++;
                        }
                        while (!textLabelflag[cpupos]) {
                            if (counter == 9) {
                                checkWinner();
                                break;
                            }
                            cpupos = rand.nextInt(9);
                        }
                        System.out.println(cpupos);
                        if (flag == false && textLabelflag[cpupos]) {
                            counter++;
                            l.get(cpupos).setText("O");
                            l.get(cpupos).setId("o");
                            flag = true;
                            textLabelflag[cpupos] = false;
                        }
                        checkWinner();
                    }
                });
                checkWinner();
                stack.add(l.get(x), j, i);
            }
        }
        //
        setId("stackGameboard");
        Label label1 = new Label("player1");
        Label label2 = new Label("player2");

        HBox hbox = new HBox(380, label1, label2);
        hbox.setLayoutX(70);
        hbox.setLayoutY(15);

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

    private void reetGame() {
        for (int i = 0; i < 9; i++) {
            l.get(i).setText("_");
        }
        for (int i = 0; i < l.size(); i++) {
            l.get(i).setId("label");
        }
        userflag = true;
        textLabelflag = new boolean[]{true, true, true, true, true, true, true, true, true};
    }

    private void checkWinner() {
        for (x = 0; x < 8; x++) {
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
            switch (line) {
                case "XXX": {
                    Alert a = new Alert(Alert.AlertType.INFORMATION);
                    a.setTitle("the winner");
                    a.setHeaderText("");
                    a.setContentText("X is the winner");
                    a.show();
                    reetGame();
                    counter = 0;
                    break;
                }
                case "OOO": {
                    Alert a = new Alert(Alert.AlertType.INFORMATION);
                    a.setTitle("O is the winner");
                    a.setHeaderText("");
                    a.setContentText("O is the winner");
                    a.show();
                    reetGame();
                    counter = 0;
                    break;
                }
            }
        }
        if (counter == 9) {
            counter = 0;
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Noone Win");
            a.setHeaderText("");
            a.setContentText("Noone Win");
            a.show();
            reetGame();
        }
    }

}
