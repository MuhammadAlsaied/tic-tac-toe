package tictactoe.client.gui;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import tictactoe.client.App;

/**
 *
 * @author muhammad
 */
public class SignupScreen extends StackPane {

    private final App app;

    public SignupScreen(App app) {
        this.app = app;
        init();
    }

    private void init() {
        TextField name = new TextField();
        name.setPromptText("Please Enter your Name");
        name.setFocusTraversable(false);
        name.setId("name");
        name.setPrefWidth(300);
        TextField nickName = new TextField();
        nickName.setPromptText("Please Enter your Nickname");
        nickName.setId("name");
        TextField password = new TextField();
        password.setPromptText("Please Enter your Password");
        password.setId("name");
        TextField repassword = new TextField();
        repassword.setPromptText("Please ReEnter your Password");
        repassword.setId("name");
        TextField email = new TextField();
        email.setPromptText("Please Enter your Email");
        email.setId("name");
        password.setFocusTraversable(false);
        nickName.setFocusTraversable(false);
        email.setFocusTraversable(false);
        repassword.setFocusTraversable(false);
        Image image = new Image(getClass().getResourceAsStream("/images/xx.png"));
        Label label1 = new Label();
        label1.setGraphic(new ImageView(image));
        ToggleButton signup = new ToggleButton("SIGN UP");
        signup.setId("signup");
        Region rec = new Region();
        rec.prefHeight(600);
        rec.prefWidth(450);
        rec.setId("rec");
        VBox box1 = new VBox(30, repassword, signup);
        VBox box = new VBox(20, label1, name, nickName, email, password, box1);
        box.setId("vbox");
        box1.setId("vbox");
        StackPane root = new StackPane();
        setId("stack");
        getChildren().addAll(rec, box);

    }

}
