/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package invitation;

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
import javafx.stage.Stage;

/**
 *
 * @author KeR
 */
public class Invitation extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("You Are The Winner");

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
        String str = new String("Kareem");

        Button lose = new Button(str + " Challenges you");
        lose.setPrefSize(250, 20);

        lose.setId("lose");
        lose.setEffect(e);
        ToggleButton Acccept = new ToggleButton("Acccept");
        Acccept.setPrefSize(200, 20);
        Acccept.setId("back");
        ToggleButton Decline = new ToggleButton("Decline");
        Decline.setPrefSize(200, 20);
        Decline.setId("playAgain");
        HBox buttonBox = new HBox(20, Acccept, Decline);

        VBox vbox = new VBox(100, lose, buttonBox);
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
