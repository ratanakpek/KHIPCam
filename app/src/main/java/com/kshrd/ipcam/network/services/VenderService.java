package com.kshrd.ipcam.network.services;

import com.kshrd.ipcam.entities.camera.Vender;
import com.kshrd.ipcam.entities.respone.ResponseList;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by rina on 1/10/17.
 */

public interface VenderService {
    @GET("/api/vender/getAllVender")
    Call<ResponseList<Vender>> getAllVender();
}
