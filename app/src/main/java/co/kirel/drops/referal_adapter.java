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

public class referal_adapter extends RecyclerView.Adapter<referal_adapter.MyViewHolder> {

    Context context;

    ArrayList<referal_item> referal_items;
    String rfc;

    public referal_adapter(Context context, ArrayList<referal_item> rewardsArrayList, String rfc) {
        this.context=context;
        this.referal_items= referal_items;
        this.rfc =rfc;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.referral_list_item,parent,false);
        return new MyViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        referal_item referal_item = referal_items.get(position);
        holder.remail.setText(String.valueOf(referal_item.ref_mail));

        if(referal_item.status == "1"){
            holder.status.setText("Signed  Up");
            holder.coins.setText("50 Coins");
            holder.icon.setImageResource(R.drawable.green_tick);
        }
        if(referal_item.status == "2"){
            holder.status.setText("Donated Blood");
            holder.coins.setText("200 Coins");
            holder.icon.setImageResource(R.drawable.red_heart);
        }
    }

    @Override
    public int getItemCount() {
        return referal_items.size();
    }

    public static  class MyViewHolder extends RecyclerView.ViewHolder{

        TextView remail,status,coins;
        ImageView icon;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            remail =itemView.findViewById(R.id.ref_name);
            status =itemView.findViewById(R.id.ref_status);
            coins  =itemView.findViewById(R.id.coins);
            icon   =itemView.findViewById(R.id.imageView23);
        }
    }
}
