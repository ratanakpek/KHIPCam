package com.kshrd.ipcam.network.controller;

import android.util.Log;

import com.kshrd.ipcam.entities.camera.Vender;
import com.kshrd.ipcam.entities.respone.ResponseList;
import com.kshrd.ipcam.network.ApiDataCompromiser;
import com.kshrd.ipcam.network.ApiGenerator;
import com.kshrd.ipcam.network.controller.CallBack.VenderCallback;
import com.kshrd.ipcam.network.services.ModelService;
import com.kshrd.ipcam.network.services.VenderService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by rina on 1/10/17.
 */

public class VenderController {
String TAG = "VenderController";
    public void getAllVender(final VenderCallback venderCallback){
        Call<ResponseList<Vender>> call = getVenderService().getAllVender();
        call.enqueue(new Callback<ResponseList<Vender>>() {
            @Override
            public void onResponse(Call<ResponseList<Vender>> call, Response<ResponseList<Vender>> response) {
              //  Log.d(TAG, "onResponse: "+response.body().getData());
                venderCallback.loadAllVender((ArrayList<Vender>) response.body().getData());
            }

            @Override
            public void onFailure(Call<ResponseList<Vender>> call, Throwable t) {

            }
        });

    }

     VenderService getVenderService(){
        return  ApiDataCompromiser.createService(VenderService.class,
                ApiGenerator.BASE_URL.toString(),
                ApiGenerator.USERNAME.toString(),
                ApiGenerator.API_PASSWORD.toString());
    }
}
