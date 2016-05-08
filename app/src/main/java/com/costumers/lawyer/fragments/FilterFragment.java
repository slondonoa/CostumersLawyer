package com.costumers.lawyer.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.costumers.lawyer.R;
import com.costumers.lawyer.activity.FilteredCustomers;
import com.costumers.lawyer.activity.MainActivity;
import com.costumers.lawyer.data.DataBaseManager;


public class FilterFragment extends Fragment implements OnClickListener  {

    private Button btnFilter;
    View view;
    private DataBaseManager manager;

    public FilterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_filter, container, false);

        btnFilter = (Button) view.findViewById(R.id.butAceptar);
        btnFilter.setOnClickListener(this);
        return view;
    }

    public void onClick(View view) {

        TextInputLayout NameEditText = (TextInputLayout) getView().findViewById(R.id.textInputLayoutName);
        String strName = NameEditText.getEditText().getText().toString();

        TextInputLayout DocumentEditText = (TextInputLayout) getView().findViewById(R.id.textInputLayoutDocument);
        String strDocument = DocumentEditText.getEditText().getText().toString();

        TextInputLayout LastNameEditText = (TextInputLayout) getView().findViewById(R.id.textInputLayoutLastName);
        String strLastName = LastNameEditText.getEditText().getText().toString();

        TextInputLayout ObservationEditText = (TextInputLayout) getView().findViewById(R.id.textInputLayoutObservations);
        String strObservation = ObservationEditText.getEditText().getText().toString();

        Spinner spProcessstatus = (Spinner) getView().findViewById(R.id.spprocessstatus);
        String strProcessstatus = spProcessstatus.getSelectedItem().toString();

        Spinner spCostumersatatus = (Spinner) getView().findViewById(R.id.spcostumersatatus);
        String strCostumerStatus = spCostumersatatus.getSelectedItem().toString();

        Spinner spProcessed = (Spinner) getView().findViewById(R.id.spProcessed);
        String strProcessed = spProcessed.getSelectedItem().toString();

        Spinner spSource = (Spinner) getView().findViewById(R.id.spSource);
        String strSource = spSource.getSelectedItem().toString();

        RadioGroup radiogroup =  (RadioGroup) getView().findViewById(R.id.radioButtonGroup);
        int selectedId = radiogroup .getCheckedRadioButtonId();
        // find the radio button by returned id
        RadioButton radioButton = (RadioButton) getView().findViewById(selectedId);
        String radio=radioButton.getText().toString();


        String where= "WHERE";
        boolean save=false;
        boolean and=false;
        if ( strDocument.length() > 0)
        {
            save=true;
            where= where + " " + "Document='" + strDocument +"'" ;
            and=true;
        }
        if ( strName.length() > 0)
        {
            save=true;
            if (and)
            {
                where= where + " " + " AND (Name like '%"+strName+"%' or Name like '"+strName+"%' or Name like '%"+strName+"' or Name like '"+strName+"')" ;
            }else {
                where= where + " " + "(Name like '%"+strName+"%' or Name like '"+strName+"%' or Name like '%"+strName+"' or Name like '"+strName+"')" ;
            }
            and=true;
        }
        if ( strLastName.length() > 0)
        {
            save=true;
            if (and)
            {
                where = where + " " + " AND (LastName like '%" + strLastName + "%' or LastName like ' " + strLastName + "%' or LastName like '%" + strLastName + " ' or LastName like '" + strLastName + "') ";
            }else {
                where = where + " " + "(LastName like '%" + strLastName + "%' or LastName like ' " + strLastName + "%' or LastName like '%" + strLastName + " ' or LastName like '" + strLastName + "') ";
            }
            and=true;
        }
        if ( strObservation.length() > 0)
        {
            save=true;
            if (and) {
                where = where + " " + " AND (observations like '%" + strObservation + "%' or observations like '" + strObservation + "%' or observations like '%" + strObservation + "' or observations like '" + strObservation + "')";
            }else {
                where = where + " " + "(observations like '%" + strObservation + "%' or observations like '" + strObservation + "%' or observations like '%" + strObservation + "' or observations like '" + strObservation + "')";
            }
            and=true;
        }

        if ( spProcessstatus.getSelectedItemPosition() > 0)
        {
            save=true;
            if (and){
                where= where + " " + " AND (ProcessStatus like '%" + strProcessstatus+"%' or ProcessStatus like '" + strProcessstatus+"%' or ProcessStatus like '%" + strProcessstatus+"' or ProcessStatus like '" + strProcessstatus+"')" ;
            }else {
                where= where + " " + "(ProcessStatus like '%" + strProcessstatus+"%' or ProcessStatus like '" + strProcessstatus+"%' or ProcessStatus like '%" + strProcessstatus+"' or ProcessStatus like '" + strProcessstatus+"')" ;
            }
            and=true;
        }

        if ( spCostumersatatus.getSelectedItemPosition() > 0)
        {
            save=true;
            if (and) {
                where = where + " " + " AND (clientstatus like '%" + strCostumerStatus + "%' or clientstatus like '" + strCostumerStatus + "%' or clientstatus like '%" + strCostumerStatus + "' or clientstatus like '" + strCostumerStatus + "')";
            }else {
                where = where + " " + "(clientstatus like '%" + strCostumerStatus + "%' or clientstatus like '" + strCostumerStatus + "%' or clientstatus like '%" + strCostumerStatus + "' or clientstatus like '" + strCostumerStatus + "')";
            }
            and=true;
        }
        if ( spProcessed.getSelectedItemPosition() > 0)
        {
            save=true;
            if (and) {
                where = where + " " + " AND (processed like '%" + strProcessed + "%' or processed like '" + strProcessed + "%' or processed like '%" + strProcessed + "' or processed like '" + strProcessed + "')";
            }else {
                where = where + " " + "(processed like '%" + strProcessed + "%' or processed like '" + strProcessed + "%' or processed like '%" + strProcessed + "' or processed like '" + strProcessed + "')";
            }
            and=true;
        }

        if ( spSource.getSelectedItemPosition() > 0)
        {
            save=true;
            if (and) {
                where = where + " " + " AND (source like '%" + strSource + "%' or source like '" + strSource + "%' or source like '%" + strSource + "' or source like '" + strSource + "')";
            }else {
                where = where + " " + "(source like '%" + strSource + "%' or source like '" + strSource + "%' or source like '%" + strSource + "' or source like '" + strSource + "')";
            }
            and=true;
        }


        if(radio.equals("Ultimo contacto"))
        {
            if(save)
            {
                where=where + " ORDER BY lastContact ASC ";
            }else
            {
                where=" ORDER BY lastContact ASC ";
                save=true;
            }
        }else {

            if(save)
            {
                where=where + " ORDER BY LimitDateProcessStatus ASC ";
            }else
            {
                where=" ORDER BY LimitDateProcessStatus ASC ";
                save=true;
            }
        }

        if (save) {
            manager = new DataBaseManager(getActivity());
            manager.Open(getActivity());
            manager.InsertFilter(where);
            manager.Close(getActivity());
        }
        getActivity().startActivity(new Intent(getActivity(), FilteredCustomers.class));
    }
}
