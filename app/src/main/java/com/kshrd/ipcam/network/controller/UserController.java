package com.kshrd.ipcam.network.controller;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import com.kshrd.ipcam.entities.respone.ResponseObject;
import com.kshrd.ipcam.entities.user.User;
import com.kshrd.ipcam.network.ApiDataCompromiser;
import com.kshrd.ipcam.network.ApiGenerator;
import com.kshrd.ipcam.network.controller.CallBack.UserCallback;
import com.kshrd.ipcam.network.services.UserService;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Miss Chea Navy on 1/5/2017.
 */
/**
 * @Class User Controller
 */
public class UserController {

    /*Field*/
    private List<User> listUser;
    String TAG = "USER_CONTROLLER";

    public void addUserFacebookAccount(String username,String email,String password,String user_profile,String user_facebook_id){
        Call<JsonObject> call = getUserService().addUserWithFacebookAccount(username,email,password,user_profile,user_facebook_id);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d(TAG, "onResponse: "+response.body());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    /*@Method Post user */
    public void addUser(String username, String email, String password,
                        MultipartBody.Part image, int i){

        Call<JsonObject> call = getUserService().addUser(username, email, password, image, 2);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d(TAG, "onResponse: "+response.body());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }
    public void emailChecker(String email, final UserCallback userCallback){
        Call<Boolean> call = getUserService().emailChecker(email);
        final Boolean emailState;
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                Log.d(TAG, "onResponse: "+response.body());
                userCallback.emailCheckerState(response.body());
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    public void updateUserProfile(MultipartBody.Part user_image,int user_id){

        Log.d(TAG, user_id+" updateUserProfile: "+user_image);

        Log.d("ImageChange", "updateUserProfile: "+user_image);

        Call<JsonObject> call = getUserService().updateUserImage(user_image,user_id);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d(TAG, "onResponse: "+response.body());
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void getUserByEmail(String email, final UserCallback userCallback){
        Log.i(TAG, "getUserByEmail: "+email);
        Call<ResponseObject<User>> call = getUserService().getUserByEmail(email);
        call.enqueue(new Callback<ResponseObject<User>>() {
            @Override
            public void onResponse(Call<ResponseObject<User>> call, Response<ResponseObject<User>> response) {

                if(response.body() != null){
                    Log.d(TAG, "onResponse: "+response.body().getData());
                    if(response.body().getData() != null){
                        LinkedTreeMap t = (LinkedTreeMap) response.body().getData();

                        User user = new User();

                        user.setUser_id(((Double)t.get("USER_ID")).intValue());
                        user.setUsername((String)t.get("USERNAME"));
                        user.setEmail((String)t.get("EMAIL"));
                        user.setPassword((String)t.get("PASSWORD"));
                        user.setImage((String)t.get("IMAGE"));
                        user.setStatus((Boolean) t.get("STATUS"));
                        user.setUser_facebook_id((String)t.get("USER_FACEBOOK_ID"));

                        userCallback.loadUserByEmail(user);
                    }
                }else {
                    Log.i(TAG, "onResponse:  ================================>>>>>>>>>>>>> GetUserByEmail cannot response "+response.body());
                }
            }

            @Override
            public void onFailure(Call<ResponseObject<User>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void getUserById(int id){
        Call<ResponseObject<User>> call = getUserService().getUserById(id);
        call.enqueue(new Callback<ResponseObject<User>>() {
            @Override
            public void onResponse(Call<ResponseObject<User>> call, Response<ResponseObject<User>> response) {
                if (response.body() != null){
                    Log.d(TAG, "onResponse: "+response.body().getData());
                }else {
                    Log.i(TAG, "onResponse:  ================================>>>>>>>>>>>>> GetUserByID cannot response "+response.body());
                }
            }

            @Override
            public void onFailure(Call<ResponseObject<User>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    UserService getUserService(){
        UserService userService = ApiDataCompromiser.createService(UserService.class,
                ApiGenerator.BASE_URL.toString(),
                ApiGenerator.USERNAME.toString(),
                ApiGenerator.API_PASSWORD.toString());
        return userService;
    }
}
