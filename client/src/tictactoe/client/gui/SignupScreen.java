package tictactoe.client.gui;

import com.google.gson.JsonObject;
import java.io.IOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
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
    private Label error;
    private ToggleButton signup;
    private TextField name, nickName, email;
    private PasswordField password, repassword;
    private String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

    public SignupScreen(App app) {
        this.app = app;
        init();
    }

    private void init() {
        name = new TextField();
        error = new Label();
        name.setPromptText("Please Enter your Name");
        name.setFocusTraversable(false);
        name.setId("name");
        name.setPrefWidth(300);
        nickName = new TextField();
        nickName.setPromptText("Please Enter your Nickname");
        nickName.setId("name");
        password = new PasswordField();
        password.setPromptText("Please Enter your Password");
        password.setId("name");
        repassword = new PasswordField();
        repassword.setPromptText("Please ReEnter your Password");
        repassword.setId("name");
        email = new TextField();
        email.setPromptText("Please Enter your Email");
        email.setId("name");
        password.setFocusTraversable(false);
        nickName.setFocusTraversable(false);
        email.setFocusTraversable(false);
        repassword.setFocusTraversable(false);
        Image image = new Image(getClass().getResourceAsStream("/images/xx.png"));
        Label label1 = new Label();
        label1.setGraphic(new ImageView(image));

//        ==================SIGN UP BUTTON AND EVENT HANDLER===============
        signup = new ToggleButton("SIGN UP");
        signup.setId("signup");
        signup.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                signupEvent();
            }
        });

        setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    signupEvent();
                }
            }
        });

        setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.TAB) {
                    
                }
            }
        });
//        ============Email And Repassowrd Event Handeler==================
        email.setOnKeyReleased(ke -> {
            if (!email.getText().matches(regex)) {
                error("Email Must be something@somthing.somthing", email);
            } else if (email.getText().matches(regex)) {
                errorRelese(email);
            }
        });
        email.setOnKeyTyped(ke -> {
            if (!email.getText().matches(regex)) {
                error("Email Must be something@somthing.somthing", email);
            } else if (email.getText().matches(regex)) {
                errorRelese(email);
            }
        });
        repassword.setOnKeyReleased(ke -> {
            if (!password.getText().equals(repassword.getText())) {
                error("The Two Feilds are not Matched", repassword);
            } else if (password.getText().equals(repassword.getText())) {
                errorRelese(repassword);
            }
        });
        repassword.setOnKeyTyped(ke -> {
            if (!password.getText().equals(repassword.getText())) {
                error("The Two Feilds are not Matched", repassword);
            } else if (password.getText().equals(repassword.getText())) {
                errorRelese(repassword);
            }
        });

//        =================================================================
        Region rec = new Region();
        rec.prefHeight(600);
        rec.prefWidth(450);
        rec.setId("recSignin");
        Label alreadyRegistered = new Label("Already Registered?");
        alreadyRegistered.setId("alreadyRegisteredLabel");
        alreadyRegistered.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                app.setScreen("signin");
            }
        });
        alreadyRegistered.setCursor(Cursor.HAND);
        VBox box1 = new VBox(18, repassword, signup);
        box1.setStyle("-fx-border-width: 0px;");
        VBox box = new VBox(14, label1, name, nickName, email, password, box1, alreadyRegistered, error);
        box.setStyle("-fx-border-width: 0px;");
        box.setId("vbox");
        box1.setId("vbox");
        setId("stackSignin");
        getChildren().addAll(rec, box);

    }
//        =====================Signup methods==============================

    public void signupEvent() {

        if (nickName.getText().isEmpty()) {
            nickName.setPromptText("You Cannot Leave Name Empty");
            nickName.setStyle("-fx-font-size: 16px; -fx-alignment: CENTER");
            nickName.setPrefSize(324, 45);
        }
        if (name.getText().isEmpty()) {
            name.setPromptText("You Cannot Leave Name Empty");
            name.setStyle("-fx-font-size: 16px; -fx-alignment: CENTER");
            name.setPrefSize(324, 45);
        }
        if (email.getText().isEmpty()) {
            email.setPromptText("You Cannot Leave E-Mail Empty");
            email.setStyle("-fx-font-size: 16px; -fx-alignment: CENTER");
            email.setPrefSize(324, 45);
        }
        if (!email.getText().matches(regex) && !email.getText().isEmpty()) {
            error("Email Must be something@somthing.somthing", email);
        }
        if (password.getText().isEmpty()) {
            password.setPromptText("You Cannot Leave Password Empty");
            password.setStyle("-fx-font-size: 16px;");
            password.setPrefSize(324, 45);
            showSignupButton();
        }
        if (!password.getText().equals(repassword.getText())) {
            error("The Two Feilds are not Matching", repassword);
        } else if (password.getText().equals(repassword.getText())) {
            errorRelese(repassword);
        }
        if (repassword.getText().isEmpty()) {
            repassword.setPromptText("You Must Retype Your Password");
            repassword.setStyle("-fx-font-size: 16px;");
            repassword.setPrefSize(324, 45);
        }
        if (password.getText().equals(repassword.getText()) && !nickName.getText().isEmpty()
                && !name.getText().isEmpty() && !email.getText().isEmpty() && !password.getText().isEmpty()
                && !repassword.getText().isEmpty() && email.getText().matches(regex)) {
            signup.setText("Loading...");
            signup.setDisable(true);
            JsonObject jsonObject = new JsonObject();
            JsonObject data = new JsonObject();
            data.addProperty("firstName", name.getText());
            data.addProperty("lastName", nickName.getText());
            data.addProperty("email", email.getText());
            data.addProperty("password", password.getText());
            jsonObject.addProperty("type", "signup");
            jsonObject.add("data", data);
            try {
                System.out.println(jsonObject);

                app.getDataOutputStream().writeUTF(jsonObject.toString());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void showSignupFailedPopup() {
        app.showAlert("Signup failed", "This email is already registered, please enter another email.");
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                signup.setText("SIGN UP");
                signup.setDisable(false);
            }
        });
    }

    public void showSignupButton() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                signup.setText("SIGN UP");
                signup.setDisable(false);
            }
        });
    }

    private void error(String errorMessage, TextField node) {
        node.setStyle("-fx-border-color: RED; -fx-alignment: CENTER; -fx-border-width: 3px;");
        error.setText(errorMessage);
        error.setStyle("-fx-text-fill: RED; -fx-font-size: 16px;");
    }

    private void errorRelese(TextField node) {
        node.setStyle("-fx-border-color: WHITE; -fx-alignment: CENTER; -fx-border-width: 3px;");
        error.setText("");
    }
//        =================================================================

}
