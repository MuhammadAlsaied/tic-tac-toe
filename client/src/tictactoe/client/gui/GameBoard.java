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

/**
 *
 * @author KeR
 */
public class GameBoard extends Application {

    boolean flag = true;
    int i, j, y, x,counter;
    String line;
    Vector<Label> l = new Vector<>();

    @Override
    public void init() throws Exception {
        super.init();
        for (int i = 0; i < 9; i++) {
            l.add(new Label("_"));
        }
        counter=0;

    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Game");
        GridPane stack = new GridPane();
        stack.setId("stackGameboard");
        for (int i = 0; i < 9; i++) {
            l.get(i).setText("_");
        }
        boolean[] textLabelflag = {true, true, true, true, true, true, true, true, true};
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
                        } else if (flag == false && textLabelflag[x]) {
                            l.get(x).setText("O");
                            l.get(x).setId("o");
                            flag = true;
                            textLabelflag[x] = false;
                            counter++;
                        }
                        checkWinner(primaryStage);
                    }
                });
                stack.add(l.get(x), j, i);
            }
        }
        x = 0;
        new Thread(new Runnable() {
            @Override
            public void run() {
            }
        }).start();
        Scene scene = new Scene(stack, 1350, 700);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toString());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void checkWinner(Stage primaryStage) {
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
                    start(primaryStage);
                    counter = 0;
                    break;
                }
                case "OOO": {
                    Alert a = new Alert(Alert.AlertType.INFORMATION);
                    a.setTitle("O is the winner");
                    a.setHeaderText("");
                    a.setContentText("O is the winner");
                    a.show();
                    start(primaryStage);
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
            a.setContentText("Draw! No winner :/");
            a.show();
            start(primaryStage);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
