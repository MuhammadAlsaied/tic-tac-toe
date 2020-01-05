package tictactoe.client;

import com.google.gson.JsonObject;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Main
{
    private JsonObject jsonObject;
    private JsonObject data;
    private Socket s;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

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

            s = new Socket(Config.SERVER_IP, Config.PORT);
            dataInputStream = new DataInputStream(s.getInputStream());
            dataOutputStream = new DataOutputStream(s.getOutputStream());
            String str = jsonObject.toString();
            System.out.println(str);
            dataOutputStream.write(str.getBytes());

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        new Main();
    }


}
