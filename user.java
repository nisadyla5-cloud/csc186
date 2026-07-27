public class User {
    private String username;
    private String password;

    public void setUser(String user, String password){
        this.username = user;
        this.password = password;
    }

    public String getUser(){
        return username;
    }

    public String getPass(){
        return password;
    }
}
