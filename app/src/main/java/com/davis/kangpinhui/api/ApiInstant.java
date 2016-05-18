package com.davis.kangpinhui.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by davis on 16/5/17.
 */
public class ApiInstant {


    private static   ApiService service=null;

    private static String base_url="http://m2.kangpinhui.com:8089";

    public static final ApiService getInstant(){

        if(service==null){

            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .create();

            Retrofit retrofit=new Retrofit.Builder()
                    .baseUrl(base_url)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            service  = retrofit.create(ApiService.class);
        }
        return  service;
    };


}
