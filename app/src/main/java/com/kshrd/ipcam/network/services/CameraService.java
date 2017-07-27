package com.kshrd.ipcam.network.services;

import com.google.gson.JsonObject;
import com.kshrd.ipcam.entities.camera.IPCam;
import com.kshrd.ipcam.entities.form.IPCameraInputer;
import com.kshrd.ipcam.entities.form.IPCameraModifier;
import com.kshrd.ipcam.entities.respone.ResponseList;
import com.kshrd.ipcam.entities.respone.ResponseObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * Created by rina on 1/1/17.
 * @Interface Camera Service
 */

public interface CameraService {

    @GET("/api/camera/cmd")
    Call<Boolean> camCmd(@Query("email") String email,
                         @Query("camera") int camera_id,
                         @Query("method") String method);

    @GET("/api/camera/all")
    Call<ResponseList<IPCam>> getALLCamera();

    @GET("/api/camera/getCameraById")
    Call<ResponseObject<IPCam> > getCameraById(@Query("ID") int camera_id);

    @GET("/api/camera/getCameraByUserId")
    Call<ResponseList<IPCam>> getCameraByUserId(@Query("USER_ID") int user_id);

    @POST("/api/camera/addCamera")
    Call<JsonObject> addCamera(@Body IPCameraInputer ipCameraInputer);

    @PUT("/api/camera/updateCamera")
    Call<JsonObject> modifier(@Body IPCameraModifier ipCameraModifier);

    @DELETE("/api/camera/removeCameraById")
    Call<JsonObject> removeCamera(@Query("ID") int camera_id);
}
