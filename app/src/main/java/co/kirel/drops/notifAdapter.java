package co.kirel.drops;

import android.content.Context;
import android.content.Intent;
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
            holder.notifimg.setImageResource(R.drawable.vecy);
            holder.rdid.setText(donations.DonationId);
            holder.horeqcardView.setOnClickListener(new View.OnClickListener() {
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
            holder.notifimg.setImageResource(R.drawable.drop_btnn);
            holder.rdid.setText(donations.DonationId);
            holder.horeqcardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, DonationQr.class);
                    context.startActivity(i);

                }
            });
        }else if(donations.DonationStatus.equals("Cancelled")) {
            holder.hotvrqid.setText("Donation Cancelled!");
            holder.notifimg.setImageResource(R.drawable.cartoon);
            holder.rdid.setText(donations.DonationId);
            holder.horeqcardView.setOnClickListener(new View.OnClickListener() {
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

        TextView hotvrqid,rdid;
        ImageView notifimg;
        CardView horeqcardView;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            hotvrqid=itemView.findViewById(R.id.hotvReqId);
            notifimg=itemView.findViewById(R.id.notifimg);
            rdid=itemView.findViewById(R.id.rdid);
            horeqcardView=itemView.findViewById(R.id.horeqcardview);

        }
    }
}
