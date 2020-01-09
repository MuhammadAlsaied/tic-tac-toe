package tictactoe.client.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.control.TextField;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;

import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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
        
        TextField password = new TextField();
        password.setPrefSize(324, 36);
        password.setFocusTraversable(false);
        
        password.setPromptText(" Enter your password");
        Button signin = new Button("SIGN IN");
        signin.setId("signin");
        signin.setPrefSize(153, 47);
        
        Button newUser = new Button("New user?");
        newUser.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                app.setScreen("signup");
            }
        });
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
