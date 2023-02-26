package co.kirel.drops;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class referAdapter extends RecyclerView.Adapter<referAdapter.MyViewHolder> {

    Context context;
    ArrayList<Donor> donorsArrayList;
    List<String> docsArray;
    String rEmail,email,ReqId;
    FirebaseFirestore firestore;




    public referAdapter(Context context, ArrayList<Donor> donorsArrayList, List<String> docsArray,String email,String ReqId) {
        this.context=context;
        this.donorsArrayList= donorsArrayList;
        this.docsArray = docsArray;
        this.email = email;
        this.ReqId = ReqId;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v =LayoutInflater.from(context).inflate(R.layout.referitem,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Donor donors= donorsArrayList.get(position);
        holder.tvhn.setText(donors.Name);
        holder.tvbg.setText(donors.bloodgroup);
        holder.tvpn.setText(donors.phonenumber);
        rEmail = docsArray.get(position);


        holder.refcardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firestore = FirebaseFirestore.getInstance();
                Map<String, Object> data = new HashMap<>();
                data.put(rEmail,email);
                firestore.collection("Requirements").document(ReqId).update("Referred",data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Intent i = new Intent(context.getApplicationContext(),donor_home.class);
                                i.putExtra("Email",email);
                                Toast.makeText(context.getApplicationContext(), "Yes", Toast.LENGTH_SHORT).show();
                                context.startActivity(i);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });

            }
        });

    }

    @Override
    public int getItemCount() {
        return donorsArrayList.size();
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
