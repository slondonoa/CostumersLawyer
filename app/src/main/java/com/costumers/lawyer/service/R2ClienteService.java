package com.costumers.lawyer.service;

import com.costumers.lawyer.entities.EventSMS;

import java.util.List;
import java.util.concurrent.Executors;

import retrofit.http.POST;
import retrofit.http.Query;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by GL552VW-DM337T on 14/04/2017.
 */

public interface R2ClienteService {
    @GET("api/GetEventsSMS/{date}")
    public Call<List<EventSMS>> GetEventsSMS(@Path("date") String date);

    @retrofit2.http.POST("api/UpdateSendSMS/{idEvent}")
    public Call<Boolean> UpdateSendSMS(@Path("idEvent") String idEvent);


    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://abogadospensionesapi.azurewebsites.net/")
            .addConverterFactory(GsonConverterFactory.create())
            .callbackExecutor(Executors.newSingleThreadExecutor())
            .build();

}
