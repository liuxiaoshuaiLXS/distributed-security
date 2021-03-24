package com.ys.model;

/**
 * @author smile
 * @version 1.0
 * @date 2021/3/21 下午6:50
 * @describe $
 */
public class UserDto {

    private int id;

    private String username;

    private String password;

    private String fullname;

    private String mobile;

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
