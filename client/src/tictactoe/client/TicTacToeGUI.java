package tictactoe.client;

import javafx.application.Application;

import javafx.scene.Scene;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author KeR
 */
public class TicTacToeGUI extends Application {
    
    @Override
    public void start(Stage primaryStage) {
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
        label1.setId("label");
        label1.setGraphic(new ImageView(image));
        ToggleButton signup = new ToggleButton("SIGN UP");
        signup.setId("signup");
        Region rec = new Region();
        rec.prefHeight(600);
        rec.prefWidth(450);
        rec.setId("rec");
        VBox box1 = new VBox(30,repassword,signup);
        VBox box = new VBox(20,label1,name,nickName,email,password,box1);
        box.setId("vbox");
        box1.setId("vbox");
        StackPane root = new StackPane();
        root.setId("stack");
        root.getChildren().addAll(rec,box);
        Scene scene = new Scene(root, 1350, 700);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toString());
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setTitle("TIC TAC TOE!");
    }

    @Override
    public void init() throws Exception {
        super.init();
        
    }

    /**
     * @param args the command line arguments
     */
  
    
}
