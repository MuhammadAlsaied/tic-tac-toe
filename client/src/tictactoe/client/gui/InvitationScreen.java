package tictactoe.client.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import tictactoe.client.App;

/**
 *
 * @author KeR
 */
public class InvitationScreen extends StackPane {

 
    public  InvitationScreen(App app) {

        Region rec = new Region();
        rec.setPrefSize(498, 460);
        rec.setId("regionInvitationScreen");



        DropShadow e = new DropShadow();
        e.setOffsetX(0.0f);
        e.setOffsetY(4.0f);
        e.setBlurType(BlurType.GAUSSIAN);
        e.setColor(Color.BLACK);
        String str = new String("Kareem");

        Button lose = new Button(str + " Challenges you");

        lose.setId("playerWantsToChallengeYou");
        lose.setEffect(e);
        ToggleButton accept = new ToggleButton("Acccept");
        accept.setPrefSize(200, 20);
        accept.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MultiOnlinePlayers.turn = false;
                app.setScreen("multiOnlinePlayers");
            }
        });
        accept.setId("acceptInvitation");
        ToggleButton Decline = new ToggleButton("Decline");
        Decline.setPrefSize(200, 20);
        Decline.setId("declineInvitation");
        HBox buttonBox = new HBox(20, accept, Decline);

        VBox vbox = new VBox(100, lose, buttonBox);
        vbox.setId("vboxInvitationScreen");

        getChildren().addAll(rec, vbox);
        setId("stackInvitation");
    }
}
