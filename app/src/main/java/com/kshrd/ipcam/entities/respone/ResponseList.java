package com.kshrd.ipcam.entities.respone;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ResponseList<T> extends Response {
	@SerializedName("DATA")
	public List<T> data;
	
	public List<T> getData()
	{
		return data;
	}
	
	public void setData(ArrayList<T> data)
	{
		this.data=data;
	}
	
}
