package tictactoe.server.db;

import java.sql.*;
import java.util.Vector;
import tictactoe.server.Config;
import tictactoe.server.models.Player;

/**
 *
 * @author asoliman
 */
public class DatabaseManger {

    private Connection connection;
    private Statement statment;
    private ResultSet resultSet;
    private Vector<Player> usersVecDetails = new Vector();
    private boolean online = false;

    public DatabaseManger() throws ClassNotFoundException, SQLException {
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
    public boolean signUp(String first_name, String last_name, String email, String password) throws SQLException {

        if (isEmailExists(email)) {
            System.out.println("Your email is already registered..");
            return false;
        } else {
            try {
                establishConnection();

                PreparedStatement preparedStatment = connection.prepareStatement("INSERT INTO player (first_name, last_name, email, password) VALUES (?, ?, ?, ?);");
                preparedStatment.setString(1, first_name);
                preparedStatment.setString(2, last_name);
                preparedStatment.setString(3, email);
                preparedStatment.setString(4, password);
                preparedStatment.executeUpdate();

                preparedStatment.close();
                connection.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return true;
        }
    }

    //chcek if the email exists in the DB;
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

    // Done and tested.
    public boolean signIn(String email, String password) {
        if (email != null && password != null) {
            try {
                establishConnection();
                statment = connection.createStatement();
                resultSet = statment.executeQuery("SELECT first_name FROM player WHERE email='" + email + "' AND password='" + password + "';");

                if (resultSet.first() == true) {
                    statment.close();
                    connection.close();
                    System.out.println("Login succssed..");
                    online = true;
                    return true;
                } else {
                    statment.close();
                    connection.close();
                    System.out.println("Login failed.");
                    return false;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            System.out.println("Empty fields..");
            return false;
        }
        return false;
    }
    
    public boolean isOnline(){
        return online;
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
