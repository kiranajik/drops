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
import java.util.HashMap;
import java.util.Map;

public class referredAdapter extends RecyclerView.Adapter<referredAdapter.MyViewHolder> {

    Context context;
    ArrayList<Requirements> requirementsArrayList;
    String email;

    public referredAdapter(Context context, ArrayList<Requirements> requirementsArrayList,String email) {
        this.context=context;
        this.requirementsArrayList= requirementsArrayList;
        this.email = email;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v =LayoutInflater.from(context).inflate(R.layout.referitem,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Requirements requirements= requirementsArrayList.get(position);
        holder.tvhn.setText(requirements.honame);
        holder.tvbg.setText(requirements.BloodGroup);
        Map.Entry<String, String> firstEntry = requirements.Referred.entrySet().iterator().next();
        String keys = firstEntry.getKey();
        holder.tvpn.setText(keys);

        holder.refcardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(view.getContext(),DonationDetails.class);
                String uid = requirements.getRequirementId();
                i.putExtra("ReqId",uid);
                i.putExtra("referrer",keys);
                i.putExtra("email",email);
                view.getContext().startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return requirementsArrayList.size();
    }

    public static  class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvhn,tvbg,tvpn;
        CardView refcardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvhn=itemView.findViewById(R.id.tvdonname);
            tvbg=itemView.findViewById(R.id.tvbloodgrp);
            tvpn=itemView.findViewById(R.id.tvdonnumber);
            refcardView=itemView.findViewById(R.id.refercardview);
        }
    }
}
