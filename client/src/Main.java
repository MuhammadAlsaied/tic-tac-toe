import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import imports.Login;
import imports.Signup;
import imports.UserDetails;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.sql.Connection;

public class Main
{
    private JsonObject jsonObject;
    private JsonObject data;
    private Socket s;
    private DataInputStream dataInputStream;
    private PrintStream printStream;

    Main()
    {
        try
        {
            data = new JsonObject();
            data.addProperty("firstName", "mohummad");
            data.addProperty("lastName", "saied");
            data.addProperty("email", "saied@gmail.com");
            data.addProperty("password", "298347923");
            jsonObject = new JsonObject();
            jsonObject.addProperty("type", "signup");
            jsonObject.add("data", data);

            s = new Socket("127.0.0.1", 4800);
            dataInputStream = new DataInputStream(s.getInputStream());
            printStream = new PrintStream(s.getOutputStream());
            String str = jsonObject.toString();
            System.out.println(str);
            printStream.println(str);

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {

    }


}
