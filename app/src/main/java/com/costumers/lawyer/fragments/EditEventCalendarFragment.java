package com.costumers.lawyer.fragments;


import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.costumers.lawyer.R;

import com.costumers.lawyer.R;
import com.costumers.lawyer.activity.EditEventCalendar;
import com.costumers.lawyer.adapter.CalendarAdapter;
import com.costumers.lawyer.data.DataBaseManager;
import com.costumers.lawyer.entities.Customer;
import com.costumers.lawyer.entities.Event;
import com.costumers.lawyer.entities.EventAdapter;
import com.costumers.lawyer.service.RestService;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditEventCalendarFragment extends Fragment {
    static String idEvent="";
    View view;
    private Toolbar toolbar;
    TextView txtStart,txtStartTime,txtEndDate,txtEndTime;
    EditText txtTitle,txtDescription,txtidEvent;
    TextInputLayout textInputLayoutTitle;
    private Spinner spTypeEvent;
    private DataBaseManager manager;
    ProgressDialog dialog =null;
    RestService restService;
    AutoCompleteTextView spCustomers;
    public static EditEventCalendarFragment newInstance(String id) {
        idEvent=id;
        return new EditEventCalendarFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_edit_event_calendar, container, false);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("Editar evento");
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);


        txtStart=(TextView)view.findViewById(R.id.txtStarDate) ;
        txtStartTime=(TextView)view.findViewById(R.id.txtStarTimeDate) ;
        spTypeEvent = (Spinner) view.findViewById(R.id.spTypeEvent);
        spCustomers = (AutoCompleteTextView) view.findViewById(R.id.spCustomers);
        txtTitle=(EditText) view.findViewById(R.id.txtTitle);
        textInputLayoutTitle=(TextInputLayout) view.findViewById(R.id.textInputLayoutTitle);
        txtDescription=(EditText) view.findViewById(R.id.txtDescription);
        txtidEvent=(EditText) view.findViewById(R.id.txtidEvent);

        txtidEvent.setText(idEvent);

        ImageButton btnstart=(ImageButton)view.findViewById(R.id.btnSatartDate);
        btnstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                java.util.Calendar now = java.util.Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                int _monthOfYear=monthOfYear+1;
                                String month= String.valueOf(_monthOfYear);
                                if (month.length()==1)
                                {
                                    month="0"+month;
                                }
                                String day= String.valueOf(dayOfMonth);
                                if (day.length()==1)
                                {
                                    day="0"+day;
                                }
                                String date = year+"/"+ month +"/"+ day ;
                                txtStart.setText(date);
                            }
                        },
                        now.get(java.util.Calendar.YEAR),
                        now.get(java.util.Calendar.MONTH),
                        now.get(java.util.Calendar.DAY_OF_MONTH)
                );
                dpd.setThemeDark(false);
                dpd.vibrate(false);
                dpd.setTitle("Fecha de inicio");
                dpd.setAccentColor(Color.parseColor("#125688"));
                FragmentManager fm = getActivity().getFragmentManager();
                dpd.show(fm, "Datepickerdialog");
            }
        });

        ImageButton btnstarttime=(ImageButton)view.findViewById(R.id.btnSatarTimetDate);

        btnstarttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                java.util.Calendar now = java.util.Calendar.getInstance();
                com.wdullaer.materialdatetimepicker.time.TimePickerDialog tpd = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(
                        new com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(com.wdullaer.materialdatetimepicker.time.TimePickerDialog view, int hourOfDay, int minute, int second) {
                                String hourString = hourOfDay < 10 ? "0"+hourOfDay : ""+hourOfDay;
                                String minuteString = minute < 10 ? "0"+minute : ""+minute;
                                String time =" "+hourString+":"+minuteString;
                                txtStartTime.setText(time);
                            }
                        },
                        now.get(java.util.Calendar.HOUR_OF_DAY),
                        now.get(java.util.Calendar.MINUTE),
                        true

                );
                tpd.setThemeDark(false);
                tpd.vibrate(false);
                tpd.enableSeconds(false);
                tpd.setAccentColor(Color.parseColor("#125688"));
                tpd.setTitle("TimePicker Title");
                FragmentManager fm = getActivity().getFragmentManager();
                tpd.show(fm, "Timepickerdialog");
            }
        });




        txtEndDate=(TextView)view.findViewById(R.id.txtEndDate) ;
        txtEndTime=(TextView)view.findViewById(R.id.txtEndTime) ;

        ImageButton btnEndDate=(ImageButton)view.findViewById(R.id.btnEndDate);
        btnEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                java.util.Calendar now = java.util.Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                int _monthOfYear=monthOfYear+1;
                                String month= String.valueOf(_monthOfYear);
                                if (month.length()==1)
                                {
                                    month="0"+month;
                                }
                                String day= String.valueOf(dayOfMonth);
                                if (day.length()==1)
                                {
                                    day="0"+day;
                                }
                                String date = year+"/"+ month +"/"+ day ;
                                txtEndDate.setText(date);
                            }
                        },
                        now.get(java.util.Calendar.YEAR),
                        now.get(java.util.Calendar.MONTH),
                        now.get(java.util.Calendar.DAY_OF_MONTH)
                );
                dpd.setThemeDark(false);
                dpd.vibrate(false);
                dpd.setTitle("Fecha de inicio");
                dpd.setAccentColor(Color.parseColor("#125688"));
                FragmentManager fm = getActivity().getFragmentManager();
                dpd.show(fm, "Datepickerdialog");
            }
        });

        ImageButton btnEndTimeDate=(ImageButton)view.findViewById(R.id.btnEndTimeDate);

        btnEndTimeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                java.util.Calendar now = java.util.Calendar.getInstance();
                com.wdullaer.materialdatetimepicker.time.TimePickerDialog tpd = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(
                        new com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(com.wdullaer.materialdatetimepicker.time.TimePickerDialog view, int hourOfDay, int minute, int second) {
                                String hourString = hourOfDay < 10 ? "0"+hourOfDay : ""+hourOfDay;
                                String minuteString = minute < 10 ? "0"+minute : ""+minute;
                                String time =" "+hourString+":"+minuteString;
                                txtEndTime.setText(time);
                            }
                        },
                        now.get(java.util.Calendar.HOUR_OF_DAY),
                        now.get(java.util.Calendar.MINUTE),
                        true

                );
                tpd.setThemeDark(false);
                tpd.vibrate(false);
                tpd.enableSeconds(false);
                tpd.setAccentColor(Color.parseColor("#125688"));
                tpd.setTitle("TimePicker Title");
                FragmentManager fm = getActivity().getFragmentManager();
                tpd.show(fm, "Timepickerdialog");
            }
        });



        getEventById(idEvent);



        Button btnsave=(Button)view.findViewById(R.id.btnsave);

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (conecNetWork()) {
                        dialog = ProgressDialog.show(getActivity(), "",
                                "Editando evento. Por favor espere...", true);
                        if (submitForm()) {
                            String startDate = txtStart.getText().toString();
                            String startTime = txtStartTime.getText().toString();
                            String start=startDate+startTime;
                            String EndDate = txtEndDate.getText().toString();
                            String EndTime = txtEndTime.getText().toString();
                            String end=EndDate+EndTime;
                            String type = spTypeEvent.getSelectedItem().toString();
                            if (type.equals("Tipo")) {
                                type = null;
                            } else {
                                switch (type) {
                                    case "Cita":
                                        type = "1";
                                        break;
                                    case "Audiencia":
                                        type = "2";
                                        break;
                                    case "Vencimiento":
                                        type = "3";
                                        break;
                                    case "Otros":
                                        type = "4";
                                        break;
                                }


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
                            String title = txtTitle.getText().toString();
                            String description = txtDescription.getText().toString();

                            String id=txtidEvent.getText().toString();


                            restService = new RestService();

                            restService.getService().EditEvent(type,start,end,clienID,description,title,id,new Callback<Boolean>() {
                                @Override
                                public void success(Boolean events, Response response) {
                                    Toast.makeText(getActivity(), "Calendario actualizado", Toast.LENGTH_LONG).show();

                                    getActivity().finish();

                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    Toast.makeText(getActivity(), error.getMessage().toString(), Toast.LENGTH_LONG).show();
                                }
                            });

                        }else {
                            dialog.cancel();
                        }
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
        });

        Button btnDelete=(Button)view.findViewById(R.id.btnDelete);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Eliminar")
                        .setMessage("Desea eliminar este evento?")
                        .setPositiveButton("Si",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(final DialogInterface dialogM, int which) {

                                        String id=txtidEvent.getText().toString();
                                        restService = new RestService();

                                        restService.getService().DeleteCalendarById(id,new Callback<Boolean>() {
                                            @Override
                                            public void success(Boolean events, Response response) {
                                                Toast.makeText(getActivity(), "Evento eliminado correctamente.", Toast.LENGTH_LONG).show();
                                                getActivity().finish();
                                            }

                                            @Override
                                            public void failure(RetrofitError error) {
                                                Toast.makeText(getActivity(), error.getMessage().toString(), Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                })
                        .setNegativeButton("No",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                builder.create();
                builder.show();
            }
        });



        return view;
    }


    private boolean submitForm() {
        boolean validate=false;
        if (validateTitle()) {
            validate=true;
        }else {
            validate = false;
        }
        if (!spTypeEvent.getSelectedItem().toString().equals("Tipo")) {
            validate=true;
        }else {
            validate = false;
            showAlertDialog(getActivity(),"Validación","Debe ingresar tipo",true);
        }
        if (txtEndTime.getText().toString().trim().isEmpty()) {
            validate=false;
            showAlertDialog(getActivity(),"Validación","Debe ingresar la hora de fin",true);
        }else {
            validate = true;
        }


        if (txtEndDate.getText().toString().trim().isEmpty()) {
            validate=false;
            showAlertDialog(getActivity(),"Validación","Debe ingresar la fecha de fin",true);
        }else {
            validate = true;
        }


        if (txtStartTime.getText().toString().trim().isEmpty()) {
            validate=false;
            showAlertDialog(getActivity(),"Validación","Debe ingresar la hora de inicio",true);
        }else {
            validate = true;
        }

        if (txtStart.getText().toString().trim().isEmpty()) {
            validate=false;
            showAlertDialog(getActivity(),"Validación","Debe ingresar la fecha de inicio",true);
        }else {
            validate = true;
        }


        if (validate) {
            //Format of the date defined in the input String
            DateFormat df = new SimpleDateFormat("yyyy/MM/dd hh:mm");
            //Desired format: 24 hour format: Change the pattern as per the need
            DateFormat outputformat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
            Date dateStart = null;
            Date dateEnd = null;
            String output = null;
            try {
                String startDate = txtStart.getText().toString();
                String startTime = txtStartTime.getText().toString();
                String start=startDate+startTime;
                String EndDate = txtEndDate.getText().toString();
                String EndTime = txtEndTime.getText().toString();
                String end=EndDate+EndTime;

                //Converting the input String to Date
                dateStart = df.parse(start);
                dateEnd=df.parse(end);
                long diff = dateEnd.getTime() - dateStart.getTime();//as given
                long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
                if(minutes < 0) {
                    validate=false;
                    showAlertDialog(getActivity(),"Validación","La fecha de inicio debe ser menor a la de fin",true);

                }

            } catch (ParseException pe) {
                pe.printStackTrace();
            }
        }


        return validate;
    }


    private boolean validateTitle() {
        boolean validate=false;
        if (txtTitle.getText().toString().trim().isEmpty()) {
            textInputLayoutTitle.setError(getString(R.string.require));
        } else {
            textInputLayoutTitle.setErrorEnabled(false);
            validate= true;
        }
        return  validate;
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

    private void getEventById(String id){
        //Call to server to grab list of student records. this is a asyn
        try {
            if (conecNetWork()) {
                dialog = ProgressDialog.show(getActivity(), "",
                        "Consultando evento. Por favor espere...", true);
                restService = new RestService();

                restService.getService().getEvents(null,null,null,id,null,new Callback<List<Event>>() {
                    @Override
                    public void success(List<Event> events, Response response) {

                        List<Event> lstEvents = events;

                        for (Event event:lstEvents) {

                            txtTitle.setText(event.Title);
                            txtDescription.setText(event.Description);

                            DateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
                            //Desired format: 24 hour format: Change the pattern as per the need
                            DateFormat outputformat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                            Date dateEventStart = null;
                            Date dataEventEnd=null;
                            String output = null;
                            try{
                                //fecha de inicio
                                dateEventStart= df.parse(event.strStartDate);
                                output = outputformat.format(dateEventStart);
                                txtStart.setText(output.split(" ")[0].toString());
                                txtStartTime.setText(" "+output.split(" ")[1].toString());

                                //fecha de fin
                                dataEventEnd= df.parse(event.strEndDate);
                                output = outputformat.format(dataEventEnd);
                                txtEndDate.setText(output.split(" ")[0].toString());
                                txtEndTime.setText(" "+output.split(" ")[1].toString());

                            }catch(ParseException pe){
                                pe.printStackTrace();
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

                            manager=new DataBaseManager(getActivity());
                            ArrayList<Customer> lstPersons=manager.getAllPersonsVector();


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

                            if (!event.Customer.isEmpty()){
                                for (Customer customer:lstPersons) {
                                    if(customer.getId().toString().equals(event.Customer)) {
                                        spCustomers.setText(customer.getName());
                                    }
                                }
                            }

                            String[] arrayListTypeEvent = getResources().getStringArray(R.array.type_event);
                            ArrayAdapter<String> arrayAdapterTypeEvent = new ArrayAdapter<String>(
                                    getActivity(),
                                    android.R.layout.simple_list_item_1,
                                    arrayListTypeEvent );
                            spTypeEvent.setAdapter(arrayAdapterTypeEvent);

                            spTypeEvent.setSelection(arrayAdapterTypeEvent.getPosition(event.TypeEvent));




                        }

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





}
