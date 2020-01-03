package imports;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection
{

    private static Connection obj;

    // private constructor to force use of
    // getConnectionFromPool() to create Connection object
    // imports.DBConnection uses Singleton but is relying on pool
    private DBConnection() throws SQLException
    {
        //TODO
        obj= DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/<DBNAME>","root","mysql");
        //here <DBNAME> is database name, root is username
    }

    public static Connection getConnectionFromPool()
    {
        if (obj==null)
            return null;
        else
            return obj;
    }

}
