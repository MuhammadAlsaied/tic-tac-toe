package tictactoe.client.gui;

import java.util.Vector;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import tictactoe.client.App;

/**
 *
 * @author KeR
 */
public class MultiPlayerGameBoardScreen extends GridPane {

    private App app;
    private boolean flag = true;    //true is X false is Y
    private boolean turn;       //this to check if it's the player's turn or not
    private int counter;
    private String line;
    private boolean[] textLabelflag;
    private Vector<Label> tiles = new Vector<>();
    public  MultiPlayerGameBoardScreen(App app) {
        setId("stackGameboard");
        setPadding(new Insets(40, 0, 0, 50));
        setHgap(150);
        setVgap(-20);
        setPrefSize(700, 700);
        for (int i = 0; i < 9; i++) {
            tiles.add(new Label("_"));
        }
        counter = 0;
        resetGame();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int x = i * 3 + j;
                tiles.get(x).setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {

                        if (flag && textLabelflag[x]) {
                            tiles.get(x).setText("X");
                            tiles.get(x).setId("x");
                            flag = false;
                            textLabelflag[x] = false;
                            counter++;
                        } 
//                          else if (flag == false && textLabelflag[x]) {
//                            tiles.get(x).setText("O");
//                            tiles.get(x).setId("o");
//                            flag = true;
//                            textLabelflag[x] = false;
//                            counter++;
//                        }
                        //checkWinner();
                    }
                });
                add(tiles.get(x), j, i);
            }
        }
    }

    public void resetGame() {
        for (int i = 0; i < 9; i++) {
            tiles.get(i).setText("_");
        }
        textLabelflag = new boolean[]{true, true, true, true, true, true, true, true, true};
        for (int i = 0; i < tiles.size(); i++) {
            tiles.get(i).setId("label");
        }
    }
    
    public void setTile(String side, int pos){
        tiles.get(pos).setText(side.toUpperCase());
        tiles.get(pos).setId(side.toLowerCase());
        textLabelflag[pos] = false; 
    }
    
    public void setPlayerNames(String side){
        
    }
    
    public void setPlayerSide(char side){
        
    }
    
    public void setPlayerTurn(boolean turn){
        
    }

    public void showResult(String res){
        if(res.equalsIgnoreCase("won"))
            app.setScreen("youWin");
        else if(res.equalsIgnoreCase("lost"))
            app.setScreen("hardLuck");
    }
    
    /* private void checkWinner() {
        for (int x = 0; x < 8; x++) {
            line = null;
            switch (x) {
                case 0:
                    line = tiles.get(0).getText() + tiles.get(1).getText() + tiles.get(2).getText();
                    break;
                case 1:
                    line = tiles.get(3).getText() + tiles.get(4).getText() + tiles.get(5).getText();
                    break;
                case 2:
                    line = tiles.get(6).getText() + tiles.get(7).getText() + tiles.get(8).getText();
                    break;
                case 3:

                    line = tiles.get(0).getText() + tiles.get(3).getText() + tiles.get(6).getText();
                    break;
                case 4:
                    line = tiles.get(1).getText() + tiles.get(4).getText() + tiles.get(7).getText();
                    break;
                case 5:
                    line = tiles.get(2).getText() + tiles.get(5).getText() + tiles.get(8).getText();
                    break;
                case 6:
                    line = tiles.get(0).getText() + tiles.get(4).getText() + tiles.get(8).getText();
                    break;
                case 7:
                    line = tiles.get(2).getText() + tiles.get(4).getText() + tiles.get(6).getText();
                    break;
            }
            switch (line) {
                case "XXX": {
                    Alert a = new Alert(Alert.AlertType.INFORMATION);
                    a.setTitle("the winner");
                    a.setHeaderText("");
                    a.setContentText("X is the winner");
                    a.show();
                    counter = 0;
                    resetGame();
                    break;
                }
                case "OOO": {
                    Alert a = new Alert(Alert.AlertType.INFORMATION);
                    a.setTitle("O is the winner");
                    a.setHeaderText("");
                    a.setContentText("O is the winner");
                    a.show();
                    counter = 0;
                    resetGame();
                    break;
                }
            }
        }
        if (counter == 9) {
            counter = 0;
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Noone Win");
            a.setHeaderText("");
            a.setContentText("Draw! No winner :/");
            a.show();
            resetGame();
        }
    }*/

}
