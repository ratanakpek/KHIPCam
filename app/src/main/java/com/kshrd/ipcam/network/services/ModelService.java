package com.kshrd.ipcam.network.services;

/**
 * Created by rina on 12/30/16.
 */

import com.google.gson.JsonObject;
import com.kshrd.ipcam.entities.camera.Model;
import com.kshrd.ipcam.entities.respone.ResponseList;
import com.kshrd.ipcam.entities.respone.ResponseObject;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * @Interface Camera controller
 */
public interface ModelService {
   /* API_MODEL_GET_ALL("api/model/getAllModel"),
    API_MODEL_GET_BY_ID("api/model/getModelById"),
    API_MODEL_ADD("api/model/addModel"),
    API_MODEL_UPDATE("api/model/updateModel"),
    API_MODEL_REMOVE("api/model/removeModel"),*/


    @GET("/api/model/getModelNameByVenderName")
    Call<ResponseList<Model>> getModelNameByVenderName(@Query("VENDER_NAME")String vender_name);

    @GET("/api/model/getAllModel")
    Call<ResponseList<Model>> getAllModel();

    @GET("/api/model/getModelById")
    Call<ResponseObject<Model>> getModelById(@Query("MODEL_ID")int model_id);

    @Multipart
    @POST("/api/model/addModel")
    Call<JsonObject> addNew(@Query("NAME") String name, @Query("VENDER_ID") int vender_id,
                            @Query("PLUIGIN_ID") int plugin_id, @Part MultipartBody.Part image);

    @PUT("api/model/updateModel")
    Call<JsonObject> update(@Query("MODEL_ID") int model_id, @Query("NAME") String name,
                            @Query("VENDER_ID") int vender_id, @Query("PLUGIN_ID") int plugin_id,
                            @Part MultipartBody.Part image);

    @DELETE("/api/model/removeModel")
    Call<JsonObject> remove(@Query("MODEL_ID") int model_id);
}
