package tictactoe.client.gui;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author hp
 */
public class HardLuck extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Good Luck Next Time");

        Region rec = new Region();
        rec.setPrefSize(498, 460);
        rec.setId("rec");

        Region over = new Region();
        over.setId("over");
        over.setPrefSize(130, 130);
       

        DropShadow e = new DropShadow();
        e.setOffsetX(0.0f);
        e.setOffsetY(4.0f);
        e.setBlurType(BlurType.GAUSSIAN);
        e.setColor(Color.BLACK);
        

        Button lose = new Button("Good Luck Next Time");
        lose.setId("lose");
        lose.setEffect(e);
        ToggleButton back = new ToggleButton("Back");
        back.setPrefSize(180, 20);
        back.setId("back");
        ToggleButton playAgain = new ToggleButton("Play Again");
        playAgain.setPrefSize(180, 20);
        playAgain.setId("playAgain");
        HBox buttonBox = new HBox(50,back,playAgain);
        

        VBox vbox = new VBox(30, over, lose,buttonBox);
                vbox.setId("vbox");


        StackPane stack = new StackPane();
        stack.getChildren().addAll(rec, vbox);
        stack.setId("stack");

        Scene scene = new Scene(stack, 1350, 700);
scene.getStylesheets().add(getClass().getResource("/css/style.css").toString());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
