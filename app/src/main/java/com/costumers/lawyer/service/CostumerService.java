package com.costumers.lawyer.service;


/**
 * Created by Tan on 6/26/2015.
 */
import com.costumers.lawyer.entities.Event;
import com.costumers.lawyer.entities.EventSMS;
import com.costumers.lawyer.entities.Persons;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import retrofit2.Call;

public interface CostumerService {
    //Retrofit turns our institute WEB API into a Java interface.
    //So these are the list available in our WEB API and the methods look straight forward

    //i.e. http://localhost/api/institute/Students
    @GET("/Costumers")
    public void getCostumers(Callback<List<Persons>> callback);

    //i.e. http://localhost/api/institute/Students/1
    //Get student record base on ID
    @GET("/Costumers")
    public void getCostumerById(@Query("IdPerson") Integer IdPerson,Callback<List<Persons>> callback);

    //i.e. http://localhost/api/institute/Students
    //Add student record and post content in HTTP request BODY
    @POST("/Costumers")
    public void addCostumer(@Body Persons person, Callback<Integer> callback);

    @POST("/Costumers")
    public void EditCostumer(@Body Persons student,@Query("idperson") Integer idperson, Callback<Boolean> callback);

    @GET("/GetEvents")
    public void getEvents(@Query("type") String type,@Query("date") String date,@Query("client") String client,@Query("id") String id,@Query("status") String status,Callback<List<Event>> callback);

    @POST("/InsertEvent")
    public void insertEvents(@Query("type") String type,@Query("start") String start,@Query("end") String end,@Query("client") String client,@Query("description") String description,@Query("title") String title,Callback<Integer> callback);

    @POST("/EventCalendarExecuted")
    public void EventCalendarExecuted(@Query("idevent") Integer idevent,@Query("exec") Boolean exec,Callback<Boolean> callback);

    @POST("/EditEvent")
    public void EditEvent(@Query("type") String type,@Query("start") String start,@Query("end") String end,@Query("client") String client,@Query("description") String description,@Query("title") String title,@Query("idevent") String idevent,Callback<Boolean> callback);

    @POST("/deleteCalendarById")
    public void DeleteCalendarById(@Query("idevent") String idevent,Callback<Boolean> callback);


}
