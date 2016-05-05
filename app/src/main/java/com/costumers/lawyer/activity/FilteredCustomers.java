package com.costumers.lawyer.activity;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.costumers.lawyer.R;
import com.costumers.lawyer.fragments.filteredCostumer;

public class FilteredCustomers extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtered_customers);

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, filteredCostumer.newInstance())
                    .commit();
        }



    }

}
