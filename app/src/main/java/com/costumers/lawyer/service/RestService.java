package com.costumers.lawyer.service;

/**
 * Created by stiven on 3/2/2016.
 */
public class RestService {
    //You need to change the IP if you testing environment is not local machine
    //or you may have different URL than we have here
    private static final String URL = "http://localhost:40449/api/";//"http://costumermanagement.azurewebsites.net/api/";
    private retrofit.RestAdapter restAdapter;
    private CostumerService apiService;

    public RestService()
    {
        restAdapter = new retrofit.RestAdapter.Builder()
                .setEndpoint(URL)
                .setLogLevel(retrofit.RestAdapter.LogLevel.FULL)
                .build();

        apiService = restAdapter.create(CostumerService.class);
    }

    public CostumerService getService()
    {
        return apiService;
    }

}
