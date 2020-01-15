package tictactoe.client;

import com.google.gson.JsonObject;

public class Player {
    
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String img;
    private String password;
    private int points;

    

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Player(){
        id = 0;
        firstName = null;
        lastName = null;
        email = null;
        img = null;
        password = null;
        
    }
    
    Player(String firstName, String email, String password){    //TODO FIX DB AND SIGNUP GUI
        this.id = 0;
        this.firstName = firstName;
        this.lastName = null;
        this.email = email;
        this.img = img;
        this.password = password;
        
    }
        
    Player(String firstName, String lastName, String email, String password){
        this.id = 0;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.img = img;
        this.password = password;
        
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    
    //TODO TEMP!
    public Player setPlayer(String firstName, String email, String password){
        Player p = new Player(firstName, lastName, email, password);
        return p;
    }
    
    public Player setPlayer(String firstName, String lastName, String email, String password){
        Player p = new Player(firstName, lastName, email, password);
        return p;
    }
    
    //sets player in a json object to be used in the data key of the request json
    public JsonObject setPlayerAsJson(String firstName, String email, String password){
        JsonObject data = new JsonObject();
        data.addProperty("firstName", firstName);
        data.addProperty("lastName", "null");
        data.addProperty("email", email);
        data.addProperty("password", password);
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("data", data);
        return jsonObject;
    }
    
    

    
}
