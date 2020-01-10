package tictactoe.client.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import tictactoe.client.App;

/**
 *
 * @author KeR
 */
public class MainScreen extends Pane {

    public MainScreen(App app) {
        ToggleButton Acccept = new ToggleButton("play with computer");
        Acccept.setPrefSize(250, 50);
        Acccept.setId("compButton");//back
        ToggleButton Decline = new ToggleButton("play with other player");
        Decline.setPrefSize(250, 50);
        Decline.setId("playerButton");//playAgain
        HBox buttonBox = new HBox(20, Acccept, Decline);
        buttonBox.setAlignment(Pos.CENTER_LEFT);
        buttonBox.setLayoutX(40);
        buttonBox.setLayoutY(350);
        GridPane gr = new GridPane();
        gr.setId("MainScreenGridPane");

        for (int i = 0; i < 10; i++) {
            ToggleButton invite2 = new ToggleButton("Challenge");
            invite2.setId("challengeScrolPaneMainScreen");
            Label score2 = new Label("500");
            score2.setId("scoreLabelGridPane");
            Label imglabel2 = new Label();
            Image img2 = new Image(getClass().getResourceAsStream("/images/k.png"));
            imglabel2.setGraphic(new ImageView(img2));
            Circle cir2 = new Circle(150.0f, 150.0f, 5.f);
            cir2.setFill(Color.GREEN);
            gr.add(cir2, 0, i);
            gr.add(invite2, 3, i);
            gr.add(score2, 2, i);
            gr.add(imglabel2, 1, i);
        }

        gr.setPrefSize(495.2, 200.0);
        Button send = new Button();
        send.setText("send");
        send.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
            }
        });
        send.setId("sendChatMainScreen");
        send.setLayoutX(1060);
        send.setLayoutY(600);
        gr.setStyle("-fx-background-color: rgba(0, 0, 0, .8); -fx-background-radius: 10;");
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setId("scrollPaneMainScreen");
        scrollPane.setContent(gr);
        scrollPane.pannableProperty().set(true);

        scrollPane.fitToWidthProperty().set(true);

        scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        scrollPane.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);

        HBox hBox1 = new HBox();
        hBox1.getChildren().add(scrollPane);
        hBox1.setAlignment(Pos.TOP_RIGHT);
        VBox v = new VBox();
        v.getChildren().add(hBox1);
        v.setLayoutX(850);
        v.setLayoutY(0);
        TextArea ta = new TextArea("sasas");
        ta.setLayoutX(730);
        ta.setLayoutY(400);
        ta.setMaxWidth(220.0);
        ta.setMaxHeight(150.0);
        TextArea text = new TextArea("dhgdjdkd");
        text.setLayoutX(730);
        text.setLayoutY(600);
        text.setMaxWidth(220.0);
        text.setMaxHeight(10.5);
        getChildren().addAll(buttonBox, text, ta, send, v);
        setId("MainScreenPane");

    }
}
