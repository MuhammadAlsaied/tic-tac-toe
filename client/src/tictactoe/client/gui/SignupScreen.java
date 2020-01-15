package tictactoe.client.gui;

import com.google.gson.JsonObject;
import java.io.IOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private ToggleButton signup;

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
        PasswordField password = new PasswordField();
        password.setPromptText("Please Enter your Password");
        password.setId("name");
        PasswordField repassword = new PasswordField();
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

//        ==================SIGN UP BUTTON AND EVENT HANDLER===============
        signup = new ToggleButton("SIGN UP");
        signup.setId("signup");
        signup.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
                if (!email.getText().matches(regex)) {
                    Alert emailValid = new Alert(Alert.AlertType.INFORMATION);
                    emailValid.setTitle("Email Validation");
                    emailValid.setHeaderText("");
                    emailValid.setContentText("Email Must Be User@email.com!");
                    emailValid.show();
                    showSignupButton();
                }
                if (password.getText().isEmpty()) {
                    Alert a = new Alert(Alert.AlertType.INFORMATION);
                    a.setTitle("Password Validation");
                    a.setHeaderText("");
                    a.setContentText("Password can't be empty!");
                    a.show();
                    showSignupButton();
                } else if (!password.getText().equals(repassword.getText())) {
                    Alert a = new Alert(Alert.AlertType.INFORMATION);
                    a.setTitle("Password Validation");
                    a.setHeaderText("");
                    a.setContentText("Passwords don't match");
                    a.show();
                    showSignupButton();
                } else if (password.getText().equals(repassword.getText())) {
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
        });
//        =================================================================
        Region rec = new Region();
        rec.prefHeight(600);
        rec.prefWidth(450);
        rec.setId("rec");
        Label alreadyRegistered = new Label("Already Registered?");
        alreadyRegistered.setId("alreadyRegisteredLabel");
        alreadyRegistered.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                app.setScreen("signin");
            }
        });
        alreadyRegistered.setCursor(Cursor.HAND);
        VBox box1 = new VBox(25, repassword, signup);
        VBox box = new VBox(18, label1, name, nickName, email, password, box1, alreadyRegistered);
        box.setId("vbox");
        box1.setId("vbox");
        setId("stack");
        getChildren().addAll(rec, box);

        
    }
//        =====================Signup methods==============================
        public void showSignupFailedPopup(){
            app.showAlert("Signup failed", "This email is already registered, please enter another email.");
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    signup.setText("SIGN UP");
                    signup.setDisable(false);
                }
            });
        }
        
        public void showSignupButton(){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    signup.setText("SIGN UP");
                    signup.setDisable(false);
                }
            });
        }
//        =================================================================

}
