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
public class MultiPlayerGameBoardScreen extends GridPane {

    private final App app;
    Random rand;
    boolean turn, fullBoardFlag;
    int counter, opponentPos;
    String line;
    Vector<Label> tiles = new Vector<>();
    boolean[] textLabelflag;
    char side;

    public MultiPlayerGameBoardScreen(App app) {
        this.app = app;
        setId("stackGameboard");
        for (int i = 0; i < 9; i++) {
            tiles.add(new Label("_"));
        }
        turn = false;
        rand = new Random();
        counter = 0;
        resetGame();
        setPadding(new Insets(40, 0, 0, 50));
        setHgap(150);
        setVgap(-20);
        setPrefSize(700, 700);
        checkWinner();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int x = i * 3 + j;
                tiles.get(x).setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (turn && textLabelflag[x]) {
                            tiles.get(x).setText("X");
                            tiles.get(x).setId("x");
                            turn = false;
                            textLabelflag[x] = false;
                            counter++;
                            checkWinner();
                        }
                        if (turn == false) {
                            doNothing();
                        }
                        checkWinner();
                    }
                });
                add(tiles.get(x), j, i);
            }
        }
    }

    void cpu() {
        PauseTransition pause = new PauseTransition(Duration.seconds(.5));
        pause.setOnFinished((ActionEvent event) -> {
            if (counter == 1 && textLabelflag[4]) {
                opponentPos = 4;
            } else {
                while (!textLabelflag[opponentPos] && counter < 9) {
                    opponentPos = generateCpuPos();
                }
            }
            if (turn == false && textLabelflag[opponentPos]) {
                counter++;
                tiles.get(opponentPos).setText("O");
                tiles.get(opponentPos).setId("o");
                turn = true;
                textLabelflag[opponentPos] = false;
                checkWinner();
            }
        });
        pause.play();
    }

    private int checkCpuPos(String line, int index1, int index2, int index3) {
        int index = 4;
        switch (line) {
            case "X__":
                index = index2;
                break;
            case "__X":
                index = index2;
                break;
            case "_X_":
                index = index3;
                break;
            case "XX_":
                index = index3;
                break;
            case "X_X":
                index = index2;
                break;
            case "_XX":
                index = index1;
                break;
        }
        System.out.println(index);
        return index;
    }

    private int generateCpuPos() {
        String lineGenerator;
        int cpu = rand.nextInt(9);
        for (int x = 0; x < 16; x++) {
            lineGenerator = null;
            switch (x) {
                case 0:
                    lineGenerator = tiles.get(0).getText() + tiles.get(1).getText() + tiles.get(2).getText();
                    if (lineGenerator.equals("X__") || lineGenerator.equals("__X") || lineGenerator.equals("_X_")) {
                        cpu = checkCpuPos(lineGenerator, 0, 1, 2);
                    }
                    break;
                case 1:
                    lineGenerator = tiles.get(3).getText() + tiles.get(4).getText() + tiles.get(5).getText();
                    if (lineGenerator.equals("X__") || lineGenerator.equals("__X") || lineGenerator.equals("_X_")) {
                        cpu = checkCpuPos(lineGenerator, 3, 4, 5);
                    }
                    break;
                case 2:
                    lineGenerator = tiles.get(6).getText() + tiles.get(7).getText() + tiles.get(8).getText();
                    if (lineGenerator.equals("X__") || lineGenerator.equals("__X") || lineGenerator.equals("_X_")) {
                        cpu = checkCpuPos(lineGenerator, 6, 7, 8);
                    }
                    break;
                case 3:
                    lineGenerator = tiles.get(0).getText() + tiles.get(3).getText() + tiles.get(6).getText();
                    if (lineGenerator.equals("X__") || lineGenerator.equals("__X") || lineGenerator.equals("_X_")) {
                        cpu = checkCpuPos(lineGenerator, 0, 3, 6);
                    }
                    break;
                case 4:
                    lineGenerator = tiles.get(1).getText() + tiles.get(4).getText() + tiles.get(7).getText();
                    if (lineGenerator.equals("X__") || lineGenerator.equals("__X") || lineGenerator.equals("_X_")) {
                        cpu = checkCpuPos(lineGenerator, 1, 4, 7);
                    }
                    break;
                case 5:
                    lineGenerator = tiles.get(2).getText() + tiles.get(5).getText() + tiles.get(8).getText();
                    if (lineGenerator.equals("X__") || lineGenerator.equals("__X") || lineGenerator.equals("_X_")) {
                        cpu = checkCpuPos(lineGenerator, 2, 5, 8);
                    }
                    break;
                case 6:
                    lineGenerator = tiles.get(0).getText() + tiles.get(4).getText() + tiles.get(8).getText();
                    if (lineGenerator.equals("X__") || lineGenerator.equals("__X") || lineGenerator.equals("_X_")) {
                        cpu = checkCpuPos(lineGenerator, 0, 4, 8);
                    }
                    break;
                case 7:
                    lineGenerator = tiles.get(2).getText() + tiles.get(4).getText() + tiles.get(6).getText();
                    if (lineGenerator.equals("X__") || lineGenerator.equals("__X") || lineGenerator.equals("_X_")) {
                        cpu = checkCpuPos(lineGenerator, 2, 4, 6);
                    }
                    break;
                case 8:
                    lineGenerator = tiles.get(0).getText() + tiles.get(1).getText() + tiles.get(2).getText();
                    if (lineGenerator.equals("XX_") || lineGenerator.equals("X_X") || lineGenerator.equals("_XX")) {
                        cpu = checkCpuPos(lineGenerator, 0, 1, 2);
                    }
                    break;
                case 9:
                    lineGenerator = tiles.get(3).getText() + tiles.get(4).getText() + tiles.get(5).getText();
                    if (lineGenerator.equals("XX_") || lineGenerator.equals("X_X") || lineGenerator.equals("_XX")) {
                        cpu = checkCpuPos(lineGenerator, 3, 4, 5);
                    }
                    break;
                case 10:
                    lineGenerator = tiles.get(6).getText() + tiles.get(7).getText() + tiles.get(8).getText();
                    if (lineGenerator.equals("XX_") || lineGenerator.equals("X_X") || lineGenerator.equals("_XX")) {
                        cpu = checkCpuPos(lineGenerator, 6, 7, 8);
                    }
                    break;
                case 11:
                    lineGenerator = tiles.get(0).getText() + tiles.get(3).getText() + tiles.get(6).getText();
                    if (lineGenerator.equals("XX_") || lineGenerator.equals("X_X") || lineGenerator.equals("_XX")) {
                        cpu = checkCpuPos(lineGenerator, 0, 3, 6);
                    }
                    break;
                case 12:
                    lineGenerator = tiles.get(1).getText() + tiles.get(4).getText() + tiles.get(7).getText();
                    if (lineGenerator.equals("XX_") || lineGenerator.equals("X_X") || lineGenerator.equals("_XX")) {
                        cpu = checkCpuPos(lineGenerator, 1, 4, 7);
                    }
                    break;
                case 13:
                    lineGenerator = tiles.get(2).getText() + tiles.get(5).getText() + tiles.get(8).getText();
                    if (lineGenerator.equals("XX_") || lineGenerator.equals("X_X") || lineGenerator.equals("_XX")) {
                        cpu = checkCpuPos(lineGenerator, 2, 5, 8);
                    }
                    break;
                case 14:
                    lineGenerator = tiles.get(0).getText() + tiles.get(4).getText() + tiles.get(8).getText();
                    if (lineGenerator.equals("XX_") || lineGenerator.equals("X_X") || lineGenerator.equals("_XX")) {
                        cpu = checkCpuPos(lineGenerator, 0, 4, 8);
                    }
                    break;
                case 15:
                    lineGenerator = tiles.get(2).getText() + tiles.get(4).getText() + tiles.get(6).getText();
                    if (lineGenerator.equals("XX_") || lineGenerator.equals("X_X") || lineGenerator.equals("_XX")) {
                        cpu = checkCpuPos(lineGenerator, 2, 4, 6);
                    }
                    break;
            }
        }
        return cpu;
    }

    private void checkWinner() {
        for (int x = 0; x < 8; x++) {
            line = null;
            switch (x) {
                case 0:
                    line = tiles.get(0).getText() + tiles.get(1).getText() + tiles.get(2).getText();

                    break;
                case 1:
                    line = tiles.get(3).getText() + tiles.get(4).getText() + tiles.get(5).getText();
                    break;
                case 2:
                    line = tiles.get(6).getText() + tiles.get(7).getText() + tiles.get(8).getText();
                    break;
                case 3:

                    line = tiles.get(0).getText() + tiles.get(3).getText() + tiles.get(6).getText();
                    break;
                case 4:
                    line = tiles.get(1).getText() + tiles.get(4).getText() + tiles.get(7).getText();
                    break;
                case 5:
                    line = tiles.get(2).getText() + tiles.get(5).getText() + tiles.get(8).getText();
                    break;
                case 6:
                    line = tiles.get(0).getText() + tiles.get(4).getText() + tiles.get(8).getText();
                    break;
                case 7:
                    line = tiles.get(2).getText() + tiles.get(4).getText() + tiles.get(6).getText();
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
    
    private void resetGame() {
        for (int i = 0; i < tiles.size(); i++) {
            tiles.get(i).setText("_");
            tiles.get(i).setId("label");
        }
        textLabelflag = new boolean[]{true, true, true, true, true, true, true, true, true};
        counter = 0;
        fullBoardFlag = true;

    }

    
    public void setTile(String side, int pos){
        tiles.get(pos).setText(side.toUpperCase());
        tiles.get(pos).setId(side.toLowerCase());
        textLabelflag[pos] = false; 
    }
    
    public void setPlayerNames(String side){
        
    }
    
    public void setPlayerSide(char s){
        side = s;
    }
    
    public void setPlayerTurn(boolean turn){
        for (int i = 0; i < tiles.size(); i++) {
            tiles.get(i).setText("_");
            tiles.get(i).setId("label");
            if(tiles.get(i).getText() == "_")
                textLabelflag[i] = true;
        }
    }

    public void doNothing(){/*nothing for testing purposes*/}
    
    public void showResult(String res){
        if(res.equalsIgnoreCase("won"))
            app.setScreen("youWin");
        else if(res.equalsIgnoreCase("lost"))
            app.setScreen("hardLuck");
    }
}

   
 
