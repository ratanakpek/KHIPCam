package com.kshrd.ipcam.network;

import android.util.Base64;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by rina on 12/30/16.
 * @Class ApiDataCompromiser
 */

public class ApiDataCompromiser {

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private static Retrofit.Builder builder = null ;
           /* new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());*/

    /**
     * @Method createService
     * @param serviceClass define any class
     * @param username  authorization of api
     *   * @param username  authorization of api
     * @param url url file
     * @param <S> define class type
     * @return service that created by any class
     */
    public static <S> S createService(Class<S> serviceClass, String url,  String username,String passwd) {

        if(!httpClient.interceptors().isEmpty()){
            httpClient.interceptors().clear();
        }

        setUrlApi(url);

        httpClient.writeTimeout(30, TimeUnit.SECONDS); // connect timeout
        httpClient.readTimeout(30, TimeUnit.SECONDS);    // socket timeout
       if(username !=null   && passwd != null  ){
            String credential = username + ":" + passwd ;
           final String  basic = "Basic "+ Base64.encodeToString(credential.getBytes(),Base64.NO_WRAP);

           httpClient.addInterceptor(new Interceptor() {
               @Override
               public Response intercept(Chain chain) throws IOException {
                   Request original = chain.request();

                   Request.Builder requestBuilder = original.newBuilder()
                           .header("Authorization", basic)
                           .header("Accept", "application/json")
                           .method(original.method(), original.body());

                   Request request = requestBuilder.build();
                   return chain.proceed(request);
               }
           });
       }

        OkHttpClient client = httpClient.build();
        Retrofit retrofit = builder.client(client).build();

        return retrofit.create(serviceClass);
    }
    /**
     *@Method setUrlAppi
     * @param url set definde url api
     */
    private static void setUrlApi(String url){
        builder = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create());
    }

}
