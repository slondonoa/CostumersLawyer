package com.costumers.lawyer.activity;

import android.provider.AlarmClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.costumers.lawyer.R;

public class EditEventCalendar extends AppCompatActivity {
    private String idEvent = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event_calendar);

        idEvent = getIntent().getStringExtra(AlarmClock.EXTRA_MESSAGE);


        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, com.costumers.lawyer.fragments.EditEventCalendarFragment.newInstance(idEvent))
                    .commit();

        }
    }
}
