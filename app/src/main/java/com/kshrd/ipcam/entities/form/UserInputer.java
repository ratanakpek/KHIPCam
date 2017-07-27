package com.kshrd.ipcam.entities.form;


import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import okhttp3.MultipartBody;

/**
 * Created by rina on 12/22/16.
 */
@Parcel(Parcel.Serialization.BEAN)
public class UserInputer {

    @SerializedName("USERNAME")
    protected String username;

    @SerializedName("EMAIL")
    private String email;
    @SerializedName("PASSWORD")
    protected String password;
    @SerializedName("IMAGE")
    protected String image;
    @SerializedName("ROLE_ID")
    protected  int role_id;

    @SerializedName("USER_FACEBOOK_ID")
    private String user_facebook_id;


    public UserInputer(){}

    @ParcelConstructor
    public UserInputer(String username, String email, String password, String image, int role_id, String user_facebook_id) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.image = image;
        this.role_id = role_id;
        this.user_facebook_id = user_facebook_id;

    }

    public String getUser_facebook_id() {
        return user_facebook_id;
    }

    public void setUser_facebook_id(String user_facebook_id) {
        this.user_facebook_id = user_facebook_id;
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

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public int getRole_id() {
        return role_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }
}