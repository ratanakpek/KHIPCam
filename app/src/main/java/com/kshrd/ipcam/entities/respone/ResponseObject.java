package com.kshrd.ipcam.entities.respone;

import com.google.gson.annotations.SerializedName;
import com.kshrd.ipcam.entities.user.User;

public class ResponseObject<T> extends Response {
	@SerializedName("DATA")
	public Object data;
	
	public Object getData()
	{
		return data;
	}
	
	public void setData(Object data)
	{
		this.data=data;
	}
}
