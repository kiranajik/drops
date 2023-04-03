package co.kirel.drops;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class notifAdapter extends RecyclerView.Adapter<notifAdapter.MyViewHolder> {
    Context context;
    ArrayList<Donations> donationsArrayList;
    String email;
    FirebaseFirestore firestore=FirebaseFirestore.getInstance();

    public notifAdapter(Context context, ArrayList<Donations> donationsArrayList,String email) {
        this.context=context;
        this.donationsArrayList= donationsArrayList;
        this.email = email;
    }

    @NonNull
    @Override
    public notifAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v =LayoutInflater.from(context).inflate(R.layout.notifitem,parent,false);
        return new notifAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull notifAdapter.MyViewHolder holder, int position) {

        Donations donations= donationsArrayList.get(position);
        if(donations.DonationStatus.equals("Yes")){
            holder.hotvrqid.setText("Donation Completed!");
            holder.hotvrqid.setTextColor(Color.parseColor("#06A755"));
            holder.notidesc.setText("Donation completed successfully. Congrats Donor!");
            holder.rdid.setText(donations.DonationId);
            holder.rdid.setTextColor(Color.parseColor("#06A755"));
            holder.notidesc.setTextColor(Color.parseColor("#06A755"));
            holder.noticardView.setCardBackgroundColor(Color.parseColor("#B5EACC"));
            holder.circle.setCardBackgroundColor(Color.parseColor("#06A755"));
            holder.notifimg.setImageResource(R.drawable.ic_baseline_done_24);

            holder.noticardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Map<String,Object> dntndata= new HashMap<>();
                    dntndata.put("notified","Yes");
                    firestore.collection("Donations").document(donations.DonationId).update(dntndata)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(context, "Notified", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "Not notified", Toast.LENGTH_SHORT).show();
                                }
                            });
                    Intent i = new Intent(context,donor_home.class);
                    i.putExtra("source","profile");
                    i.putExtra("Email",email);
                    context.startActivity(i);

                }
            });
        }else if(donations.DonationStatus.equals("No")) {
            holder.hotvrqid.setText("Pending Donation!");
            holder.hotvrqid.setTextColor(Color.parseColor("#FBA010"));
            holder.notidesc.setText("Donation pending. Complete to receive your reward!");
            holder.rdid.setText(donations.DonationId);
            holder.rdid.setTextColor(Color.parseColor("#FBA010"));
            holder.notidesc.setTextColor(Color.parseColor("#FBA010"));
            holder.noticardView.setCardBackgroundColor(Color.parseColor("#FEE5C7"));
            holder.circle.setCardBackgroundColor(Color.parseColor("#FBA010"));
            holder.notifimg.setImageResource(R.drawable.ic_baseline_warning_white_24);

            holder.noticardView.setOnClickListener(new View.OnClickListener() {
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
        }else if(donations.DonationStatus.equals("Cancelled")) {
            holder.hotvrqid.setText("Donation Cancelled!");
            holder.hotvrqid.setTextColor(Color.parseColor("#EB3F24"));
            holder.notidesc.setText("Donation cancelled. Donate blood to save lives.");
            holder.rdid.setText(donations.DonationId);
            holder.rdid.setTextColor(Color.parseColor("#EB3F24"));
            holder.notidesc.setTextColor(Color.parseColor("#EB3F24"));
            holder.noticardView.setCardBackgroundColor(Color.parseColor("#FFE3E0"));
            holder.circle.setCardBackgroundColor(Color.parseColor("#EB3F24"));
            holder.notifimg.setImageResource(R.drawable.ic_baseline_close_white_24);

            holder.noticardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    firestore.collection("Donations").document(donations.DonationId).delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(context, "Notified", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "Not notified", Toast.LENGTH_SHORT).show();
                                }
                            });
                    Intent i = new Intent(context, donor_home.class);
                    i.putExtra("Email", email);
                    context.startActivity(i);

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return donationsArrayList.size();
    }

    public static  class MyViewHolder extends RecyclerView.ViewHolder{

        TextView hotvrqid,rdid,notidesc;
        ImageView notifimg;
        CardView noticardView,circle;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            hotvrqid=itemView.findViewById(R.id.hotvReqId);
            notifimg=itemView.findViewById(R.id.notifimg);
            rdid=itemView.findViewById(R.id.rdid);
            notidesc=itemView.findViewById(R.id.notidesc);
            circle=itemView.findViewById(R.id.ncardView3);
            noticardView=itemView.findViewById(R.id.noticardview);

        }
    }
}
