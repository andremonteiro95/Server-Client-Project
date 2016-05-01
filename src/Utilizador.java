import java.io.Serializable;

/**
 * Created by vicesource on 04-04-2016.
 */
public class Utilizador implements Serializable {
    private String username;
    private String password;
    private boolean privilegio = false; // true = admin

    public Utilizador(String user, String passwd){
        username=user;
        password=passwd;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void giveAdmin(){
        privilegio = true;
    }
}
