package com.costumers.lawyer.fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.costumers.lawyer.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.*;

public class InsertEventeFragment extends Fragment {

    private Toolbar toolbar;
    TextView txtStart,txtStartTime;
    View view;

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
        view = inflater.inflate(R.layout.fragment_edit_costumer, container, false);



        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("Insertar Nuevo Evento");
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        txtStart=(TextView)view.findViewById(R.id.txtStarDate) ;
        txtStartTime=(TextView)view.findViewById(R.id.txtStarTimeDate) ;

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
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });

        return view;
    }


}
