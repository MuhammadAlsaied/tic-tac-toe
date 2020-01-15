package tictactoe.server.db;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.sql.*;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import tictactoe.server.Config;
import tictactoe.server.models.Enums;
import tictactoe.server.models.Enums.Status;
import tictactoe.server.models.Game;
import tictactoe.server.models.Player;

/**
 *
 * @author Ayman Magdy
 */
public class DatabaseManager {

    private Connection connection;
    private Statement statment;
    private ResultSet resultSet;
    private Vector<Player> usersVecDetails = new Vector();

    public DatabaseManager() throws ClassNotFoundException, SQLException {
        // Here to establish the connecection once creating an instance.
        establishConnection();
    }

    private void establishConnection() throws ClassNotFoundException {
        try {
            // to start the connection;
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/"
                    + Config.DB_NAME, Config.DB_USERNAME, Config.DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Done and tested.
    public Player signUp(String first_name, String last_name, String email, String password) throws SQLException, ClassNotFoundException {
        Player newPlayer = null;

        if (isEmailExists(email)) {
            System.out.println("Your email is already registered..");
        } else {
            establishConnection();

            PreparedStatement preparedStatment = connection.prepareStatement("INSERT INTO player (first_name, last_name, email, password) VALUES (?, ?, ?, ?);");
            preparedStatment.setString(1, first_name);
            preparedStatment.setString(2, last_name);
            preparedStatment.setString(3, email);
            preparedStatment.setString(4, password);
            preparedStatment.executeUpdate();

            newPlayer = getLastPlayer();

            preparedStatment.close();
            connection.close();
        }
        return newPlayer;
    }

    // Done and tested
    public Player getLastPlayer() {
        Player lastPlayer = new Player();

        try {
            establishConnection();
            statment = connection.createStatement();
            resultSet = statment.executeQuery("SELECT * FROM player ORDER BY id DESC LIMIT 0, 1");

            if (resultSet.first()) {
                int newUserId = Integer.parseInt(resultSet.getString("id"));
                lastPlayer.setId(newUserId);
                lastPlayer.setFirstName(resultSet.getString("first_name"));
                lastPlayer.setLastName(resultSet.getString("last_name"));
                lastPlayer.setPoints(0);
                lastPlayer.setEmail(resultSet.getString("email"));
                lastPlayer.setCurrentGame(null);
            } else {
                lastPlayer = null;
                throw new SQLException("Getting the last user failed");
            }

            statment.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lastPlayer;
    }

    //chcek if the email exists in the DB, done and tested;
    public boolean isEmailExists(String email) throws SQLException {

        try {
            establishConnection();
            statment = connection.createStatement();
            resultSet = statment.executeQuery("SELECT email FROM player WHERE email='" + email + "';");

            // The email is in the DB.
            if (resultSet.first() == true) {
                statment.close();
                connection.close();
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        statment.close();
        connection.close();
        return false;
    }

    // Done and tested. return player with data
    public Player signIn(String email, String password) {
        Player playerSignIn = null;
        if (email != null && password != null) {
            try {
                establishConnection();
                statment = connection.createStatement();
                resultSet = statment.executeQuery("SELECT * FROM player WHERE email='" + email + "' AND password='" + password + "';");

                if (resultSet.first() == true) {
                    System.out.println("Login successed..");
                    playerSignIn = new Player();
                    playerSignIn.setId(resultSet.getInt("id"));
                    playerSignIn.setFirstName(resultSet.getString("first_name"));
                    playerSignIn.setLastName(resultSet.getString("last_name"));
                    playerSignIn.setEmail(resultSet.getString("email"));
                    playerSignIn.setImg(resultSet.getString("image"));
                    playerSignIn.setPoints(resultSet.getInt("points"));
                    statment.close();
                    connection.close();
                    return playerSignIn;
                } else {
                    statment.close();
                    connection.close();
                    System.out.println("Login failed.");
                    return playerSignIn;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            System.out.println("Empty fields..");
            return playerSignIn;
        }
        return playerSignIn;
    }

    // Done and tested
    public boolean insertGame(Game gameToSave) throws ClassNotFoundException {
        boolean success = false;
        Integer playerXId = gameToSave.getPlayerX().getId();
        Integer playerOId = gameToSave.getPlayerO().getId();
        String gameStatus = gameToSave.getStatus();
        String coordinatesToSave = gameToSave.getCoordinates().toString();

        try {
            establishConnection();

            PreparedStatement preparedStatment = connection.prepareStatement("INSERT INTO game (player1_id, player2_id, session_status, coordinates) VALUES (?, ?, ?, ?);");
            preparedStatment.setString(1, playerXId.toString());
            preparedStatment.setString(2, playerOId.toString());
            preparedStatment.setString(3, gameStatus);
            preparedStatment.setString(4, coordinatesToSave);
            preparedStatment.executeUpdate();

            preparedStatment.close();
            connection.close();
            success = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return success;
    }

    // Done and tested
    public ArrayList<Player> getAllPlayers() throws SQLException {
        ArrayList<Player> allPlayers = new ArrayList<>();

        try {
            establishConnection();
            statment = connection.createStatement();
            resultSet = statment.executeQuery("SELECT * FROM player;");

            while (resultSet.next()) {
                Player tempPlayer = new Player();

                tempPlayer.setId(resultSet.getInt("id"));
                tempPlayer.setFirstName(resultSet.getString("first_name"));
                tempPlayer.setLastName(resultSet.getString("last_name"));
                tempPlayer.setEmail(resultSet.getString("email"));
                tempPlayer.setImg(resultSet.getString("image"));
                tempPlayer.setPoints(resultSet.getInt("points"));

                allPlayers.add(tempPlayer);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        statment.close();
        connection.close();

        return allPlayers;
    }

    // Done and tested
    public Player updatePlayerScore(int id, int additionalPoints) throws ClassNotFoundException {
        Player updatedPlayer = new Player();
        PreparedStatement pst;
        int totalPoints = 0;

        try {
            establishConnection();
            statment = connection.createStatement();
            resultSet = statment.executeQuery("SELECT * FROM player WHERE id=" + id + ";");
            if (resultSet.next()) {
                updatedPlayer.setId(resultSet.getInt("id"));
                updatedPlayer.setFirstName(resultSet.getString("first_name"));
                updatedPlayer.setLastName(resultSet.getString("last_name"));
                updatedPlayer.setEmail(resultSet.getString("email"));
                totalPoints = resultSet.getInt("points");
            }

            resultSet.close();
            statment.close();
            totalPoints += additionalPoints;
            updatedPlayer.setPoints(totalPoints);

            pst = connection.prepareStatement("UPDATE player SET points=? WHERE id=?");
            pst.setInt(1, totalPoints);
            pst.setInt(2, id);

            pst.executeUpdate();
            pst.close();
            connection.close();

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return updatedPlayer;
    }

    // Done and tested
    public Game getTerminatedGame(int firstPlayerId, int secondPlayerId) throws SQLException {
        Game terminatedGame = null;

        try {
            establishConnection();
            statment = connection.createStatement();
            resultSet = statment.executeQuery("select * from game where player1_id ='" + firstPlayerId + "' and player2_id ='" + secondPlayerId + "';");
            if (resultSet.last()) {
                terminatedGame = new Game(null, null);
                Status gameStatus = Enums.Status.valueOf(resultSet.getString("session_status"));
                String coordinatesDB = resultSet.getString("coordinates");
                JsonObject request = JsonParser.parseString(coordinatesDB).getAsJsonObject();
                terminatedGame.setGameStatus(gameStatus);
                terminatedGame.setGameCoordinates(request);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return terminatedGame;
    }

    /* to test the database connetion and getting some data.
     public void check(){
     try{
     establishConnection();
     statment = connection.createStatement();
     resultSet = statment.executeQuery("SELECT * FROM sign_up");
            
     while(resultSet.next()){
     Player user = new Player();
                
     user.setId(resultSet.getInt("id"));
     user.setFirstName(resultSet.getString("first_name"));
     user.setLastName(resultSet.getString("last_name"));
     user.setEmail(resultSet.getString("email"));
     user.setImg(resultSet.getString("image"));
     user.setPassword(resultSet.getString("password"));
                
     usersVecDetails.add(user);
     }
            
     statment.close();
     connection.close();
            
     } catch (Exception ex){
     ex.printStackTrace();
     }
     }
     */
}
