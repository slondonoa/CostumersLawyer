package com.costumers.lawyer.activity;


import android.app.Fragment;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;


import com.costumers.lawyer.R;


public class EditCostumer extends AppCompatActivity {

    private String strIdPerson = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_costumer);
        strIdPerson = getIntent().getStringExtra(AlarmClock.EXTRA_MESSAGE);


        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, com.costumers.lawyer.fragments.EditCostumerData.newInstance(strIdPerson))
                    .commit();

        }
    }

}
