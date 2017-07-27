package com.kshrd.ipcam.entities.camera;
import org.parceler.Parcel;
import org.parceler.ParcelConstructor;
import org.parceler.ParcelFactory;
import org.parceler.ParcelProperty;

import com.google.gson.annotations.SerializedName;
import com.kshrd.ipcam.entities.user.User;

@Parcel(Parcel.Serialization.BEAN)
public class IPCam  {
	@SerializedName("CAMERA_ID")
	 int camera_id;
	@SerializedName("NAME")
	 String name;
	@SerializedName("SERIAL_NUMBER")
	 String serial_number;
	@SerializedName("IP_ADDRESS")
	 String ip_address;
	@SerializedName("WEB_PORT")
	 int web_port;
	@SerializedName("RTSP_PORT")
	 int rtsp_port;
	@SerializedName("USERNAME")
	 String username;
	@SerializedName("PASSWORD")
	 String password;
	@SerializedName("MODEL")
	 Model model;
	@SerializedName("USER")
	 User user;

	public int getImage() {
		return image;
	}

	public void setImage(int image) {
		this.image = image;
	}

	@SerializedName("IMAGE")
	int image;

	 public IPCam() {
	}
	public IPCam(int image){
		this.image=image;
	}

	@ParcelConstructor
	public IPCam(int camera_id, String name, String serial_number, String ip_address, int web_port, int rtsp_port, String username, String password, Model model, User user) {
		this.camera_id = camera_id;
		this.name = name;
		this.serial_number = serial_number;
		this.ip_address = ip_address;
		this.web_port = web_port;
		this.rtsp_port = rtsp_port;
		this.username = username;
		this.password = password;
		this.model = model;
		this.user = user;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCamera_id() {
		return camera_id;
	}
	public void setCamera_id(int camera_id) {
		this.camera_id = camera_id;
	}
	public String getSerial_number() {
		return serial_number;
	}
	public void setSerial_number(String serial_number) {
		this.serial_number = serial_number;
	}
	public String getIp_address() {
		return ip_address;
	}
	public void setIp_address(String ip_address) {
		this.ip_address = ip_address;
	}
	public int getWeb_port() {
		return web_port;
	}
	public void setWeb_port(int web_port) {
		this.web_port = web_port;
	}
	public int getRtsp_port() {
		return rtsp_port;
	}
	public void setRtsp_port(int rtsp_port) {
		this.rtsp_port = rtsp_port;
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
	public Model getModel() {
		return model;
	}
	public void setModel(Model model) {
		this.model = model;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

}