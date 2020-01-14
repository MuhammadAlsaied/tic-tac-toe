package tictactoe.client.gui;

import java.util.Vector;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import static javafx.application.Application.launch;
import tictactoe.client.App;
import static javafx.application.Application.launch;
import static javafx.application.Application.launch;

/**
 *
 * @author KeR
 */
public class MultiPlayerGameBpardScreen extends GridPane {

    boolean flag = true;
    int counter;
    String line;
    boolean[] textLabelflag;
    Vector<Label> l = new Vector<>();
    public  MultiPlayerGameBpardScreen(App app) {
        setId("stackGameboard");
        setPadding(new Insets(40, 0, 0, 50));
        setHgap(150);
        setVgap(-20);
        setPrefSize(750, 700);
        for (int i = 0; i < 9; i++) {
            l.add(new Label("_"));
        }
        counter = 0;
        resetGame();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
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
                        } else if (flag == false && textLabelflag[x]) {
                            l.get(x).setText("O");
                            l.get(x).setId("o");
                            flag = true;
                            textLabelflag[x] = false;
                            counter++;
                        }
                        checkWinner();
                    }
                });
                add(l.get(x), j, i);
            }
        }
    }

    void resetGame() {
        for (int i = 0; i < 9; i++) {
            l.get(i).setText("_");
        }
        textLabelflag = new boolean[]{true, true, true, true, true, true, true, true, true};
        for (int i = 0; i < l.size(); i++) {
            l.get(i).setId("label");
        }
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
                    Alert a = new Alert(Alert.AlertType.INFORMATION);
                    a.setTitle("the winner");
                    a.setHeaderText("");
                    a.setContentText("X is the winner");
                    a.show();
                    counter = 0;
                    resetGame();
                    break;
                }
                case "OOO": {
                    Alert a = new Alert(Alert.AlertType.INFORMATION);
                    a.setTitle("O is the winner");
                    a.setHeaderText("");
                    a.setContentText("O is the winner");
                    a.show();
                    counter = 0;
                    resetGame();
                    break;
                }
            }
        }
        if (counter == 9) {
            counter = 0;
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Noone Win");
            a.setHeaderText("");
            a.setContentText("Draw! No winner :/");
            a.show();
            resetGame();
        }
    }
}
