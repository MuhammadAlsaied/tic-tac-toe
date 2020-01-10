/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe.client.gui;

import java.util.Random;
import java.util.Vector;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import tictactoe.client.App;

/**
 *
 * @author KeR
 */
public class PlayWithComputerEasyGameBoardScreen extends GridPane {

    private final App app;
    Random rand;
    boolean turn, fullBoardFlag;
    int counter, cpupos;
    String line;
    Vector<Label> l = new Vector<>();
    boolean[] textLabelflag;

    public PlayWithComputerEasyGameBoardScreen(App app) {
        this.app = app;
        setId("stackGameboard");
        for (int i = 0; i < 9; i++) {
            l.add(new Label("_"));
        }
        turn = true;
        resetGame();
        rand = new Random();
        counter = 0;
        cpupos = 0;
        setPadding(new Insets(40, 0, 0, 50));
        setHgap(150);
        setVgap(-20);
        setPrefSize(700, 700);
        checkWinner();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int x = i * 3 + j;
                l.get(x).setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (turn && textLabelflag[x]) {
                            l.get(x).setText("X");
                            l.get(x).setId("x");
                            turn = false;
                            textLabelflag[x] = false;
                            counter++;
                            checkWinner();
                        }
                        if (turn == false) {
                            cpu();
                        }
                        checkWinner();
                    }
                });
                add(l.get(x), j, i);
            }
        }
    }

    void cpu() {
        PauseTransition pause = new PauseTransition(Duration.seconds(.5));
        pause.setOnFinished((ActionEvent event) -> {
            while (!textLabelflag[cpupos] && counter < 9) {
                cpupos = rand.nextInt(9);
            }
            if (turn == false && textLabelflag[cpupos]) {
                counter++;
                l.get(cpupos).setText("O");
                l.get(cpupos).setId("o");
                turn = true;
                textLabelflag[cpupos] = false;
                checkWinner();
            }
        });
        pause.play();
    }

    private void resetGame() {
        for (int i = 0; i < l.size(); i++) {
            l.get(i).setText("_");
            l.get(i).setId("label");
        }
        textLabelflag = new boolean[]{true, true, true, true, true, true, true, true, true};
        counter = 0;
        fullBoardFlag = true;

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
            switch (line) {
                case "XXX": {
                    fullBoardFlag = false;
                    turn = true;
                    for (int i = 0; i < 9; i++) {
                        if (textLabelflag[i]) {
                            textLabelflag[i] = false;
                        }
                    }
                    PauseTransition pause = new PauseTransition(Duration.seconds(2));
                    pause.setOnFinished((ActionEvent event) -> {
                        app.setScreen("youWin");
                        counter = 0;
                        resetGame();
                    });
                    pause.play();
                    break;
                }
                case "OOO": {
                    fullBoardFlag = false;
                    turn = true;
                    for (int i = 0; i < 9; i++) {
                        if (textLabelflag[i]) {
                            textLabelflag[i] = false;
                        }
                    }
                    PauseTransition pause = new PauseTransition(Duration.seconds(2));
                    pause.setOnFinished((ActionEvent event) -> {
                        app.setScreen("hardLuck");
                        counter = 0;
                        resetGame();
                    });
                    pause.play();
                    break;
                }
            }
        }
        if (counter == 9 && fullBoardFlag) {
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
