/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe.client.gui;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.Rectangle;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 *
 * @author hp
 */
public class Levels extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Good Luck Next Time");

        Region rec = new Region();
        rec.setPrefSize(500, 460);
        rec.setId("rec");
       

        DropShadow e = new DropShadow();
        e.setOffsetX(0.0f);
        e.setOffsetY(4.0f);
        e.setBlurType(BlurType.GAUSSIAN);
        e.setColor(Color.BLACK);
        

        Button lose = new Button("Choose A Level");
        lose.setId("lose");
        lose.setEffect(e);
        ToggleButton  easy = new ToggleButton("Easy");
        easy.setPrefSize(332, 83);
        easy.setId("back");
        ToggleButton normal = new ToggleButton("Normal");
        normal.setPrefSize(332, 83);
        normal.setId("playAgain");
        ToggleButton hard = new ToggleButton("Hard");
        hard.setPrefSize(332, 83);
        hard.setId("hard");
        

        VBox vbox = new VBox(30,easy,normal,hard);
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
