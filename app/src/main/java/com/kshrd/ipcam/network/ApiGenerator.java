package com.kshrd.ipcam.network;

/**
 * Created by rina on 12/30/16.
 */
public enum ApiGenerator {

    URL_STREAMING("http://120.136.24.173:9000"),
  //  BASE_URL("http://120.136.24.173:8080"),
  BASE_URL("http://192.168.1.101:8080"),
    USERNAME("admin"),API_PASSWORD("kompongsom");

    private String value;
    ApiGenerator(String value){
        this.value = value;
    }


    @Override
    public   String toString(){
        return  value;
    }
}