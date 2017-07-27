package com.kshrd.ipcam.entities.camera;


import com.google.gson.annotations.SerializedName;
import org.parceler.Parcel;

@Parcel
public class Vender  {

	@SerializedName("VENDER_ID")
	 int vender_id;

	@SerializedName("LOGO")
	 String logo;

	@SerializedName("NAME")
	  String name;

	public int getVender_id() {
		return vender_id;
	}

	public void setVender_id(int vender_id) {
		this.vender_id = vender_id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	@Override
	public String toString() {
		return "Vender [logo=" + logo + "]";
	}
	
}
