package com.kshrd.ipcam.entities.form;

import com.google.gson.annotations.SerializedName;

/**
 * Created by rina on 12/22/16.
 */
public class UserModifier {

    @SerializedName("USER_ID")
    private  int user_id;

    @SerializedName("USERNAME")
    protected String username;
    @SerializedName("PASSWORD")
    protected String password;
    @SerializedName("EMAIL")
    private String email;
    @SerializedName("IMAGE")
    protected String image;
    @SerializedName("STATUS")
    protected boolean status;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}
}