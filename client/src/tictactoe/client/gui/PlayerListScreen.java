/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe.client.gui;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import tictactoe.client.App;


public class PlayerListScreen extends Pane {
    
    
    public PlayerListScreen(App app) 
    {  
        GridPane gr = new GridPane();
        gr.setHgap(50);
        Region rectBack = new Region();
        rectBack.setPrefSize(498, 460);
        rectBack.setId("rectBack");
        rectBack.setLayoutX(400.0);
        rectBack.setLayoutY(150.0);
        rectBack.setMaxHeight(450.0);
        for(int i=0 ; i<9 ; i++)
        {
            ToggleButton invite2 = new ToggleButton("Challenge");
            invite2.setId("ChallengePlayerList");
            Label score2 = new Label("500");
            score2.setId("scoreLabel");
            Label imglabel2 = new Label();
            Image img2 = new Image(getClass().getResourceAsStream("/images/k.png"));
            imglabel2.setGraphic(new ImageView(img2));
            Circle cir2 = new Circle(150.0f, 150.0f, 5.f);
            cir2.setFill(Color.GREEN);
            gr.add(cir2,0,i);
            gr.add(invite2, 3, i);
            gr.add(score2, 2, i);
            gr.add(imglabel2, 1, i);
        }

        ScrollPane scrollPane = new ScrollPane(gr);
        scrollPane.setId("scrolPanePlaylistScreen");
        scrollPane.setFocusTraversable(false);
        scrollPane.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setLayoutX(450.0);
        scrollPane.setLayoutY(150.0);
        scrollPane.setMaxHeight(450.0);
        
        setId("stack");
        getChildren().add(rectBack);
        getChildren().add(scrollPane);

        
    }
}
