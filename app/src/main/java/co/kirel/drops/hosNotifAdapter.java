package co.kirel.drops;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class hosNotifAdapter extends RecyclerView.Adapter<hosNotifAdapter.MyViewHolder> {
    Context context;
    ArrayList<Donations> donationsArrayList;
    String name,number,bloodgroup;
    Button ok_dlg_btn;
    TextView donname,donnumber,donbg;
    FirebaseFirestore firestore=FirebaseFirestore.getInstance();

    public hosNotifAdapter(Context context, ArrayList<Donations> donationsArrayList) {
        this.context=context;
        this.donationsArrayList= donationsArrayList;
    }

    @NonNull
    @Override
    public hosNotifAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v =LayoutInflater.from(context).inflate(R.layout.notifitem,parent,false);
        return new hosNotifAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull hosNotifAdapter.MyViewHolder holder, int position) {

        Donations donations= donationsArrayList.get(position);
        if(donations.DonationStatus.equals("Yes")){
            holder.hotvrqid.setText("Donation Completed!");
            holder.hotvrqid.setTextColor(Color.parseColor("#06A755"));
            holder.notidesc.setText("Donation completed Succesfully. Once a blood donor, always a lifesaver.");
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
                    dntndata.put("Hnotified","Yes");
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
                }
            });
        }else if(donations.DonationStatus.equals("No")) {
            holder.hotvrqid.setText("Pending Donation!");
            holder.hotvrqid.setTextColor(Color.parseColor("#FBA010"));
            holder.notidesc.setText("The donation is pending. View donor details.");
            holder.rdid.setText(donations.DonationId);
            holder.rdid.setTextColor(Color.parseColor("#FBA010"));
            holder.notidesc.setTextColor(Color.parseColor("#FBA010"));
            holder.noticardView.setCardBackgroundColor(Color.parseColor("#FEE5C7"));
            holder.circle.setCardBackgroundColor(Color.parseColor("#FBA010"));
            holder.notifimg.setImageResource(R.drawable.ic_baseline_warning_white_24);

            holder.noticardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    View alertCustomDialog = LayoutInflater.from(context).inflate(R.layout.profile_dialog, null);
                    androidx.appcompat.app.AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.setView(alertCustomDialog);
                    ok_dlg_btn=alertCustomDialog.findViewById(R.id.dondetdone);
                    donname=alertCustomDialog.findViewById(R.id.donname);
                    donnumber=alertCustomDialog.findViewById(R.id.donnumber);
                    donbg=alertCustomDialog.findViewById(R.id.donbg);
                    firestore= FirebaseFirestore.getInstance();
                    DocumentReference docRef = firestore.collection("Donor").document(donations.DonorId);
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    name= document.getString("Name");
                                    number= document.getString("phonenumber");
                                    bloodgroup= document.getString("bloodgroup");
                                    donname.setText(name);
                                    donnumber.setText(number);
                                    donbg.setText(bloodgroup);
                                } else {
                                    Log.d("error", "No such document");
                                }
                            } else {
                                Log.d("error", "get failed with ", task.getException());
                            }
                        }
                    });
                    final AlertDialog dialog = alertDialog.create();
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                    ok_dlg_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.cancel();
                            Toast.makeText(context,"Done",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }else if(donations.DonationStatus.equals("Cancelled")) {
            holder.hotvrqid.setText("Donation Cancelled!");
            holder.hotvrqid.setTextColor(Color.parseColor("#EB3F24"));
            holder.notidesc.setText("Donation process Cancelled. Donate blood to save others.");
            holder.rdid.setText(donations.DonationId);
            holder.rdid.setTextColor(Color.parseColor("#EB3F24"));
            holder.notidesc.setTextColor(Color.parseColor("#EB3F24"));
            holder.noticardView.setCardBackgroundColor(Color.parseColor("#FFE3E0"));
            holder.circle.setCardBackgroundColor(Color.parseColor("#EB3F24"));
            holder.notifimg.setImageResource(R.drawable.ic_baseline_close_white_24);

            holder.noticardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Map<String,Object> dntndata= new HashMap<>();
                    dntndata.put("Hnotified","Yes");
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
