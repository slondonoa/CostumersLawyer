package com.costumers.lawyer.fragments;


import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Interpolator;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.costumers.lawyer.R;
import com.costumers.lawyer.activity.DetailCostumer;
import com.costumers.lawyer.activity.EditEventCalendar;
import com.costumers.lawyer.activity.InsertEventCalendar;
import com.costumers.lawyer.activity.MainActivity;
import com.costumers.lawyer.adapter.CalendarAdapter;
import com.costumers.lawyer.adapter.CostumerAdapter;
import com.costumers.lawyer.data.DataBaseManager;
import com.costumers.lawyer.entities.Customer;
import com.costumers.lawyer.entities.Event;
import com.costumers.lawyer.entities.EventAdapter;
import com.costumers.lawyer.entities.EventSMS;
import com.costumers.lawyer.entities.Persons;
import com.costumers.lawyer.service.R2ClienteService;
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
import retrofit2.Call;
import retrofit2.Retrofit;

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
    private Spinner spTypeEvent, spStatus;
    private TextView txtdate,txtfilter;
    boolean click = false;
    PhoneStateListener listener;
    Thread SMSthread =null;
    AutoCompleteTextView spCustomers;


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
        spCustomers = (AutoCompleteTextView) view.findViewById(R.id.spCustomers);
        spStatus = (Spinner) view.findViewById(R.id.spStatus);


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

        spCustomers.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    spCustomers.showDropDown();
                }
            }
        });

        String[] arrayStatus = getResources().getStringArray(R.array.event_status);
        ArrayAdapter<String> arrayAdapterStatus = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                arrayStatus );
        spStatus.setAdapter(arrayAdapterStatus);
        final ImageButton btndate=(ImageButton)view.findViewById(R.id.btnDate);

        spStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectStatus=parentView.getItemAtPosition(position).toString();
                if(!selectStatus.equals("Dia")){
                    txtdate=(TextView) view.findViewById(R.id.txtDate);
                    txtdate.setText("");
                    btndate.setClickable(false);
                }else {
                    btndate.setClickable(true);
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
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });



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
                FragmentManager fm = getActivity().getFragmentManager();
                dpd.show(fm, "Datepickerdialog");
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
                refreshCalendar(true);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(getActivity(), InsertEventCalendar.class);
                startActivity(intent);
            }
        });



        return view;

    }


    @Override
    public void onResume() {
        refreshCalendar(false);
        threadSend();
        super.onResume();
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    private void refreshCalendar(boolean filter){
        //Call to server to grab list of student records. this is a asyn
        try {
            if (conecNetWork()) {
                if(filter) {
                    dialog = ProgressDialog.show(getActivity(), "",
                            "Actualizando calendario. Por favor espere...", true);
                }

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
                manager=new DataBaseManager(getActivity());
                ArrayList<Customer> lstPersons=manager.getAllPersonsVector();
                Customer client=new Customer("","");
                if (!spCustomers.getText().toString().isEmpty()){
                    for (Customer customer:lstPersons) {
                        if(customer.getName().toString().equals(spCustomers.getText().toString())) {
                            client=customer;
                        }
                    }
                    if(client.getId().isEmpty()) {
                        showAlertDialog(getActivity(),"Validación","El cliente elegido no es valido.",true);
                        dialog.cancel();
                        return;
                    }
                }
                String clienID = null;
                if (client.getId().equals("") || client.getId().equals("0")) {
                    clienID = null;
                } else {
                    clienID = client.getId().toString();
                }

                String id=null;
                spStatus = (Spinner) getActivity().findViewById(R.id.spStatus);
                String selectStatus=spStatus.getSelectedItem().toString();
                restService = new RestService();
                restService.getService().getEvents(type,date,clienID,id,selectStatus,new Callback<List<Event>>() {
                    @Override
                    public void success(List<Event> events, Response response) {
                        mEvents = new ArrayList<>();
                        List<Event> lstEvents = events;


                        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));



                        for(Event event:lstEvents) {
                            String name="";
                            if(event.FullName.length() > 29) {
                                name =event.FullName.substring(0, 29) + "...";
                            }else {
                                name =event.FullName;
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
                                strStartDate=event.strStartDate.split(" ")[0];
                                strStrartHour=event.strStartDate.split(" ")[1] + " " +event.strStartDate.split(" ")[2];

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Date endDate=new Date();
                            String strEndDate="";
                            String strEndHour="";
                            try {
                                strEndDate=event.EndDate.split("T")[0];
                                strEndHour=event.strEndDate.split(" ")[1] + " "+event.strEndDate.split(" ")[2] ;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            EventAdapter pa=new EventAdapter(event.Id,event.TypeEvent,event.Description,event.Customer,strStartDate
                                    ,strEndDate,event.Title,event.Executed,name.toUpperCase(),strStrartHour,strEndHour,event.sms);
                            mEvents.add(pa);

                        }
                        mAdapter = new CalendarAdapter(getActivity(), mEvents);
                        mRecyclerView.setAdapter(mAdapter);

                        mAdapter.setOnItemClickListener(new CalendarAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(String IdEvent, int position) {

                                String message = IdEvent;
                                Intent intent;
                                intent = new Intent(getActivity(), EditEventCalendar.class);
                                intent.putExtra(AlarmClock.EXTRA_MESSAGE, message);
                                startActivity(intent);

                            }
                        });
                        if (dialog != null) {
                            dialog.cancel();
                        }

                        spStatus = (Spinner) getActivity().findViewById(R.id.spStatus);
                        String selectStatus=spStatus.getSelectedItem().toString();
                        String textFilter=selectStatus + " - ";
                        String dateFilter=txtdate.getText().toString();
                        if (!dateFilter.equals(""))
                        {
                            textFilter=textFilter + "Fecha: "+dateFilter;
                        }

                        String type=spTypeEvent.getSelectedItem().toString();
                        if (type.equals("Tipo"))
                        {
                            type=null;
                        }else {

                            textFilter=textFilter + " - Tipo: "+type;
                        }

                        manager=new DataBaseManager(getActivity());
                        ArrayList<Customer> lstPersons=manager.getAllPersonsVector();
                        Customer client=new Customer("","");
                        if (!spCustomers.getText().toString().isEmpty()){
                            for (Customer customer:lstPersons) {
                                if(customer.getName().toString().equals(spCustomers.getText().toString())) {
                                    client=customer;
                                }
                            }
                            if(client.getId().isEmpty()) {
                                showAlertDialog(getActivity(),"Validación","El cliente elegido no es valido.",true);
                                dialog.cancel();
                                return;
                            }
                        }
                        String clienID=null;
                        if ( client.getId().equals("0") ||  client.getId().equals(""))
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



    public void threadSend()
    {
        boolean incall=inCall();
        while (incall)
        {
            incall=inCall();

        }
        manager=new DataBaseManager(getContext());

        Runnable  runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    /*
                    try {
                        SMSthread.sleep(20000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    */


                    java.util.Date getDate= new Date();
                    java.util.Calendar cal = java.util.Calendar.getInstance();
                    cal.setTime(getDate);
                    cal.add(java.util.Calendar.DATE,1);
                    DateFormat outputf = new SimpleDateFormat("yyyy-MM-dd");

                    String dateEvent=outputf.format(cal.getTime());

                    R2ClienteService rService = R2ClienteService.retrofit.create(R2ClienteService.class);
                    Call<List<EventSMS>> call = rService.GetEventsSMS(dateEvent);
                    call.enqueue(new retrofit2.Callback<List<EventSMS>>() {
                        @Override
                        public void onResponse(Call<List<EventSMS>> call, retrofit2.Response<List<EventSMS>> response) {
                            List<EventSMS>  lstEvents= response.body();

                            for (EventSMS eventSMS:lstEvents)
                            {
                                TelephonyManager telephony = (TelephonyManager) getContext().getSystemService(getContext().TELEPHONY_SERVICE);

                                int state=telephony.getCallState();
                                final boolean[] incall = new boolean[1];
                                switch (state) {
                                    case TelephonyManager.CALL_STATE_IDLE:
                                        incall[0] = false;
                                        break;
                                    case TelephonyManager.CALL_STATE_OFFHOOK:
                                        incall[0] = true;
                                        break;
                                    case TelephonyManager.CALL_STATE_RINGING:
                                        incall[0] = true;
                                        break;
                                }
                                while (incall[0]) {
                                    state=telephony.getCallState();
                                    switch (state) {
                                        case TelephonyManager.CALL_STATE_IDLE:
                                            incall[0] = false;
                                            break;
                                        case TelephonyManager.CALL_STATE_OFFHOOK:
                                            incall[0] = true;
                                            break;
                                        case TelephonyManager.CALL_STATE_RINGING:
                                            incall[0] = true;
                                            break;
                                    }
                                }

                                //enviar mensaje
                                String strPhone = eventSMS.cell;
                                String strMessage =eventSMS.message;
                                SmsManager sms = SmsManager.getDefault();
                                ArrayList messageParts = sms.divideMessage(strMessage);

                                sms.sendMultipartTextMessage(strPhone, null, messageParts, null, null);
                                String id= String.valueOf(eventSMS.IdEvent);

                                R2ClienteService rService = R2ClienteService.retrofit.create(R2ClienteService.class);
                                Call<Boolean> calluPDATE = rService.UpdateSendSMS(id);
                                calluPDATE.enqueue(new retrofit2.Callback<Boolean>() {
                                    @Override
                                    public void onResponse(Call<Boolean> call, retrofit2.Response<Boolean> response) {

                                    }

                                    @Override
                                    public void onFailure(Call<Boolean> call, Throwable t) {

                                    }
                                });


                                try {
                                    SMSthread.sleep(20000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            if(SMSthread!=null) {
                                SMSthread.interrupt();
                                refreshCalendar(false);
                            }

                        }

                        @Override
                        public void onFailure(Call<List<EventSMS>> call, Throwable t) {
                            Toast.makeText(getActivity(), t.getMessage().toString(), Toast.LENGTH_LONG).show();
                        }

                    });

                } catch (Exception e) {
                    e.getLocalizedMessage();
                }
            }
        };

        SMSthread = new Thread(runnable);
        SMSthread.start();


    }



    private boolean inCall()
    {
        final boolean[] incall = new boolean[1];
        listener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                switch (state) {
                    case TelephonyManager.CALL_STATE_IDLE:
                        incall[0] =false;
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        incall[0] =true;
                        break;
                    case TelephonyManager.CALL_STATE_RINGING:
                        incall[0] =true;
                        break;
                }

            }};
        return incall[0];
    }


}
