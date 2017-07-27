package com.kshrd.ipcam.entities.camera;

import com.google.gson.annotations.SerializedName;
import com.kshrd.ipcam.entities.plugin.Plugin;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.io.Serializable;

@Parcel(Parcel.Serialization.BEAN)
public class Model{
	@SerializedName("MODEL_ID")
	 int model_id;

	@SerializedName("VENDER")
	 Vender vender;

	@SerializedName("NAME")
	 String name;

	@SerializedName("VENDER_ID")
	 int vender_id;

	@SerializedName("IMAGE")
	 String image;

	@SerializedName("PLUGIN")
	 Plugin plugin;

	@SerializedName("PLUGIN_ID")
	 int plugin_id;

	@SerializedName("STREAM_URL")
	private String stream_url;

	public String getStream_url() {return stream_url;}
	public void setStream_url(String stream_url) {this.stream_url = stream_url;}


	public Model(){}

	@ParcelConstructor public Model(int model_id, Vender vender, String name, int vender_id, String image, Plugin plugin, int plugin_id) {
		this.model_id = model_id;
		this.vender = vender;
		this.name = name;
		this.vender_id = vender_id;
		this.image = image;
		this.plugin = plugin;
		this.plugin_id = plugin_id;
	}

	public int getModel_id() {
		return model_id;
	}

	public void setModel_id(int model_id) {
		this.model_id = model_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Vender getVender() {
		return vender;
	}
	public void setVender(Vender vender) {
		this.vender = vender;
	}

	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}

	public int getVender_id() {
		return vender_id;
	}

	public void setVender_id(int vender_id) {
		this.vender_id = vender_id;
	}

	public int getPlugin_id() {
		return plugin_id;
	}

	public void setPlugin_id(int plugin_id) {
		this.plugin_id = plugin_id;
	}

	public Plugin getPlugin() {
		return plugin;
	}

	public void setPlugin(Plugin plugin) {
		this.plugin = plugin;
	}


}

