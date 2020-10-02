package softagi.ss.news.models;

import java.io.Serializable;

public class UserModel implements Serializable
{
    private String uId;
    private String name;
    private String email;
    private String phone;

    public UserModel(String uId, String name, String email, String phone) {
        this.uId = uId;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public UserModel() {
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
