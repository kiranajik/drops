package co.kirel.drops;

import android.content.Context;
import android.content.Intent;
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

        View v =LayoutInflater.from(context).inflate(R.layout.donlistitem,parent,false);
        return new PendingAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingAdapter.MyViewHolder holder, int position) {

        Donations donations= donationsArrayList.get(position);
        holder.notifimg.setImageResource(R.drawable.drop_btnn);
        holder.hotvrqid.setText(donations.RequirementId);
        holder.hotvsts.setText(donations.DonationId);
        holder.horeqcardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, DonationQr.class);
                i.putExtra("check","notification");
                i.putExtra("donationId",donations.DonationId);
                i.putExtra("reqId",donations.RequirementId);
                i.putExtra("honame",donations.honame);
                context.startActivity(i);
            }
        });
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
