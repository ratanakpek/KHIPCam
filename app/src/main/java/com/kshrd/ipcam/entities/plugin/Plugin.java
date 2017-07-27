package com.kshrd.ipcam.entities.plugin;


import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by sophatvathana on 21/12/16.
 */

@Parcel
public class Plugin {

    @SerializedName("PLUGIN_ID")
     int plugin_id;

    @SerializedName("PLUGIN_NAME")
     String plugin_name;

    @SerializedName("PLUGIN_DESCRIPTION")
     String plugin_description;

    @SerializedName("PLUGIN_VERSION")
     String plugin_version;

    @SerializedName("PLUGIN_RELEASE")
     String plugin_release;

    @SerializedName("PLUGIN_ACTIVE")
     boolean plugin_active;

    @SerializedName("PLUGIN_VENDOR")
     String plugin_vendor;

    public int getPlugin_id() {
        return plugin_id;
    }

    public void setPlugin_id(int plugin_id) {
        this.plugin_id = plugin_id;
    }

    public String getPlugin_name() {
        return plugin_name;
    }

    public void setPlugin_name(String plugin_name) {
        this.plugin_name = plugin_name;
    }

    public String getPlugin_description() {
        return plugin_description;
    }

    public void setPlugin_description(String plugin_description) {
        this.plugin_description = plugin_description;
    }

    public String getPlugin_version() {
        return plugin_version;
    }

    public void setPlugin_version(String plugin_version) {
        this.plugin_version = plugin_version;
    }

    public String getPlugin_release() {
        return plugin_release;
    }

    public void setPlugin_release(String plugin_release) {
        this.plugin_release = plugin_release;
    }


    public boolean isPlugin_active() {
        return plugin_active;
    }

    public void setPlugin_active(boolean plugin_active) {
        this.plugin_active = plugin_active;
    }

    public String getPlugin_vendor() {
        return plugin_vendor;
    }

    public void setPlugin_vendor(String plugin_vendor) {
        this.plugin_vendor = plugin_vendor;
    }
}
