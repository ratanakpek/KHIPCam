package com.kshrd.ipcam.network.controller;

import android.util.Log;

import com.kshrd.ipcam.entities.camera.Model;
import com.kshrd.ipcam.entities.respone.ResponseList;
import com.kshrd.ipcam.network.ApiDataCompromiser;
import com.kshrd.ipcam.network.ApiGenerator;
import com.kshrd.ipcam.network.controller.CallBack.ModelCallback;
import com.kshrd.ipcam.network.services.CameraService;
import com.kshrd.ipcam.network.services.ModelService;

import java.lang.reflect.Array;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ppsc08 on 10-Jan-17.
 */

public class ModelController {
  String TAG = "ModelController";

   public void getModelNameByVenderName(String vender_name, final ModelCallback modelCallback){
       Call<ResponseList<Model>> call = getModelService().getModelNameByVenderName(vender_name);
       call.enqueue(new Callback<ResponseList<Model>>() {
           @Override
           public void onResponse(Call<ResponseList<Model>> call, Response<ResponseList<Model>> response) {
               if(response != null){
                 //  if(response.body() != null){
                       modelCallback.loadModelName((ArrayList<Model>) response.body().getData());
                 //  }
               }
           }

           @Override
           public void onFailure(Call<ResponseList<Model>> call, Throwable t) {
               t.printStackTrace();
           }
       });
   }


    /**
     *
     * @return connection api
     */
    ModelService getModelService(){
        ModelService modelService = ApiDataCompromiser.createService(ModelService.class,
                ApiGenerator.BASE_URL.toString(),
                ApiGenerator.USERNAME.toString(),
                ApiGenerator.API_PASSWORD.toString());
        return modelService;
    }
}
