package com.costumers.lawyer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.costumers.lawyer.R;
import com.costumers.lawyer.entities.EventAdapter;

import java.util.ArrayList;
import java.util.List;

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
        public TextView name, description, startDate,id,typeEvent;
        public LinearLayout Ltype;

        public CalendarViewHolder(final View view) {
            super(view);
            typeEvent = (TextView) view.findViewById(R.id.Type);
            description = (TextView) view.findViewById(R.id.txtDescription);
            startDate = (TextView) view.findViewById(R.id.txtstrStartDate);
            id= (TextView) view.findViewById(R.id.txtId);
            Ltype=(LinearLayout) view.findViewById(R.id.Ltype);


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
            description.setText(model.getDescription());

            startDate.setText(model.getStartDate() + " * " +model.getStrStartDate() + " / "+model.getStrEndDate());
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
