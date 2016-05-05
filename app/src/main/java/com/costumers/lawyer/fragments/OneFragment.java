package com.costumers.lawyer.fragments;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.costumers.lawyer.MainActivityFragment;
import com.costumers.lawyer.R;
import com.costumers.lawyer.activity.MainActivity;
import com.costumers.lawyer.entities.Persons;
import com.costumers.lawyer.service.RestService;

import java.util.List;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class OneFragment extends Fragment{

    RestService
            restService;

    public OneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //refreshScreen();
        return inflater.inflate(R.layout.fragment_one, container, false);
    }


    private void refreshScreen(){

        //Call to server to grab list of student records. this is a asyn
        restService.getService().getCostumers(new Callback<List<Persons>>() {
            @Override
            public void success(List<Persons> persons, Response response) {

                List<Persons> pr= persons;
            }

            @Override
            public void failure(RetrofitError error) {
               Toast.makeText(getView().getContext(), error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });


    }

}