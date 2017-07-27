package com.kshrd.ipcam.entities.form;


import com.google.gson.annotations.SerializedName;
import com.kshrd.ipcam.entities.plugin.Plugin;

/**
 * Created by rina on 12/21/16.
 */
public class ModelInputer  {

    @SerializedName("NAME")
    private String name;

    @SerializedName("VENDER_ID")
    private int vender_id;

    @SerializedName("IMAGE")
    private String image;

    @SerializedName("PLUGIN")
    private Plugin plugin;

    @SerializedName("PLUGIN_ID")
    private int plugin_id;

    @SerializedName("STREAM_URL")
    private String stream_url;

    public String getStream_url() {return stream_url;}
    public void setStream_url(String stream_url) {this.stream_url = stream_url;}



    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
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
}