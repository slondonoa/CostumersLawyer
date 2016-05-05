package com.costumers.lawyer.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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


public class EditCostumerData extends android.support.v4.app.Fragment {

    private Toolbar toolbar;
    private DataBaseManager manager;
    RestService restService;
    ProgressDialog dialog =null;
    private int _IdPerson=0;
    private TextView txtDocument, txtName, txtLastName, txtCell, txtPhone, txtobservations,txtlastcontac,txtLimitDate, txtEmail;
    private TextInputLayout textInputLayoutName,textInputLayoutLastName;
    private Spinner spProcessed, spCostumersatatus, spProcessstatus;
    View view;
    static String idperson="";
    String cell2="";
    String email2="";
    public static EditCostumerData newInstance(String id) {
        idperson=id;
        return new EditCostumerData();
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_costumer, container, false);

        restService = new RestService();
        manager=new DataBaseManager(getActivity());
        super.onCreate(savedInstanceState);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("Editar cliente");
        //set toolbar appearance
        //for crate home button
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        //activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);


        textInputLayoutName=(TextInputLayout) view.findViewById(R.id.textInputLayoutName);
        textInputLayoutLastName=(TextInputLayout) view.findViewById(R.id.textInputLayoutLastName);

        txtDocument =(TextView) view.findViewById(R.id.txtDocument);
        txtName =(TextView) view.findViewById(R.id.txtName);
        txtLastName =(TextView) view.findViewById(R.id.txtLastName);
        txtCell =(TextView) view.findViewById(R.id.txtCell);
        txtPhone =(TextView) view.findViewById(R.id.txtPhone);
        txtEmail =(TextView) view.findViewById(R.id.txtEmail);
        txtobservations =(TextView) view.findViewById(R.id.txtObservations);
        spProcessstatus = (Spinner) view.findViewById(R.id.spprocessstatus);
        spCostumersatatus = (Spinner) view.findViewById(R.id.spcostumersatatus);
        spProcessed = (Spinner) view.findViewById(R.id.spProcessed);
        txtlastcontac=(TextView) view.findViewById(R.id.txtLastcontac);
        txtLimitDate=(TextView) view.findViewById(R.id.txtLimitprocess);

