package LSB.web.Mapper;

/**
 * @ClassName: Account
 * @Description: TODO
 * @Author: Nick Lee
 * @Date: Create in 23:06 2023/3/6
 **/
public class Account {
    String ID;
    String nickname;
    String password;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    String key;
}
