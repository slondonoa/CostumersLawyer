package com.costumers.lawyer.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.costumers.lawyer.R;
import com.costumers.lawyer.activity.DetailCostumer;
import com.costumers.lawyer.data.DataBaseManager;
import com.costumers.lawyer.entities.Persons;
import com.costumers.lawyer.service.RestService;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class RegisterCostumer extends Fragment  {

    private DataBaseManager manager;
    private TextView txtDocument, txtName, txtLastName, txtCell,txtCell2, txtPhone, txtobservations,txtlastcontac,txtLimitDate, txtEmail, txtStart;
    private Spinner spProcessed, spCostumersatatus, spProcessstatus,spSource;
    private TextInputLayout textInputLayoutDocument,textInputLayoutName,textInputLayoutLastName;
    ProgressDialog dialog =null;

    View view;
    RestService restService;
    public RegisterCostumer() {
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
        //




        view = inflater.inflate(R.layout.fragment_register_costumer, container, false);

        txtDocument =(TextView) view.findViewById(R.id.txtDocument);
        txtName =(TextView) view.findViewById(R.id.txtName);
        txtLastName =(TextView) view.findViewById(R.id.txtLastName);
        txtCell =(TextView) view.findViewById(R.id.txtCell);
        txtCell2 =(TextView) view.findViewById(R.id.txtCell2);
        txtPhone =(TextView) view.findViewById(R.id.txtPhone);
        txtEmail =(TextView) view.findViewById(R.id.txtEmail);
        txtobservations =(TextView) view.findViewById(R.id.txtObservations);
        spProcessstatus = (Spinner) view.findViewById(R.id.spprocessstatus);
        spCostumersatatus = (Spinner) view.findViewById(R.id.spcostumersatatus);
        spProcessed = (Spinner) view.findViewById(R.id.spProcessed);
        spSource = (Spinner) view.findViewById(R.id.spSource);
        txtlastcontac=(TextView)view.findViewById(R.id.txtLastcontac);
        txtLimitDate=(TextView) view.findViewById(R.id.txtLimitprocess);
        textInputLayoutDocument=(TextInputLayout) view.findViewById(R.id.textInputLayoutDocument);
        textInputLayoutName=(TextInputLayout) view.findViewById(R.id.textInputLayoutName);
        textInputLayoutLastName=(TextInputLayout) view.findViewById(R.id.textInputLayoutLastName);
        txtStart=(TextView)view.findViewById(R.id.txtstart);

        final Button btnsave=(Button)view.findViewById(R.id.btnsave);
        txtDocument.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (!s.equals("")) {
                    List<Persons> PersonsList = new ArrayList<Persons>();
                    manager = new DataBaseManager(getActivity());
                    manager.Open(getActivity());
                    PersonsList = manager.getPerson(s.toString());
                    manager.Close(getActivity());
                    if (PersonsList.size() > 0) {
                        Toast.makeText(getActivity(), "Cliente ya registrado.", Toast.LENGTH_SHORT).show();
                        btnsave.setEnabled(false);
                    }else
                    {
                        btnsave.setEnabled(true);
                    }
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {

            }

        });

        ImageButton btnlast=(ImageButton)view.findViewById(R.id.btnlastDate);
        btnlast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
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
                                txtlastcontac.setText(date);
                            }
                        },
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setThemeDark(false);
                dpd.vibrate(false);
                dpd.setTitle("Ultimo contacto");
                dpd.setAccentColor(Color.parseColor("#125688"));
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });


        ImageButton btnlimit=(ImageButton)view.findViewById(R.id.btnLimitprocess);
        btnlimit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
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
                                txtLimitDate.setText(date);
                            }
                        },
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setThemeDark(false);
                dpd.vibrate(false);
                dpd.setTitle("Fecha limite");
                dpd.setAccentColor(Color.parseColor("#125688"));
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });


        ImageButton btnstart=(ImageButton)view.findViewById(R.id.btnstart);
        btnstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
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
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setThemeDark(false);
                dpd.vibrate(false);
                dpd.setTitle("Fecha de inicio");
                dpd.setAccentColor(Color.parseColor("#125688"));
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });


        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = ProgressDialog.show(getActivity(), "",
                        "Registrando cliente. Por favor espere...", true);
                if (submitForm()) {
                    String strDocument = txtDocument.getText().toString().trim();
                    String strName = txtName.getText().toString().trim();
                    String strLatName = txtLastName.getText().toString().trim();
                    String strCell = txtCell.getText().toString().trim();
                    String strCell2 = txtCell2.getText().toString().trim();
                    String strPhone = txtPhone.getText().toString().trim();
                    String strEmail = txtEmail.getText().toString().trim();
                    String strObservations = txtobservations.getText().toString().trim();
                    String strProcessstatus = spProcessstatus.getSelectedItem().toString().trim();
                    String strCostumerStatus = spCostumersatatus.getSelectedItem().toString().trim();
                    String strProcessed = spProcessed.getSelectedItem().toString().trim();
                    String strSource = spSource.getSelectedItem().toString().trim();
                    String strLimitDate = txtLimitDate.getText().toString().trim();
                    String strLatContact = txtlastcontac.getText().toString().trim();
                    String strStart = txtStart.getText().toString().trim();

                    final Persons persons = new Persons();
                    persons.Document = strDocument;
                    persons.Name = strName;
                    persons.LastName = strLatName;
                    persons.cell1 = strCell;
                    persons.cell2 = strCell2;
                    persons.phone = strPhone;
                    persons.maial1=strEmail;
                    persons.observations = strObservations;
                    if ( spProcessstatus.getSelectedItemPosition() > 0) {
                        persons.ProcessStatus = strProcessstatus;
                    }
                    if ( spCostumersatatus.getSelectedItemPosition() > 0) {
                        persons.clientstatus = strCostumerStatus;
                    }
                    if ( spProcessed.getSelectedItemPosition() > 0) {
                        persons.processed = strProcessed;
                    }
                    if ( spSource.getSelectedItemPosition() > 0) {
                        persons.source = strSource;
                    }

                    persons.LimitDateProcessStatus = strLimitDate;
                    persons.lastContact = strLatContact;
                    persons.start=strStart;
                    restService = new RestService();
                    restService.getService().addCostumer(persons, new Callback<Integer>() {
                        @Override
                        public void success(Integer idperson, Response response) {
                            List<Persons> PersonsList = new ArrayList<Persons>();
                            persons.IdPerson = idperson.toString().trim();
                            PersonsList.add(persons);
                            manager = new DataBaseManager(getActivity());
                            manager.Open(getActivity());
                            manager.InsertCostumer(PersonsList);
                            manager.Close(getActivity());
                            if (dialog != null) {
                                dialog.cancel();
                            }
                            txtDocument.setText("");
                            txtName.setText("");
                            txtLastName.setText("");
                            txtCell.setText("");
                            txtCell2.setText("");
                            txtPhone.setText("");
                            txtEmail.setText("");
                            txtobservations.setText("");
                            spProcessstatus.setSelection(0);
                            spCostumersatatus.setSelection(0);
                            spProcessed.setSelection(0);
                            spSource.setSelection(0);
                            txtlastcontac.setText("");
                            txtLimitDate.setText("");
                            textInputLayoutDocument.setErrorEnabled(false);
                            textInputLayoutName.setErrorEnabled(false);
                            textInputLayoutLastName.setErrorEnabled(false);

                            String message = idperson.toString();
                            Intent intent;
                            intent = new Intent(getActivity(), DetailCostumer.class);
                            intent.putExtra(AlarmClock.EXTRA_MESSAGE, message);
                            startActivity(intent);

                            Toast.makeText(getActivity(), "Cliente registrado correctamente.", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            if (dialog != null) {
                                dialog.cancel();
                            }
                            Toast.makeText(getActivity(), error.getMessage().toString(), Toast.LENGTH_LONG).show();

                        }
                    });
                }else {
                    if (dialog != null) {
                        dialog.cancel();
                    }
                    return;
                }
            }
        });
        return view;
    }

    private boolean submitForm() {
        boolean validate=false;
        if (valiateDocument()) {
            validate=true;
        }else {
            validate = false;
        }
        if (validateName()) {
            validate=true;
        }else {
            validate = false;
        }
        if (validateLastName()) {
            validate=true;
        }else {
            validate = false;
        }
        return validate;
    }

    private boolean valiateDocument() {
        boolean validate=false;
        if (txtDocument.getText().toString().trim().isEmpty()) {
            textInputLayoutDocument.setError(getString(R.string.err_msg_document));
        } else {
            textInputLayoutDocument.setErrorEnabled(false);
            validate =true;
        }
        return  validate;
    }

    private boolean validateName() {
        boolean validate=false;
        if (txtName.getText().toString().trim().isEmpty()) {
            textInputLayoutName.setError(getString(R.string.err_msg_name));
        } else {
            textInputLayoutName.setErrorEnabled(false);
            validate= true;
        }
        return  validate;
    }

    private boolean validateLastName() {
        boolean validate=false;
        if (txtLastName.getText().toString().trim().isEmpty()) {
            textInputLayoutLastName.setError(getString(R.string.err_msg_lastname));
        } else {
            textInputLayoutLastName.setErrorEnabled(false);
            validate= true;
        }
        return validate;
    }

    //private void requestFocus(View view) {
    //    if (view.requestFocus()) {
    //        getView().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    //    }
    //}

}
