package tictactoe.client;

import com.google.gson.JsonObject;

public class Player {

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String img;
    private int points;

    Player() {
    }

    Player(String firstName, String lastName, String email, int points) {    //TODO FIX DB AND SIGNUP GUI
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.points = points;
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

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    //sets player in a json object to be used in the data key of the request json
    public JsonObject setPlayerAsJson(String firstName, String email, String password) {
        JsonObject data = new JsonObject();
        data.addProperty("firstName", firstName);
        data.addProperty("lastName", "null");
        data.addProperty("email", email);
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("data", data);
        return jsonObject;
    }

}
