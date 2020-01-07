package tictactoe.client.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.control.TextField;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;

import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author hp
 */
public class Signin extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("TIC TAC TOE!");

        Label header = new Label("Sign In");
        header.setId("siginLabel");

        TextField email = new TextField();
        email.setPromptText(" Enter your Email");
        email.setId("email");
        email.setPrefSize(324, 36);
        email.setFocusTraversable(false);

        TextField password = new TextField();
        password.setPrefSize(324, 36);
        password.setFocusTraversable(false);

        password.setPromptText(" Enter your password");
        Button signin = new Button("SIGN IN");
        signin.setId("signin");
        signin.setPrefSize(153, 47);

        //TO MAKE SHADOW
        DropShadow e = new DropShadow();
        e.setOffsetX(0.0f);
        e.setOffsetY(4.0f);
        e.setBlurType(BlurType.GAUSSIAN);
        email.setEffect(e);
        password.setEffect(e);
        signin.setEffect(e);

        e.setColor(javafx.scene.paint.Color.BLACK);

        VBox vbox = new VBox(20, header, email, password, signin);
        vbox.setId("vbox");
        Region rec = new Region();

        region(rec);
        StackPane stack = new StackPane();
        stack.getChildren().addAll(rec, vbox);
        stack.setId("stackSignin");

        // Create a scene with registration form grid pane as the root node
        Scene scene = new Scene(stack, 700, 500);

        scene.getStylesheets().add(getClass().getResource("/css/style.css").toString());
        // Set the scene in primary stage	
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    public void region(Region rec) {
        rec.setPrefSize(498, 460);
        rec.setId("recSignin");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
