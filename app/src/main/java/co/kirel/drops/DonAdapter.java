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

public class DonAdapter extends RecyclerView.Adapter<DonAdapter.MyViewHolder> {
    Context context;
    ArrayList<Donations> donationsArrayList;

    public DonAdapter(Context context, ArrayList<Donations> donationsArrayList) {
        this.context=context;
        this.donationsArrayList= donationsArrayList;
    }

    @NonNull
    @Override
    public DonAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v =LayoutInflater.from(context).inflate(R.layout.cmpltddonlistitem,parent,false);
        return new DonAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DonAdapter.MyViewHolder holder, int position) {

        Donations donations= donationsArrayList.get(position);
        holder.hotvrqid.setText(donations.RequirementId);
        if(donations.btlsdonated.equals("1")){
            holder.hotvsts.setText(donations.btlsdonated+" Bottle");
        }else{
            holder.hotvsts.setText(donations.btlsdonated+" Bottles");
        }
        holder.notifimg.setImageResource(R.drawable.ic_baseline_bloodtype_24);
        holder.notifimg.setColorFilter(Color.parseColor("#E26864"));
    }

    @Override
    public int getItemCount() {
        return donationsArrayList.size();
    }

    public static  class MyViewHolder extends RecyclerView.ViewHolder{

        TextView hotvrqid,hotvsts;
        CardView horeqcardView;
        ImageView notifimg;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            hotvrqid=itemView.findViewById(R.id.hotvReqId);
            hotvsts=itemView.findViewById(R.id.horeqsts);
            horeqcardView=itemView.findViewById(R.id.horeqcardview);
            notifimg=itemView.findViewById(R.id.notifimg);
        }
    }
}
