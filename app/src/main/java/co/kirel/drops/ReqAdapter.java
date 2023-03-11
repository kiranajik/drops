package co.kirel.drops;

import android.content.Context;
import android.content.Intent;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReqAdapter extends RecyclerView.Adapter<ReqAdapter.MyViewHolder> {
    Context context;
    ArrayList<Requirements> requirementsArrayList;

    public ReqAdapter(Context context, ArrayList<Requirements> requirementsArrayList) {
        this.context=context;
        this.requirementsArrayList= requirementsArrayList;
    }

    @NonNull
    @Override
    public ReqAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v =LayoutInflater.from(context).inflate(R.layout.reqlistitemho,parent,false);
        return new ReqAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ReqAdapter.MyViewHolder holder, int position) {

        Requirements requirements= requirementsArrayList.get(position);
        holder.hotvrqid.setText(requirements.RequirementId);
        holder.hotvsts.setText(requirements.btlsgot+"/"+requirements.NoofBottles);
        holder.hotvbg.setText(requirements.BloodGroup);
        holder.progressBar.setProgress(Integer.parseInt(requirements.getBtlsgot()));
        holder.progressBar.setMax(Integer.parseInt(requirements.getNoofBottles()));

//      NOT NEEDED CODE

//        holder.reqcardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i= new Intent(view.getContext(),DonationDetails.class);
//                String uid = requirements.getRequirementId();
//                String bldgrp= requirements.getBloodGroup();
//                i.putExtra("ReqId",uid);
//                i.putExtra("BldGrp",bldgrp);
//                view.getContext().startActivity(i);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return requirementsArrayList.size();
    }

    public static  class MyViewHolder extends RecyclerView.ViewHolder{

        TextView hotvrqid,hotvsts,hotvbg;
        CardView horeqcardView;
        ProgressBar progressBar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            hotvrqid=itemView.findViewById(R.id.hotvReqId);
            hotvsts=itemView.findViewById(R.id.horeqsts);
            hotvbg=itemView.findViewById(R.id.hotvbloodgrp);
            progressBar = itemView.findViewById(R.id.reqprogressBar);
            horeqcardView=itemView.findViewById(R.id.horeqcardview);

        }
    }
}
