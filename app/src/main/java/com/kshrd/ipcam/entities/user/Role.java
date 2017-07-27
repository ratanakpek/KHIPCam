package com.kshrd.ipcam.entities.user;


import com.google.gson.annotations.SerializedName;
import org.parceler.Parcel;
import org.parceler.ParcelFactory;

import java.io.Serializable;

@Parcel(Parcel.Serialization.BEAN)
public class Role{

    @SerializedName("ROLE_ID")
     int role_id;

    @SerializedName("NAME")
     String name;

    @SerializedName("DESCRIPTION")
     String description;

    public Role(){}

    public Role(int role_id, String name, String description) {
        this.role_id = role_id;
        this.name = name;
        this.description = description;
    }

    public int getRole_id() {
        return role_id;
    }
    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}