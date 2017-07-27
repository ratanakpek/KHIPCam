package com.kshrd.ipcam.entities.user;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;
import org.parceler.ParcelFactory;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Parcel(Parcel.Serialization.BEAN)
public class User {

	@SerializedName("USER_ID")
	  int user_id;

	@SerializedName("USERNAME")
	 String username;

	@SerializedName("EMAIL")
	 String email;

	@SerializedName("PASSWORD")
	 String password;

	@SerializedName("IMAGE")
	 String image;

	@SerializedName("STATUS")
	 boolean status;

	@SerializedName("ROLE")
	 Role role;

	@SerializedName("USER_FACEBOOK_ID")
	private String user_facebook_id;

	public String getUser_facebook_id() {
		return user_facebook_id;
	}

	public void setUser_facebook_id(String user_facebook_id) {
		this.user_facebook_id = user_facebook_id;
	}
	 public User(){}

	@ParcelConstructor
	public User(int user_id, String username, String email, String password, String image, boolean status, Role role,String user_facebook_id) {
		this.user_id = user_id;
		this.username = username;
		this.email = email;
		this.password = password;
		this.image = image;
		this.user_facebook_id =  user_facebook_id;
		this.status = status;
		this.role = role;
	}

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

	public Role getRole() {return role;}

	public void setRole(Role role) {this.role = role;}

}