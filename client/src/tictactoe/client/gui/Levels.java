<<<<<<< Updated upstream:client/src/tictactoe/client/gui/Levels.java
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
import tictactoe.client.App;

/**
 *
 * @author hp
 */
public class Levels extends StackPane {

    private final App app;
    public Levels(App app) {
        this.app = app;
        Region rec = new Region();
        rec.setPrefSize(500, 460);
        rec.setId("rec");
       

        DropShadow e = new DropShadow();
        e.setOffsetX(0.0f);
        e.setOffsetY(4.0f);
        e.setBlurType(BlurType.GAUSSIAN);
        e.setColor(Color.BLACK);
        

        Button chooseALevel = new Button("Choose A Level");
        chooseALevel.setId("ChooseALevel");
        chooseALevel.setEffect(e);
        chooseALevel.setPrefSize(332, 83);
        ToggleButton  easy = new ToggleButton("Easy");
        easy.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                app.setScreen("playWithComputerEasyGameBoard");
            }
        });
        easy.setPrefSize(280, 83);
        easy.setId("easyButton");
        ToggleButton normal = new ToggleButton("Normal");
        normal.setPrefSize(280, 83);
        normal.setId("normalButton");
        ToggleButton hard = new ToggleButton("Hard");
        hard.setPrefSize(280, 83);
        hard.setId("hardButton");
        

        VBox vbox = new VBox(30,chooseALevel,easy,normal,hard);
                vbox.setId("vbox");


        getChildren().addAll(rec, vbox);
        setId("stackGameResultScreen");

    }

}
=======
package tictactoe.client.gui;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import tictactoe.client.App;

/**
 *
 * @author hp
 */
public class LevelsScreen extends StackPane {

    private final App app;
    public LevelsScreen(App app) {
        this.app = app;
        Region rec = new Region();
        rec.setPrefSize(500, 460);
        rec.setId("rec");
       

        DropShadow e = new DropShadow();
        e.setOffsetX(0.0f);
        e.setOffsetY(4.0f);
        e.setBlurType(BlurType.GAUSSIAN);
        e.setColor(Color.BLACK);
        

        Button chooseALevel = new Button("Choose A Level");
        chooseALevel.setId("ChooseALevel");
        chooseALevel.setEffect(e);
        chooseALevel.setPrefSize(332, 83);
        ToggleButton  easy = new ToggleButton("Easy");
        easy.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                app.setScreen("playWithComputerEasyGameBoard");
            }
        });
        easy.setPrefSize(280, 83);
        easy.setId("easyButton");
        ToggleButton normal = new ToggleButton("Normal");
        normal.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                app.setScreen("playWithComputerNormalGameBoard");
            }
        });
        normal.setPrefSize(280, 83);
        normal.setId("normalButton");
        ToggleButton hard = new ToggleButton("Hard");
        hard.setPrefSize(280, 83);
        hard.setId("hardButton");
        

        VBox vbox = new VBox(30,chooseALevel,easy,normal,hard);
                vbox.setId("vbox");


        getChildren().addAll(rec, vbox);
        setId("stackGameResultScreen");

    }

}
>>>>>>> Stashed changes:client/src/tictactoe/client/gui/LevelsScreen.java
