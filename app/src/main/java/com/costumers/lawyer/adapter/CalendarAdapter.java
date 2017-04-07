package com.costumers.lawyer.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.method.DateTimeKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.costumers.lawyer.R;
import com.costumers.lawyer.entities.EventAdapter;
import com.costumers.lawyer.service.RestService;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.jar.Attributes;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by GL552VW-DM337T on 23/03/2017.
 */

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {

    private final LayoutInflater mInflater;
    private List<EventAdapter> mEvent;
    AdapterView.OnItemClickListener mItemClickListener;

    public CalendarAdapter(Context context, List<EventAdapter> events) {
        mInflater = LayoutInflater.from(context);
        mEvent =new ArrayList<>(events);
    }

    // Define listener member variable
    private static OnItemClickListener listener;
    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(String idperson, int position);
    }
    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public CalendarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = mInflater.inflate(R.layout.calendar_row, parent, false);
        return new CalendarViewHolder(itemView);
    }

    public static class CalendarViewHolder extends RecyclerView.ViewHolder{
        public TextView name, title, startDate,id,typeEvent,txtDescription;
        public LinearLayout Ltype;
        public CardView cardView;
        public SwitchCompat execute;
        ProgressDialog dialog =null;

        public CalendarViewHolder(final View view) {
            super(view);
            typeEvent = (TextView) view.findViewById(R.id.txtType);
            name = (TextView) view.findViewById(R.id.txtName);
            title = (TextView) view.findViewById(R.id.txtTitle);
            startDate = (TextView) view.findViewById(R.id.txtstrStartDate);
            id= (TextView) view.findViewById(R.id.txtId);
            Ltype=(LinearLayout) view.findViewById(R.id.Ltype);
            cardView=(CardView)view.findViewById(R.id.cv);
            execute=(SwitchCompat)view.findViewById(R.id.Sexecute);
            txtDescription=(TextView)view.findViewById(R.id.txtDescription);





            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Triggers click upwards to the adapter on click
                    if (listener != null)
                        listener.onItemClick(id.getText().toString(), getLayoutPosition());
                }


            });
        }

        public void bind(EventAdapter model) {
            typeEvent.setText(model.getTypeEvent());
            name.setText(model.getFullName());
            title.setText(model.getTitle());
            txtDescription.setText(model.getDescription());
            Integer idEvent=Integer.parseInt(model.getId().toString());
            execute.setTextOn(idEvent.toString());
            execute.setTextOff(idEvent.toString());
            if(model.getExecuted()=="true")
            {
                execute.setChecked(true);
            }else {
                execute.setChecked(false);
            }

            execute.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {

                    RestService restService;
                    restService = new RestService();
                    String id=((SwitchCompat)buttonView).getTextOn().toString();

                    Integer idE=Integer.parseInt(id);

                    restService.getService().EventCalendarExecuted(idE,isChecked,new Callback<Boolean>() {
                        @Override
                        public void success(Boolean events, Response response) {

                            if(isChecked)
                            {
                                cardView.setCardBackgroundColor(Color.argb(25,76,175, 80));
                            }else {
                                cardView.setCardBackgroundColor(Color.argb(25,255,193, 7));
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {

                        }
                    });
                }
            });
            DateFormat dfLbl = new SimpleDateFormat("MM/dd/yyyy");
            DateFormat outputf = new SimpleDateFormat("dd/MM/yyyy");
            String dateEventStr="";
            try {
                Date dateSt=dfLbl.parse(model.getStartDate().toString());
                dateEventStr=outputf.format(dateSt);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            startDate.setText(dateEventStr + " * " +model.getStrStartDate() + " / "+model.getStrEndDate());
            id.setText(model.getId());
            String type=model.getTypeEvent();
            if (type=="Cita")
            {
                Ltype.setBackgroundResource(R.drawable.type1);
            }else if (type=="Audiencia")
            {
                Ltype.setBackgroundResource(R.drawable.type2);
            }else if (type=="Vencimiento")
            {
                Ltype.setBackgroundResource(R.drawable.type3);
            }else
            {
                Ltype.setBackgroundResource(R.drawable.type4);
            }

            if(model.getExecuted()=="true")
            {
                cardView.setCardBackgroundColor(Color.argb(25,76,175, 80));
            }
            else {
                    //Format of the date defined in the input String
                    DateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
                    //Desired format: 24 hour format: Change the pattern as per the need
                    DateFormat outputformat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                    Date dateEvent = null;
                    String output = null;
                    try{
                        //Converting the input String to Date
                        dateEvent= df.parse(model.getStartDate().toString() + " "+model.getStrStartDate().toString());
                        //Changing the format of date and storing it in String
                        output = outputformat.format(dateEvent);
                        //Displaying the date
                        System.out.println(output);
                    }catch(ParseException pe){
                        pe.printStackTrace();
                    }

                    java.util.Date getDate= new Date();
                    java.util.Calendar cal = java.util.Calendar.getInstance();
                    cal.setTime(getDate);

                    long diff = dateEvent.getTime() - getDate.getTime();//as given
                    long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
                    if(minutes > 0)
                    {
                        if (minutes <= 30)
                        {
                            cardView.setCardBackgroundColor(Color.argb(25,244,67, 54));
                        }
                    }
                    else{
                        cardView.setCardBackgroundColor(Color.argb(25,255,193, 7));
                    }

            }


            //Validaciones para colocar colora als cards
            /*

            */

        }
    }


    @Override
    public void onBindViewHolder(CalendarViewHolder holder, int position) {
        final EventAdapter model = mEvent.get(position);
        holder.bind(model);
    }



    @Override
    public int getItemCount() {
        return mEvent.size();
    }

    public void animateTo(List<EventAdapter> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<EventAdapter> newModels) {
        for (int i = mEvent.size() - 1; i >= 0; i--) {
            final EventAdapter model = mEvent.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<EventAdapter> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final EventAdapter model = newModels.get(i);
            if (!mEvent.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<EventAdapter> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final EventAdapter model = newModels.get(toPosition);
            final int fromPosition = mEvent.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public EventAdapter removeItem(int position) {
        final EventAdapter model = mEvent.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, EventAdapter model) {
        mEvent.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final EventAdapter model = mEvent.remove(fromPosition);
        mEvent.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }




}
