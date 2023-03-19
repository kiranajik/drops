package co.kirel.drops;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class allDonAdapter extends RecyclerView.Adapter<allDonAdapter.MyViewHolder> {
    Context context;
    ArrayList<Donations> donationsArrayList;

    public allDonAdapter(Context context, ArrayList<Donations> donationsArrayList) {
        this.context=context;
        this.donationsArrayList= donationsArrayList;
    }

    @NonNull
    @Override
    public allDonAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v =LayoutInflater.from(context).inflate(R.layout.donlistitem,parent,false);
        return new allDonAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull allDonAdapter.MyViewHolder holder, int position) {

        Donations donations= donationsArrayList.get(position);
        holder.hotvrqid.setText(donations.DonationId);
        if(donations.btlsdonated.equals("1")){
            holder.hotvsts.setText(donations.btlsdonated+" Bottle");
        }else{
            holder.hotvsts.setText(donations.btlsdonated+" Bottles");
        }
        holder.img.setImageResource(R.drawable.vecy);


    }

    @Override
    public int getItemCount() {
        return donationsArrayList.size();
    }

    public static  class MyViewHolder extends RecyclerView.ViewHolder{

        TextView hotvrqid,hotvsts;
        ImageView img;
        CardView horeqcardView;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            hotvrqid=itemView.findViewById(R.id.hotvReqId);
            hotvsts=itemView.findViewById(R.id.horeqsts);
            horeqcardView=itemView.findViewById(R.id.horeqcardview);
            img=itemView.findViewById(R.id.notifimg);

        }
    }
}
