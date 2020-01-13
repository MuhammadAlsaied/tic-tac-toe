package tictactoe.client.gui;

import com.google.gson.JsonObject;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import sun.security.util.Password;
import tictactoe.client.App;

/**
 *
 * @author hp
 */
public class SigninScreen extends StackPane {

    private final App app;

    public SigninScreen(App app) {
        this.app = app;

        Label header = new Label("Sign In");
        header.setId("siginLabel");

        TextField email = new TextField();
        email.setPromptText(" Enter your Email");
        email.setId("email");
        email.setPrefSize(324, 36);
        email.setFocusTraversable(false);

        PasswordField password = new PasswordField();
        password.setPrefSize(324, 36);
        password.setFocusTraversable(false);

        password.setPromptText(" Enter your password");
        //        ==================SIGN UP BUTTON AND EVENT HANDLER===============
        Button signin = new Button("SIGN IN");
        signin.setId("signin");

        signin.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
                if (!email.getText().matches(regex)) {
                    Alert emailValid = new Alert(Alert.AlertType.INFORMATION);
                    emailValid.setTitle("Email Validation");
                    emailValid.setHeaderText("");
                    emailValid.setContentText("Email Must Be User@email.com!");
                    emailValid.show();
                }
                if (password.getText().isEmpty()) {
                    Alert a = new Alert(Alert.AlertType.INFORMATION);
                    a.setTitle("Password Validation");
                    a.setHeaderText("");
                    a.setContentText("Password can't be empty!");
                    a.show();
                } else {
                    signin.setText("Connecting...");
                    signin.setDisable(true);
                    JsonObject jsonObject = new JsonObject();
                    JsonObject data = new JsonObject();
                    data.addProperty("email", email.getText());
                    data.addProperty("password", password.getText());
                    jsonObject.addProperty("type", "signin");
                    jsonObject.add("data", data);
                    System.out.println(jsonObject);
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

        signin.setPrefSize(153, 47);

        Label newUser = new Label("New user?");
        newUser.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                app.setScreen("signup");
            }
        });
        newUser.setCursor(Cursor.HAND);
        
        
        //TO MAKE SHADOW
        DropShadow e = new DropShadow();
        e.setOffsetX(0.0f);
        e.setOffsetY(4.0f);
        e.setBlurType(BlurType.GAUSSIAN);
        email.setEffect(e);
        password.setEffect(e);
        signin.setEffect(e);

        e.setColor(javafx.scene.paint.Color.BLACK);

        VBox vbox = new VBox(20, header, email, password, signin, newUser);
        vbox.setId("vbox");
        Region rec = new Region();

        region(rec);
        getChildren().addAll(rec, vbox);
        setId("stackSignin");
    }

    public void region(Region rec) {
        rec.setPrefSize(498, 460);
        rec.setId("recSignin");
    }

}
