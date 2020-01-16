package tictactoe.server.models;

import com.google.gson.JsonObject;

/**
 *
 * @author muhammad and Ayman Magdy
 */
public class Game {

    private Player playerX;
    private Player playerO;
    private int winnerId;
    private JsonObject gameCoordinates;
    private Status gameStatus;
    private Move nextMove;

    private int gameId;
    
    
    public Game(Player player1, Player player2){
        this.playerX = player1;
        this.playerO = player2;
        nextMove = Move.X;
        gameStatus = Status.inProgress;
        this.gameCoordinates = new JsonObject();
        gameCoordinates.addProperty("upper_left", "-");
        gameCoordinates.addProperty("up", "-");
        gameCoordinates.addProperty("upper_right", "-");
        gameCoordinates.addProperty("left", "-");
        gameCoordinates.addProperty("center", "-");
        gameCoordinates.addProperty("right", "-");
        gameCoordinates.addProperty("lower_left", "-");
        gameCoordinates.addProperty("lower", "-");
        gameCoordinates.addProperty("lower_right", "-");
    }

    public void setNextMove(Position key, Move value) {
        if (gameCoordinates.get(key.toString()).toString().equals("-")) {
            gameCoordinates.addProperty(key.toString(), value.toString());
            if (value.equals(Move.O)) {
                nextMove = Move.X;
            } else {
                nextMove = Move.O;
            }
        }
    }

    public void setPlayerX(Player playerX) {
        this.playerX = playerX;
    }

    public Player getPlayerX() {
        return playerX;
    }

    public void setPlayerO(Player playerO) {
        this.playerO = playerO;
    }

    public Player getPlayerO() {
        return playerO;
    }

    public void setWinnerId(int winnerId) {
        this.winnerId = winnerId;
    }

    public int getWinnerId() {
        return winnerId;
    }

    public String getStatus() {
        return gameStatus.toString();
    }

    public Status getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(Status gameStatus) {
        this.gameStatus = gameStatus;
    }

    public JsonObject getGameCoordinates() {
        return gameCoordinates;
    }

    public void setGameCoordinates(JsonObject gameCoordinates) {
        this.gameCoordinates = gameCoordinates;
    }

    public String getCoordinates() {
        return gameCoordinates.toString();
    }


    public static enum Status {
        inProgress, terminated, finished;
    }

    public static enum Position {
        upper_left, up, upper_right, left, center, right, lower_left, lower_right, down
    }

    public static enum Move {
        X, O
    }
    
    public void setGameId(int gameId){
        this.gameId = gameId;
    }
    
    public int getGameId(){
        return gameId;
    }
    
    public int getPlayerXId(){
        return playerX.getId();
    }
    
    public int getPlayerOId(){
        return playerO.getId();
   }
}
