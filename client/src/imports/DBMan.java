package imports;

import java.sql.*;
import java.sql.Connection;

public class DBMan
{


    private Connection con;
    private ResultSet rs;
    private Player player;
    private Player oponentPlayer;
    int counter;


    public static void main(String[] args) {

        DBMan db = new DBMan();


    }

    public DBMan()
    {   //TODO
        counter = 0;


        try
        {
//            rs = con.getMetaData().getCatalogs();
            openConnection();
            //TODO
            closeConnection();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    public void openConnection()
    {
        con = DBConnection.getConnectionFromPool();
    }

    public void closeConnection()
    {
        try
        {
            //closing statement
            con.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

}
