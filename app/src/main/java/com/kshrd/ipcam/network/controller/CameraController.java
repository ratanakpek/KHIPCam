package com.kshrd.ipcam.network.controller;

/**
 * Created by rina on 12/30/16.
 */

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import com.kshrd.ipcam.activitys.ConfigCameraActivity;
import com.kshrd.ipcam.activitys.customerAdapter.CustomizeListCamera;
import com.kshrd.ipcam.entities.camera.IPCam;
import com.kshrd.ipcam.entities.camera.Model;
import com.kshrd.ipcam.entities.camera.Vender;
import com.kshrd.ipcam.entities.form.IPCameraInputer;
import com.kshrd.ipcam.entities.form.IPCameraModifier;
import com.kshrd.ipcam.entities.respone.ResponseList;
import com.kshrd.ipcam.entities.respone.ResponseObject;
import com.kshrd.ipcam.network.ApiDataCompromiser;
import com.kshrd.ipcam.network.ApiGenerator;
import com.kshrd.ipcam.network.controller.CallBack.CameraCallback;
import com.kshrd.ipcam.network.services.CameraService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @Class Camera controller
 */
public class CameraController {


    /**
     * Field
     **/
    private List<IPCam> listIpCam;

    /**
     * @Method Remote Camdera
     */
    public void doCommand(String email, int camera_id, String cmd) {
        Call<Boolean> call = getCameraService().camCmd(email, camera_id, cmd);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                Log.d(TAG, "onResponse: " + response.body());
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    /**
     * @param id             camera id
     * @param cameraCallback get data after responding
     */
    public void getCameraById(int id, final CameraCallback cameraCallback) {

        Call<ResponseObject<IPCam>> call = getCameraService().getCameraById(id);
        call.enqueue(new Callback<ResponseObject<IPCam>>() {
            @Override
            public void onResponse(Call<ResponseObject<IPCam>> call, Response<ResponseObject<IPCam>> response) {
                if (response.body() != null) {
                    LinkedTreeMap t = (LinkedTreeMap) response.body().getData();
                    IPCam ipCam = new IPCam();

                    ipCam.setCamera_id(((Double) t.get("COMERA_ID")).intValue());

                    ipCam.setName((String) t.get("NAME"));
                    ipCam.setSerial_number((String) t.get("SERIAL_NUMBER"));
                    ipCam.setIp_address((String) t.get("IP_ADDRESS"));
                    ipCam.setRtsp_port(
                            ((Double) t.get("RTSP_PORT")).intValue());
                    ipCam.setWeb_port(((Double) t.get("WEB_PORT")).intValue());
                    ipCam.setUsername((String) t.get("USERNAME"));
                    ipCam.setPassword((String) t.get("PASSWORD"));

                    LinkedTreeMap modelMap = (LinkedTreeMap) t.get("MODEL");
                    Model model = new Model();
                    model.setModel_id(((Double) modelMap.get("MODEL_ID")).intValue());
                    model.setName((String) modelMap.get("NAME"));
                    model.setImage((String) modelMap.get("IMAGE"));
                    //     model.setVender_id(((Double)modelMap.get("VENDOR_ID")).intValue());


                    LinkedTreeMap venderMap = (LinkedTreeMap) modelMap.get("VENDER");
                    Vender vender = new Vender();
                    vender.setName((String) venderMap.get("NAME"));
                    vender.setVender_id(((Double) venderMap.get("VENDER_ID")).intValue());
                    vender.setLogo((String) venderMap.get("LOGO"));

                    model.setVender(vender);

                    ipCam.setModel(model);
                    cameraCallback.loadOneCamera(ipCam);
                } else {
                    Log.i("getCameraById", "onResponse : ==============================>>>>>>>>> Cannot load camera by id!  " + response.body());
                }
            }

            @Override
            public void onFailure(Call<ResponseObject<IPCam>> call, Throwable t) {

            }
        });
    }


    String TAG = "$#$#$#$#$#$#$#_+++++";

    /**
     * @param user_id        user id
     * @param cameraCallback get data after responding
     */
    public void getCameraByUser(int user_id, final CameraCallback cameraCallback) {
        Log.d("CamByUser", "getCameraByUser: " + user_id);

        Call<ResponseList<IPCam>> call = getCameraService().getCameraByUserId(user_id);
        call.enqueue(new Callback<ResponseList<IPCam>>() {
            @Override
            public void onResponse(Call<ResponseList<IPCam>> call, Response<ResponseList<IPCam>> response) {

                if (response.body() != null) {
                    cameraCallback.loadListCameraByUser(response.body().getData());
                    Log.i("CamByUser", "onResponse : ==============================>>>>>>>>> in loadCameraByUser controller " + response.body().getData());
                } else {
                    Log.i("CamByUser", "onResponse : ==============================>>>>>>>>> Cannot load camera by user!  " + response.body());
                }
            }

            @Override
            public void onFailure(Call<ResponseList<IPCam>> call, Throwable t) {

            }
        });
    }

    /**
     * @Method update camera
     */
    public void updateCamera(IPCameraModifier ipCameraModifier) {

        Log.d(TAG, "updateCamera: " + ipCameraModifier.getIp_address());

        Call<JsonObject> call = getCameraService().modifier(ipCameraModifier);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d(TAG, "onResponse: " + response.body());
                if (response.body() != null) {
                    CustomizeListCamera.isDataCameraChanged = true;
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    /**
     * @Method Remove Camera
     */
    public void removeCamera(int camera_id) {
        Call<JsonObject> call = getCameraService().removeCamera(camera_id);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d(TAG, "onRomveCamera " + response.body());

                if (response.body() != null) {
                    CustomizeListCamera.isDataCameraChanged = true;
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });


    }


    /**
     * @param cameraCallback get list data
     */
    public void getAllCamera(final CameraCallback cameraCallback) {
        Call<ResponseList<IPCam>> call = getCameraService().getALLCamera();
        call.enqueue(new Callback<ResponseList<IPCam>>() {
            @Override
            public void onResponse(Call<ResponseList<IPCam>> call, Response<ResponseList<IPCam>> response) {
                cameraCallback.loadListCamera(response.body().getData());
            }

            @Override
            public void onFailure(Call<ResponseList<IPCam>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void addCamera(final Context context, IPCameraInputer bodyCamera) {
        Call<JsonObject> call = getCameraService().addCamera(bodyCamera);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {
                    if (response.body().get("MESSAGE").getAsString() != null) {
                        Log.i("AddCamera", "onResponse: " + response.body().get("MESSAGE").getAsString());
                        Toast.makeText(context, response.body().get("MESSAGE").getAsString(), Toast.LENGTH_LONG).show();

                        if (context instanceof ConfigCameraActivity) {
                            ConfigCameraActivity conf = (ConfigCameraActivity) context;
                            conf.clearText();
                            conf.isDataCameraChanged = true;
                        }
                    }
                } else {
                    Toast.makeText(context, "Something went wrong!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    /**
     * @return connection api
     */
    CameraService getCameraService() {
        CameraService cameraService = ApiDataCompromiser.createService(CameraService.class,
                ApiGenerator.BASE_URL.toString(),
                ApiGenerator.USERNAME.toString(),
                ApiGenerator.API_PASSWORD.toString());
        return cameraService;
    }


}