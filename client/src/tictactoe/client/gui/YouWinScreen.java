package tictactoe.client.gui;

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
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import tictactoe.client.App;

/**
 *
 * @author KeR
 */
public class YouWinScreen extends StackPane {
    private final App app;
    
    public YouWinScreen(App app) {
        this.app = app;
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

        Button lose = new Button("You Are The Winner");
        lose.setId("lose");
        lose.setEffect(e);
        //=========Back button===============
        ToggleButton back = new ToggleButton("Back");
        back.setPrefSize(180, 20);
        back.setId("back");
        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                app.setScreen("levels");
            }
        });
        //=========Play again button===========
        ToggleButton playAgain = new ToggleButton("Play Again");
        playAgain.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                app.setScreen("playWithComputerEasyGameBoard");
            }
        });
        playAgain.setPrefSize(180, 20);
        playAgain.setId("playAgain");
        HBox buttonBox = new HBox(50,back,playAgain);
        

        VBox vbox = new VBox(30, over, lose,buttonBox);
                vbox.setId("vbox");


        getChildren().addAll(rec, vbox);
        setId("stackGameResultScreen");
    }
}
