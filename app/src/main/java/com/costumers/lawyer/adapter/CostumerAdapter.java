package com.costumers.lawyer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.costumers.lawyer.R;
import com.costumers.lawyer.activity.FilteredCustomers;
import com.costumers.lawyer.activity.MainActivity;
import com.costumers.lawyer.entities.PersonAdapter;
//import com.costumers.lawyer.viewholder.CostumersViewHolder;

import java.util.ArrayList;
import java.util.List;

public class CostumerAdapter  extends RecyclerView.Adapter<CostumerAdapter.CostumersViewHolder> {

    private final LayoutInflater mInflater;
    private List<PersonAdapter> mPersons;
    AdapterView.OnItemClickListener mItemClickListener;


    public CostumerAdapter(Context context,List<PersonAdapter> persons) {
        mInflater = LayoutInflater.from(context);
        mPersons=new  ArrayList<>(persons);
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
    public CostumersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = mInflater.inflate(R.layout.costumer_row, parent, false);
        return new CostumersViewHolder(itemView);
    }

    public static class CostumersViewHolder extends RecyclerView.ViewHolder{
        public TextView name, lastname, lastcontact, document, idperson,nameLastname;

        public CostumersViewHolder(final View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.txtName);
            lastname = (TextView) view.findViewById(R.id.txtLastName);
            lastcontact = (TextView) view.findViewById(R.id.txtLastContact);
            document = (TextView) view.findViewById(R.id.txtDocument);
            idperson = (TextView) view.findViewById(R.id.txtIdPerson);
            nameLastname = (TextView) view.findViewById(R.id.txtNameLastName);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Triggers click upwards to the adapter on click
                    if (listener != null)
                        listener.onItemClick(idperson.getText().toString(), getLayoutPosition());
                }
            });
        }

        public void bind(PersonAdapter model) {
            name.setText(model.getName());
            lastname.setText(model.getLastName());
            lastcontact.setText(model.getLastContact());
            document.setText(model.getDocument());
            idperson.setText(model.getIdPerson());
            nameLastname.setText(model.getNameLastName());
        }
    }



    @Override
    public void onBindViewHolder(CostumersViewHolder holder, int position) {
        final PersonAdapter model = mPersons.get(position);
        holder.bind(model);
    }



    @Override
    public int getItemCount() {
        return mPersons.size();
    }

    public void animateTo(List<PersonAdapter> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<PersonAdapter> newModels) {
        for (int i = mPersons.size() - 1; i >= 0; i--) {
            final PersonAdapter model = mPersons.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<PersonAdapter> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final PersonAdapter model = newModels.get(i);
            if (!mPersons.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<PersonAdapter> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final PersonAdapter model = newModels.get(toPosition);
            final int fromPosition = mPersons.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public PersonAdapter removeItem(int position) {
        final PersonAdapter model = mPersons.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, PersonAdapter model) {
        mPersons.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final PersonAdapter model = mPersons.remove(fromPosition);
        mPersons.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }



}
