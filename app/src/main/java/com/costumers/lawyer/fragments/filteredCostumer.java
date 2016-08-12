package com.costumers.lawyer.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.costumers.lawyer.R;
import com.costumers.lawyer.activity.DetailCostumer;
import com.costumers.lawyer.activity.FilteredCustomers;
import com.costumers.lawyer.adapter.CostumerAdapter;
import com.costumers.lawyer.data.DataBaseManager;
import com.costumers.lawyer.entities.PersonAdapter;
import com.costumers.lawyer.entities.Persons;

import java.util.ArrayList;
import java.util.List;


public class filteredCostumer extends Fragment implements SearchView.OnQueryTextListener{

    public static filteredCostumer newInstance() {
        return new filteredCostumer();
    }

    private DataBaseManager manager;
    private RecyclerView mRecyclerView;
    private CostumerAdapter mAdapter;
    private List<PersonAdapter> mPersons;
    Toolbar toolbar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_filtered_costumer, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPersons = new ArrayList<>();
        manager = new DataBaseManager(getActivity());
        manager.Open(getActivity());
        String where=manager.getFilter();
        manager.DeleteFilter();
        List<Persons> lstperson = manager.getFilterPersons(where);
        manager.Close(getActivity());

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("Clientes filtrados (" + lstperson.size() + ")");
        //set toolbar appearance
        //for crate home button
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        //activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));



        for(Persons persons:lstperson) {
            String name="";
            if(persons.Name.length() > 21) {
                name = persons.Name.substring(0, 19) + "...";
            }else {
                name = persons.Name;
            }
            String datestr="";
            if(where.contains("lastContact")) {
                datestr="Sin contactar";
                if (persons.lastContact != null) {
                    if (persons.lastContact.length() >= 10) {
                        datestr = persons.lastContact.substring(0, 10);
                    }
                }
            }else if(where.contains("LimitDateProcessStatus")) {
                datestr="Sin fecha limite";
                if (persons.LimitDateProcessStatus != null) {
                    if (persons.LimitDateProcessStatus.length() >= 10) {
                        datestr = persons.LimitDateProcessStatus.substring(0, 10);
                    }
                }

            }else if(where.contains("DateStart")) {
                datestr="Sin fecha de inicio";
                if (persons.start != null) {
                    if (persons.start.length() >= 10) {
                        datestr = persons.start.substring(0, 10);
                    }
                }

            }

            PersonAdapter pa=new PersonAdapter(name.toUpperCase(),persons.LastName.toUpperCase(),persons.Document,datestr,persons.IdPerson,persons.Name +" " + persons.LastName);
            mPersons.add(pa);

        }
        mAdapter = new CostumerAdapter(getActivity(), mPersons);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new CostumerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String Idperson, int position) {
                String message = Idperson;
                Intent intent;
                intent = new Intent(getActivity(), DetailCostumer.class);
                intent.putExtra(AlarmClock.EXTRA_MESSAGE, message);
                startActivity(intent);
            }
        });


/*
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                PersonAdapter movie = mPersons.get(position);
                Intent intent;
                intent = new Intent(getActivity(), DetailCostumer.class);
                String message = movie.getIdPerson().toString();
                intent.putExtra(AlarmClock.EXTRA_MESSAGE, message);
                startActivity(intent);
            }
            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        */

    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_costumers, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextChange(String query) {
        final List<PersonAdapter> filteredModelList = filter(mPersons, query);
        mAdapter.animateTo(filteredModelList);
        mRecyclerView.scrollToPosition(0);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private List<PersonAdapter> filter(List<PersonAdapter> models, String query) {
        query = query.toLowerCase();

        final List<PersonAdapter> filteredModelList = new ArrayList<>();
        for (PersonAdapter model : models) {
            final String text = model.getNameLastName().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    /*
    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private filteredCostumer.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final filteredCostumer.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
    */



}
