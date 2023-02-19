package co.kirel.drops;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<Requirements> requirementsArrayList;

    public MyAdapter(Context context, ArrayList<Requirements> requirementsArrayList) {
        this.context=context;
        this.requirementsArrayList= requirementsArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v =LayoutInflater.from(context).inflate(R.layout.listitem,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Requirements requirements= requirementsArrayList.get(position);
        holder.tvhn.setText(requirements.Purpose);
        holder.tvbg.setText(requirements.BloodGroup);

    }

    @Override
    public int getItemCount() {
        return requirementsArrayList.size();
    }

    public static  class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvhn,tvbg;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvhn=itemView.findViewById(R.id.tvhosname);
            tvbg=itemView.findViewById(R.id.tvbloodgrp);
        }
    }
}
