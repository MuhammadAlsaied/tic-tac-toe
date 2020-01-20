package tictactoe.server;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import tictactoe.server.models.Player;

public class ServerMain extends Application {

    private Server server = null;
    private int playersListCounter;
    private GridPane gridPane;
    private ServerMain app = this;
    private TextArea textArea;

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                Platform.exit();
                System.exit(0);
            }
        });

        primaryStage.setTitle("Server Main");
        ToggleButton toggleButton = new ToggleButton("ON");
        toggleButton.setId("toggleButton");
        toggleButton.setOnAction((ActionEvent event) -> {
            if (server == null) {
                guiLog("turning on the server");
                server = new Server(app);
                server.start();
                toggleButton.setText("OFF");
            } else {
                guiLog("turning off the server");
                server.turnOff();
                server.stop();
                server = null;
                toggleButton.setText("ON");
            }
        });
        HBox hbox = new HBox(toggleButton);
        hbox.setLayoutX(275);
        hbox.setLayoutY(150);

        textArea = new TextArea("");
        textArea.setId("textArea");
        textArea.setLayoutX(50);
        textArea.setLayoutY(300);
        textArea.setMaxWidth(575.0);
        textArea.setMaxHeight(300.0);
        textArea.setEditable(false);
        textArea.setId("text-area");
        gridPane = new GridPane();
        gridPane.setHgap(50);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(gridPane);
        scrollPane.setId("scrolPane");

        scrollPane.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setLayoutX(650.0);
        scrollPane.setLayoutY(100.0);
        scrollPane.setMaxHeight(500.0);

        Pane root = new Pane();
        root.setId("stack");
        root.getChildren().addAll(hbox, scrollPane, textArea);
        Scene scene = new Scene(root, 1350, 700);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toString());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void clearPlayersListPane() {
        gridPane.getChildren().clear();
    }

    public void addPlayersToList(JsonArray playerList, Color color) {
        for (int i = 0; i < playerList.size(); i++) {
            JsonObject jsonPlayer = playerList.get(i).getAsJsonObject();
            Player player = new Player();
            player.setFirstName(jsonPlayer.get("firstName").getAsString());
            player.setPoints(jsonPlayer.get("points").getAsInt());
            player.setId(jsonPlayer.get("id").getAsInt());
            Label score2 = new Label(Integer.toString(player.getPoints()));
            score2.setId("scoreLabel");
            Label playerName = new Label(player.getFirstName());
            Circle cir2 = new Circle(150.0f, 150.0f, 5.f);
            cir2.setFill(color);
            gridPane.add(cir2, 0, playersListCounter);
            gridPane.add(score2, 2, playersListCounter);
            gridPane.add(playerName, 1, playersListCounter);
            playersListCounter++;

        }
    }

    public void setPlayersListCounter(int playersListCounter) {
        this.playersListCounter = playersListCounter;
    }

    public void guiLog(String text) {
        textArea.appendText(text + "\n");
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
