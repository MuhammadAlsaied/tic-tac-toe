package tictactoe.client.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

/**
 *
 * @author KeR
 */
public class PlayersList extends Application {
    
    @Override
    public void start(Stage primaryStage) 
    {
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


        GridPane gr = new GridPane();
        gr.setHgap(50);
        for(int i=0 ; i<7 ; i++)
        {
            ToggleButton invite2 = new ToggleButton("Challenge");
            invite2.setId("back");
            Label score2 = new Label("500");
            score2.setId("scoreLabel");
            Label imglabel2 = new Label();
            Image img2 = new Image(getClass().getResourceAsStream("/images/k.png"));
            imglabel2.setGraphic(new ImageView(img2));
            Circle cir2 = new Circle(150.0f, 150.0f, 5.f);
            cir2.setFill(Color.GREEN);
            gr.add(cir2,0,i);
            gr.add(invite2, 3, i);
            gr.add(score2, 2, i);
            gr.add(imglabel2, 1, i);
        }

        
        VBox box = new VBox(30,gr);
        box.setPrefHeight(500);
        box.setId("vbox");
        StackPane stack = new StackPane();
        stack.setId("stack");
        stack.getChildren().addAll(rec,box);
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
