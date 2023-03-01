package co.kirel.drops;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;

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
        holder.tvhn.setText(requirements.honame);
        holder.tvbg.setText(requirements.BloodGroup);

        holder.reqcardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(view.getContext(),DonationDetails.class);
                String uid = requirements.getRequirementId();
                String bldgrp= requirements.getBloodGroup();
                i.putExtra("ReqId",uid);
                i.putExtra("BldGrp",bldgrp);
                view.getContext().startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return requirementsArrayList.size();
    }

    public static  class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvhn,tvbg;
        CardView reqcardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvhn=itemView.findViewById(R.id.tvhosname);
            tvbg=itemView.findViewById(R.id.tvbloodgrp);
            reqcardView=itemView.findViewById(R.id.donationcardview);
        }
    }
}
