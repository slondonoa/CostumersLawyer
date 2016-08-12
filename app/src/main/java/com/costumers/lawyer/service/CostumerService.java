package com.costumers.lawyer.service;


/**
 * Created by Tan on 6/26/2015.
 */
import com.costumers.lawyer.entities.Persons;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

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

}
