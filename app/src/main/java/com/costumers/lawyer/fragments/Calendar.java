package com.costumers.lawyer.fragments;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.costumers.lawyer.R;
import com.costumers.lawyer.activity.DetailCostumer;
import com.costumers.lawyer.activity.MainActivity;
import com.costumers.lawyer.adapter.CalendarAdapter;
import com.costumers.lawyer.adapter.CostumerAdapter;
import com.costumers.lawyer.entities.Event;
import com.costumers.lawyer.entities.EventAdapter;
import com.costumers.lawyer.service.RestService;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class Calendar extends Fragment{


    public Calendar newInstance() {
        return new Calendar();
    }

    private RecyclerView mRecyclerView;
    private CalendarAdapter mAdapter;
    private List<EventAdapter> mEvents;
    ProgressDialog dialog =null;
    RestService restService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        return view;

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refreshCalendar();
    }


    private void refreshCalendar(){
        //Call to server to grab list of student records. this is a asyn
        try {
            if (conecNetWork()) {
                dialog = ProgressDialog.show(getActivity(), "",
                        "Actualizando calendario. Por favor espere...", true);
                restService = new RestService();
                restService.getService().getEvents(new Callback<List<Event>>() {
                    @Override
                    public void success(List<Event> events, Response response) {
                        mEvents = new ArrayList<>();
                        List<Event> lstEvents = events;


                        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));



                        for(Event event:lstEvents) {
                            String name="";
                            if(event.FullName.length() > 21) {
                                name = event.FullName.substring(0, 19) + "...";
                            }else {
                                name = event.FullName;
                            }


                            EventAdapter pa=new EventAdapter(event.Id,event.TypeEvent,event.Description,event.Customer,event.StartDate
                                    ,event.EndDate,event.Title,event.Executed,name.toUpperCase(),event.strStartDate,event.strEndDate);
                            mEvents.add(pa);

                        }
                        mAdapter = new CalendarAdapter(getActivity(), mEvents);
                        mRecyclerView.setAdapter(mAdapter);

                        mAdapter.setOnItemClickListener(new CalendarAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(String Idperson, int position) {
                                String message = Idperson;
                            }
                        });
                        if (dialog != null) {
                            dialog.cancel();
                        }
                        Toast.makeText(getActivity(), "Calendario actualizado", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(getActivity(), error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }else {

                showAlertDialog(getActivity(),"Conexión a internet","Usted no cuenta con conexión a internet para la actualizacion del calendario",true);
            }
        }catch (Exception e){
            if (dialog != null) {
                dialog.cancel();
            }
            showAlertDialog(getActivity(),"Error","No fue posible realizar la actualización del calendario, por favor intentelo de nuevo",true);
        };

    }
    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.cancel();
            }
        });
        alertDialog.show();
    }

    protected Boolean conecNetWork() {
        ConnectivityManager conMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return true;
        } else {
            return false;
        }

    }

}
