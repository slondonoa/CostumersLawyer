package com.costumers.lawyer.fragments;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.costumers.lawyer.R;
import com.costumers.lawyer.activity.DetailCostumer;
import com.costumers.lawyer.activity.MainActivity;
import com.costumers.lawyer.adapter.CalendarAdapter;
import com.costumers.lawyer.adapter.CostumerAdapter;
import com.costumers.lawyer.data.DataBaseManager;
import com.costumers.lawyer.entities.Customer;
import com.costumers.lawyer.entities.Event;
import com.costumers.lawyer.entities.EventAdapter;
import com.costumers.lawyer.entities.Persons;
import com.costumers.lawyer.service.RestService;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
    private DataBaseManager manager;
    private RecyclerView mRecyclerView;
    private CalendarAdapter mAdapter;
    private List<EventAdapter> mEvents;
    ProgressDialog dialog =null;
    RestService restService;
    private Spinner spTypeEvent;
    private Spinner spCustomers;
    private TextView txtdate,txtfilter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        manager=new DataBaseManager(getActivity());
        ArrayList<Customer> lstPersons=manager.getAllPersonsVector();
        final View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        txtdate=(TextView) view.findViewById(R.id.txtDate);
        txtfilter=(TextView) view.findViewById(R.id.txtfilter);

        spTypeEvent = (Spinner) view.findViewById(R.id.spTypeEvent);
        spCustomers = (Spinner) view.findViewById(R.id.spCustomers);

        String[] arrayListprocessstatus = getResources().getStringArray(R.array.type_event);
        ArrayAdapter<String> arrayAdapterTypeEvent = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                arrayListprocessstatus );
        spTypeEvent.setAdapter(arrayAdapterTypeEvent);

        ArrayAdapter<Customer> arrayAdapterCustomers = new ArrayAdapter<Customer>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                lstPersons );

        spCustomers.setAdapter(arrayAdapterCustomers);


        ImageButton btndate=(ImageButton)view.findViewById(R.id.btnDate);
        btndate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                java.util.Calendar now = java.util.Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                int _monthOfYear=monthOfYear+1;
                                String month = String.valueOf(_monthOfYear);
                                if (month.length() == 1) {
                                    month = "0" + month;
                                }
                                String day = String.valueOf(dayOfMonth);
                                if (day.length() == 1) {
                                    day = "0" + day;
                                }
                                String date = year + "/" + month + "/" + day;
                                txtdate.setText(date);
                            }
                        },
                        now.get(java.util.Calendar.YEAR),
                        now.get(java.util.Calendar.MONTH),
                        now.get(java.util.Calendar.DAY_OF_MONTH)
                );
                dpd.setThemeDark(false);
                dpd.vibrate(false);
                dpd.setTitle("Ultimo contacto");
                dpd.setAccentColor(Color.parseColor("#125688"));
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });


        ImageButton btnNoDate=(ImageButton)view.findViewById(R.id.btnNoDate);
        btnNoDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtdate=(TextView) view.findViewById(R.id.txtDate);
                txtdate.setText("");
            }
        });


        java.util.Date date= new Date();
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(date);
        String month = String.valueOf(cal.get(java.util.Calendar.MONTH));
        String day = String.valueOf(cal.get(java.util.Calendar.DATE));
        String year = String.valueOf(cal.get(java.util.Calendar.YEAR));
        if (month.length()==1)
        {
            int montemp=Integer.parseInt(month)+1;
            month="0"+montemp;
        }else {
            int montemp=Integer.parseInt(month)+1;
            month=String.valueOf(montemp);
        }
        if (day.length()==1)
        {
            day="0"+day;
        }

        String dateCal = year + "/" + month + "/" + day;
        txtdate.setText(dateCal);


        LinearLayout lFilter = (LinearLayout) view.findViewById(R.id.LinerFilter);
        lFilter.setVisibility(View.GONE);
        ImageButton btnFilterDown=(ImageButton)view.findViewById(R.id.btnFilterDown);
        ImageButton btnFilterUp=(ImageButton)view.findViewById(R.id.btnFilterUp);
        btnFilterDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout lFilter = (LinearLayout) view.findViewById(R.id.LinerFilter);
                lFilter.setVisibility(View.VISIBLE);
                ImageButton btnFilterUp=(ImageButton)view.findViewById(R.id.btnFilterUp);
                btnFilterUp.setVisibility(View.VISIBLE);
                ImageButton btnFilterDown=(ImageButton)view.findViewById(R.id.btnFilterDown);
                btnFilterDown.setVisibility(View.GONE);
            }
        });


        btnFilterUp.setVisibility(View.INVISIBLE);
        btnFilterUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout lFilter = (LinearLayout) view.findViewById(R.id.LinerFilter);
                lFilter.setVisibility(View.GONE);
                ImageButton btnFilterUp=(ImageButton)view.findViewById(R.id.btnFilterUp);
                btnFilterUp.setVisibility(View.GONE);
                ImageButton btnFilterDown=(ImageButton)view.findViewById(R.id.btnFilterDown);
                btnFilterDown.setVisibility(View.VISIBLE);
            }
        });




        Button btnFilter=(Button)view.findViewById(R.id.btnFilter);
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshCalendar();
            }
        });


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
                String type=spTypeEvent.getSelectedItem().toString();
                if (type.equals("Tipo"))
                {
                    type=null;
                }
                else {
                    switch (type){
                        case "Cita":
                            type="1";
                            break;
                        case "Audiencia":
                            type="2";
                            break;
                        case "Vencimiento":
                            type="3";
                            break;
                        case "Otros":
                            type="4";
                            break;
                    }


                }

                String date=txtdate.getText().toString();
                if (date.equals(""))
                {
                    date=null;
                }
                Customer client=(Customer) spCustomers.getSelectedItem();
                String clienID=null;
                if ( client.getId().equals("0") )
                {
                    clienID=null;
                }else
                {
                    clienID=client.getId().toString();
                }
                String id=null;
                restService.getService().getEvents(type,date,clienID,id,new Callback<List<Event>>() {
                    @Override
                    public void success(List<Event> events, Response response) {
                        mEvents = new ArrayList<>();
                        List<Event> lstEvents = events;


                        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));



                        for(Event event:lstEvents) {
                            String name="";
                            if(event.FullName.length() > 29) {
                                name = " - "+event.FullName.substring(0, 29) + "...";
                            }else {
                                name = " - "+event.FullName;
                            }
                            switch (event.TypeEvent) {
                                case "1":
                                    event.TypeEvent="Cita";
                                    break;
                                case "2":
                                    event.TypeEvent="Audiencia";
                                    break;
                                case "3":
                                    event.TypeEvent="Vencimiento";
                                    break;
                                case "4":
                                    event.TypeEvent="Otros";
                            }



                            String strStartDate="";
                            String strStrartHour="";
                            try {
                                strStartDate=event.StartDate.split("T")[0];
                                strStrartHour=event.strStartDate.split(" ")[1] + " " +event.strStartDate.split(" ")[2];

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Date endDate=new Date();
                            String strEndDate="";
                            String strEndHour="";
                            try {
                                strEndDate=event.StartDate.split("T")[0];
                                strEndHour=event.strStartDate.split(" ")[1] + " "+event.strStartDate.split(" ")[2] ;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            EventAdapter pa=new EventAdapter(event.Id,event.TypeEvent,event.Description,event.Customer,strStartDate
                                    ,strEndDate,event.Title,event.Executed,name.toUpperCase(),strStrartHour,strEndHour);
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

                        String textFilter="";
                        String dateFilter=txtdate.getText().toString();
                        if (!dateFilter.equals(""))
                        {
                            textFilter="Fecha: "+dateFilter;
                        }

                        String type=spTypeEvent.getSelectedItem().toString();
                        if (type.equals("Tipo"))
                        {
                            type=null;
                        }else {

                            textFilter=textFilter + " - Tipo: "+type;
                        }

                        Customer client=(Customer) spCustomers.getSelectedItem();
                        String clienID=null;
                        if ( client.getId().equals("0") )
                        {
                            clienID=null;
                        }else
                        {
                            clienID=client.getId().toString();
                            textFilter=textFilter + " - Cliente: "+client.getName().toString();
                        }
                        String id=null;
                        txtfilter=(TextView) getActivity().findViewById(R.id.txtfilter);
                        txtfilter.setText(textFilter);


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
