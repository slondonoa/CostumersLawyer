package com.costumers.lawyer.fragments;

import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.costumers.lawyer.R;
import com.costumers.lawyer.data.DataBaseManager;
import com.costumers.lawyer.entities.Customer;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import java.util.*;
import java.util.Calendar;

public class InsertEventeFragment extends android.support.v4.app.Fragment {

    private Toolbar toolbar;
    TextView txtStart,txtStartTime,txtEndDate,txtEndTime;
    View view;
    private Spinner spTypeEvent, spCustomers;
    private DataBaseManager manager;

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

        return view;
    }


}
