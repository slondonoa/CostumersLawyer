package com.costumers.lawyer.fragments;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.costumers.lawyer.R;
import com.costumers.lawyer.adapter.CalendarAdapter;
import com.costumers.lawyer.data.DataBaseManager;
import com.costumers.lawyer.entities.Customer;
import com.costumers.lawyer.entities.Event;
import com.costumers.lawyer.entities.EventAdapter;
import com.costumers.lawyer.service.RestService;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.sql.DataTruncation;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class InsertEventeFragment extends android.support.v4.app.Fragment {

    private Toolbar toolbar;
    TextView txtStart,txtStartTime,txtEndDate,txtEndTime;
    EditText txtTitle,txtDescription;
    TextInputLayout textInputLayoutTitle;
    View view;
    private Spinner spTypeEvent, spCustomers;
    private DataBaseManager manager;
    ProgressDialog dialog =null;
    RestService restService;

    public static InsertEventeFragment newInstance() {
        return new InsertEventeFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_insert_evente, container, false);



        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("Insertar Nuevo Evento");
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        txtStart=(TextView)view.findViewById(R.id.txtStarDate) ;
        txtStartTime=(TextView)view.findViewById(R.id.txtStarTimeDate) ;
        spTypeEvent = (Spinner) view.findViewById(R.id.spTypeEvent);
        spCustomers = (Spinner) view.findViewById(R.id.spCustomers);
        txtTitle=(EditText) view.findViewById(R.id.txtTitle);
        textInputLayoutTitle=(TextInputLayout) view.findViewById(R.id.textInputLayoutTitle);
        txtDescription=(EditText) view.findViewById(R.id.txtDescription);

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
                Calendar now = Calendar.getInstance();
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
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
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
                Calendar now = Calendar.getInstance();
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
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
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

        manager=new DataBaseManager(getActivity());
        ArrayList<Customer> lstPersons=manager.getAllPersonsVector();


        ArrayAdapter<Customer> arrayAdapterCustomers = new ArrayAdapter<Customer>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                lstPersons );

        spCustomers.setAdapter(arrayAdapterCustomers);

        String[] arrayListprocessstatus = getResources().getStringArray(R.array.type_event);
        ArrayAdapter<String> arrayAdapterTypeEvent = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                arrayListprocessstatus );
        spTypeEvent.setAdapter(arrayAdapterTypeEvent);



        Button btnsave=(Button)view.findViewById(R.id.btnsave);

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (conecNetWork()) {
                        dialog = ProgressDialog.show(getActivity(), "",
                                "Insertando evento. Por favor espere...", true);
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
                            Customer client = (Customer) spCustomers.getSelectedItem();
                            String clienID = null;
                            if (client.getId().equals("0")) {
                                clienID = null;
                            } else {
                                clienID = client.getId().toString();
                            }
                            String title = txtTitle.getText().toString();
                            String description = txtDescription.getText().toString();

                            restService = new RestService();

                            restService.getService().insertEvents(type,start,end,clienID,description,title,new Callback<Integer>() {
                                @Override
                                public void success(Integer events, Response response) {
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


}
