package co.kirel.drops;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PendingAdapter extends RecyclerView.Adapter<PendingAdapter.MyViewHolder> {
    Context context;
    ArrayList<Donations> donationsArrayList;

    public PendingAdapter(Context context, ArrayList<Donations> donationsArrayList) {
        this.context=context;
        this.donationsArrayList= donationsArrayList;
    }

    @NonNull
    @Override
    public PendingAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v =LayoutInflater.from(context).inflate(R.layout.cmpltddonlistitem,parent,false);
        return new PendingAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingAdapter.MyViewHolder holder, int position) {

        Donations donations= donationsArrayList.get(position);
        holder.notifimg.setImageResource(R.drawable.ic_baseline_warning_24);
        holder.hotvrqid.setText(donations.RequirementId);
        holder.hotvsts.setText(donations.DonationId);
    }

    @Override
    public int getItemCount() {
        return donationsArrayList.size();
    }

    public static  class MyViewHolder extends RecyclerView.ViewHolder{

        TextView hotvrqid,hotvsts;
        ImageView notifimg;
        CardView horeqcardView;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            hotvrqid=itemView.findViewById(R.id.hotvReqId);
            hotvsts=itemView.findViewById(R.id.horeqsts);
            notifimg=itemView.findViewById(R.id.notifimg);
            horeqcardView=itemView.findViewById(R.id.horeqcardview);
        }
    }
}
