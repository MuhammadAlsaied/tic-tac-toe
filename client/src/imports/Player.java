package imports;

public class Player
{
    private String userName;
    private String name;
    private String email;
    private String password;
    private int highscore;
    private boolean status;

    //case of login
    Player(String userName, String password)
    {
        this.userName = userName;
        this.name = null;
        this.email = null;
        this.password = password;
        this.highscore = 0;
    }

    //case of sign-up or instantiating
    Player(String userName, String name, String email, String password, int highscore)
    {
        this.userName = userName;
        this.name = name;
        this.email = email;
        this.password = password;
        this.highscore = highscore;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getEmail()
    {
        return email;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getPassword()
    {
        return password;
    }

    public void setHighscore(int highscore)
    {
        this.highscore = highscore;
    }

    public int getHighscore()
    {
        return highscore;
    }

    public void setStatus(boolean status)
    {
        this.status = status;
    }

    public boolean getStatus()
    {
        return status;
    }



    public boolean isOnline()
    {
        return status;
    }

    public void printPlayer()
    {
        System.out.println(userName);
        System.out.println(name);
        System.out.println(email);
        System.out.println(password);
        System.out.println(highscore);
    }

    public static void main(String[] args)
    {
        Player p1 = new Player("userName", "tharwat", "woehfoh@iwhefi", "wakejfhihwe", 28349827);
        p1.printPlayer();
    }
}