package com.kshrd.ipcam.network.services;

import com.google.gson.JsonObject;
import com.kshrd.ipcam.entities.respone.ResponseObject;
import com.kshrd.ipcam.entities.user.User;
import com.squareup.okhttp.ResponseBody;

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
 * Created by Miss Chea Navy on 1/5/2017.
 */

/**
 * @Class User Service
 */
public interface UserService {


    @POST("/api/user/addFacebookAccount")
    Call<JsonObject> addUserWithFacebookAccount(@Query("USERNAME") String username,
                                                @Query("EMAIL")String email,
                                                @Query("PASSWORD")String password,
                                                @Query("USER_PROFILE")String user_profile,
                                                @Query("USER_FACEBOOK_ID")String user_facebook_id);

    @Multipart
    @POST("/api/user/addUser")
    Call<JsonObject> addUser(@Query("USERNAME") String username, @Query("EMAIL") String email,
                             @Query("PASSWORD") String password, @Part MultipartBody.Part userImage,
                             @Query("ROLE_ID")int role_id);

    @GET("/api/user/getUserByEmail")
    Call<ResponseObject<User>> getUserByEmail (@Query("email") String email);

    @GET("/api/user/emailChecker")
    Call<Boolean> emailChecker(@Query("EMAIL") String email);

    @GET("/api/user/getUserById")
    Call<ResponseObject<User>> getUserById(@Query("ID") int id);

    @DELETE("/api/user/removeUser")
    Call<JsonObject> removeUser(@Query("USER_ID") int user_id);

    @PUT("/api/user/updateUser")
    Call<JsonObject> updateUser(@Query("USERNAME") String username, @Query("EMAIL") String email,
                                @Query("PASSWORD") String password);

    @Multipart
    @PUT("/api/user/updateUserImage")
    Call<JsonObject> updateUserImage(@Part MultipartBody.Part userImage,
                                       @Query("USER_ID") int user_id);
    @PUT("/api/user/updateUserName")
    Call<JsonObject> updateUserName(@Query("USERNAME") String username, @Query("USER_ID") int user_id);

    @PUT("/api/user/updateUserPassword")
    Call<JsonObject> updateUserPassword(@Query("PASSWORD") String password, @Query("USER_ID") int user_id);
}