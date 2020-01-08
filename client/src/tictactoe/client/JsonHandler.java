/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe.client;

import com.google.gson.JsonObject;
import java.io.DataInputStream;


/**
 *
 * @author Tharwat
 */
public class JsonHandler {
    
    private Thread t1;
    private DataInputStream dataInputStream;
  
    JsonHandler(App app){
        //TODO
    }
    
    public void handle(JsonObject request) { 
        String requestType = request.get("type").getAsString();
        JsonObject requestData = request.getAsJsonObject("data");
        switch (requestType) {
            case "signup-error":
                System.out.println(request);
                break;            
            case "signup-success":
                System.out.println(request);
                break;
            case "signin-success":
                System.out.println(request);
                break;
            case "signin-error":
                System.out.println(request);
                break;
        }
    }
}
