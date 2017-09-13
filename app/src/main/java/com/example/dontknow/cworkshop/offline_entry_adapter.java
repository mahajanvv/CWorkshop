package com.example.dontknow.cworkshop;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Dont know on 26-07-2017.
 */

public class offline_entry_adapter extends RecyclerView.Adapter<offline_entry_adapter.MyViewHolder> {

    private ArrayList<CWDatabase> dataset;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView email;
        TextView phone;
        TextView remaining;
        TextView status;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.name = (TextView) itemView.findViewById(R.id.idoffline_entry_name);
            this.email = (TextView) itemView.findViewById(R.id.idoffline_entry_email);
            this.phone = (TextView) itemView.findViewById(R.id.idoffline_entry_phone);
            this.remaining = (TextView) itemView.findViewById(R.id.idoffline_entry_remaining);
            this.status = (TextView) itemView.findViewById(R.id.idoffline_entry_status);
        }
    }
        public offline_entry_adapter(ArrayList<CWDatabase> data)
        {
            this.dataset = data;
        }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.offline_entry_row,parent,false);
        view.setOnLongClickListener(offline_entry_list.myOnLongClickListener);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
      TextView name = holder.name;
        TextView email = holder.email;
        TextView phone = holder.phone;
        TextView remaining = holder.remaining;
        TextView status = holder.status;
        if(dataset.get(position).get_status().equals("Not Uploaded"))
            status.setTextColor(Color.RED);
        else
            status.setTextColor(Color.GREEN);

        name.setText(dataset.get(position).get_firstname()+" "+dataset.get(position).get_midname()+" "+dataset.get(position).get_lastname());
        email.setText(dataset.get(position).get_email());
        phone.setText(dataset.get(position).get_phone());
        remaining.setText(dataset.get(position).get_remaining());
        status.setText(dataset.get(position).get_status());
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}