        String[] arrayListprocessstatus = getResources().getStringArray(R.array.processstatus_array);
        ArrayAdapter<String> arrayAdapterprocessstatus = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                arrayListprocessstatus );
        spProcessstatus.setAdapter(arrayAdapterprocessstatus);

        String[] arrayListcostumerstatus = getResources().getStringArray(R.array.costumerstatus_array);
        ArrayAdapter<String> arrayAdaptercostumerstatus = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                arrayListcostumerstatus );
        spCostumersatatus.setAdapter(arrayAdaptercostumerstatus);

        String[] arrayListprocessed = getResources().getStringArray(R.array.processed_array);
        ArrayAdapter<String> arrayAdapterprocessed= new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                arrayListprocessed );
        spProcessed.setAdapter(arrayAdapterprocessed);


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
                                String month = String.valueOf(_monthOfYear);
                                if (month.length() == 1) {
                                    month = "0" + month;
                                }
                                String day = String.valueOf(dayOfMonth);
                                if (day.length() == 1) {
                                    day = "0" + day;
                                }
                                String date = year + "/" + month + "/" + day;
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
                                String month = String.valueOf(_monthOfYear);
                                if (month.length() == 1) {
                                    month = "0" + month;
                                }
                                String day = String.valueOf(dayOfMonth);
                                if (day.length() == 1) {
                                    day = "0" + day;
                                }
                                String date = year + "/" + month + "/" + day;
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


        Button btnsave=(Button)view.findViewById(R.id.btnsave);
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = ProgressDialog.show(getActivity(), "",
                        "Actualizando cliente. Por favor espere...", true);
                if (submitForm()) {
                    String strDocument = txtDocument.getText().toString();
                    String strName = txtName.getText().toString();
                    String strLatName = txtLastName.getText().toString();
                    String strCell = txtCell.getText().toString();
                    String strPhone = txtPhone.getText().toString();
                    String strEmail = txtEmail.getText().toString();
                    String strObservations = txtobservations.getText().toString();
                    String strProcessstatus = spProcessstatus.getSelectedItem().toString();
                    String strCostumerStatus = spCostumersatatus.getSelectedItem().toString();
                    String strProcessed = spProcessed.getSelectedItem().toString();
                    String strLimitDate = txtLimitDate.getText().toString();
                    String strLatContact = txtlastcontac.getText().toString();

                    final Persons persons = new Persons();
                    persons.Document = strDocument;
                    persons.Name = strName;
                    persons.LastName = strLatName;
                    persons.cell1 = strCell;
                    persons.cell2=cell2;
                    persons.phone = strPhone;
                    persons.maial1=strEmail;
                    persons.maial2=email2;
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
                    persons.LimitDateProcessStatus = strLimitDate;
                    persons.lastContact = strLatContact;
                    persons.IdPerson=idperson;
                    restService = new RestService();
                    restService.getService().EditCostumer(persons, Integer.parseInt(idperson), new Callback<Boolean>() {
                        @Override
                        public void success(Boolean sucs, Response response) {
                            persons.cell2="";
                            persons.maial2="";
                            List<Persons> PersonsList = new ArrayList<Persons>();
                            PersonsList.add(persons);
                            manager = new DataBaseManager(getActivity());
                            manager.Open(getActivity());
                            manager.UpdatePerson(PersonsList);
                            manager.Close(getActivity());
                            if (dialog != null) {
                                dialog.cancel();
                            }
                            Toast.makeText(getActivity(), "Cliente actualizado correctamente.", Toast.LENGTH_LONG).show();
                            String message = persons.IdPerson;
                            Intent intent;
                            intent = new Intent(getActivity(), DetailCostumer.class);
                            intent.putExtra(AlarmClock.EXTRA_MESSAGE, message);
                            startActivity(intent);
                            getActivity().finish();

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


        //String strIdPerson = getIntent().getStringExtra(AlarmClock.EXTRA_MESSAGE);

        getPerson(Integer.parseInt(idperson));

        return view;
    }

    private void getPerson(int IdPerson){
        //Call to server to grab list of student records. this is a asyn
        try {
            if (conecNetWork()) {
                dialog = ProgressDialog.show(getActivity(), "",
                        "Actualizando datos del cliente. Por favor espere...", true);

                restService.getService().getCostumerById(IdPerson, new Callback<List<Persons>>() {
                    @Override
                    public void success(List<Persons> persons, Response response) {
                        List<Persons> lstpr = persons;
                        manager.Open(getActivity());
                        manager.UpdatePerson(lstpr);
                        manager.Close(getActivity());
                        for (Persons person:lstpr) {
                            txtDocument.setText(person.Document);
                            txtName.setText(person.Name);

                            txtLastName.setText(person.LastName);
                            txtCell.setText(person.cell1.trim());
                            cell2=person.cell1.trim();
                            email2=person.maial1.trim();
                            txtPhone.setText(person.phone.trim());
                            txtEmail.setText(person.maial1);
                            txtobservations.setText(person.observations);

                            String[] arrayListprocessstatus = getResources().getStringArray(R.array.processstatus_array);//.indexOf(person.ProcessStatus);
                            int cont=0;
                            for (String s : arrayListprocessstatus)
                            {
                                String vl=s.trim().toLowerCase().toString();
                                String vp=person.ProcessStatus.trim().toLowerCase().toString();
                                String vlf=vl.toString();
                                String vpf=vp.toString();
                                boolean compare=vlf.equals(vpf);
                                if (compare)
                                {
                                    spProcessstatus.setSelection(cont);
                                    break;
                                }
                                cont++;
                            }


                            String[] arrayListcostumerstatus = getResources().getStringArray(R.array.costumerstatus_array);//.indexOf(person.ProcessStatus);
                            cont=0;
                            for (String s : arrayListcostumerstatus)
                            {
                                String vl=s.trim().toLowerCase().toString();
                                String vp=person.clientstatus.trim().toLowerCase().toString();
                                String vlf=vl.toString();
                                String vpf=vp.toString();
                                boolean compare=vlf.equals(vpf);
                                if (compare)
                                {
                                    spCostumersatatus.setSelection(cont);
                                    break;
                                }
                                cont++;
                            }
                            String[] arrayListprocessed= getResources().getStringArray(R.array.processed_array);//.indexOf(person.ProcessStatus);
                            cont=0;
                            for (String s : arrayListprocessed)
                            {
                                String vl=s.trim().toLowerCase().toString();
                                String vp=person.processed.trim().toLowerCase().toString();
                                String vlf=vl.toString();
                                String vpf=vp.toString();
                                boolean compare=vlf.equals(vpf);
                                if (compare)
                                {
                                    spProcessed.setSelection(cont);
                                    break;
                                }
                                cont++;
                            }


                            txtlastcontac.setText(person.lastContact);
                            txtLimitDate.setText(person.LimitDateProcessStatus);
                        }
                        if (dialog != null) {
                            dialog.cancel();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(getActivity(), error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }else {

                showAlertDialog(getActivity(),"Conexión a internet","Usted no cuenta con conexión a internet para la actualizacion de clientes",true);
            }
        }catch (Exception e){
            if (dialog != null) {
                dialog.cancel();
            }
            showAlertDialog(getActivity(),"Error","No fue posible realizar la actualización de clientes, por favor intentelo de nuevo",true);
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

    private boolean submitForm() {
        boolean validate=false;
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



}
