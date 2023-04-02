package co.kirel.drops;

import static android.content.Context.MODE_PRIVATE;
import static android.os.ParcelFileDescriptor.MODE_APPEND;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<Requirements> requirementsArrayList;
    String nxtDntnDate;

    boolean your_date_is_outdated;
    Button ok_dlg_btn;
    TextView tvdate;


    public MyAdapter(Context context, ArrayList<Requirements> requirementsArrayList) {
        this.context=context;
        this.requirementsArrayList= requirementsArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v =LayoutInflater.from(context).inflate(R.layout.listitem,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        //ALERT CANCEL DIALOG CODE
        View alertCustomDialog1 = LayoutInflater.from(context).inflate(R.layout.nxtdonationdate_dlg, null);
        androidx.appcompat.app.AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(context);
        alertDialog1.setView(alertCustomDialog1);
        ok_dlg_btn=alertCustomDialog1.findViewById(R.id.ok_dntn_dlg_btn);
        tvdate=alertCustomDialog1.findViewById(R.id.tvnxtdate);
        final AlertDialog dialog1 = alertDialog1.create();
        //ALERT CANCEL DIALOG CODE

        //donor_home activity= new donor_home();
        //semail= activity.getMyData();

        ok_dlg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.cancel();
            }
        });

        Requirements requirements= requirementsArrayList.get(position);
        holder.tvhn.setText(requirements.honame);
        holder.tvbg.setText(requirements.BloodGroup);
        holder.cpi.setIndeterminate(false);
        int percentage=Integer.parseInt(requirements.btlsgot)*100/Integer.parseInt(requirements.NoofBottles);
        holder.cpi.setProgressCompat(percentage, true);
        holder.reqsts.setText(requirements.btlsgot+"/"+requirements.NoofBottles);

        FirebaseFirestore firestore=FirebaseFirestore.getInstance();
        SharedPreferences sharedPref = PreferenceManager
                .getDefaultSharedPreferences(context);
        String semail = sharedPref.getString("donoremail", "");

        holder.reqcardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(view.getContext(), nxtDntnDate, Toast.LENGTH_SHORT).show();
                DocumentReference docRefnc = firestore.collection("Donor").document(semail);
                docRefnc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                nxtDntnDate = document.getString("nxtDntnDate");
                            } else {
                                Log.d("error", "No such document");
                            }
                        } else {
                            Log.d("error", "Firestore Error", task.getException());
                        }
                    }
                });
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        Date strDate = null;
                        try {
                            strDate = sdf.parse(nxtDntnDate);
                        } catch (ParseException e) {
                            Log.e("date",e.toString());
                        }
                        if (new Date().after(strDate)) {
                            your_date_is_outdated = true;

                                Intent i = new Intent(view.getContext(), DonationDetails.class);
                                String uid = requirements.getRequirementId();
                                String bldgrp = requirements.getBloodGroup();
                                i.putExtra("ReqId", uid);
                                i.putExtra("BldGrp", bldgrp);
                                view.getContext().startActivity(i);
                            }
                            else{
                                your_date_is_outdated = false;
                                TextView tvdate=alertCustomDialog1.findViewById(R.id.tvnxtdate);
                                tvdate.setText("Donors are required to wait a minimum of 12 weeks between donations. You can donate again after " + nxtDntnDate);
                                dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialog1.show();
                            }
                    }
                },500);
            }
        });

    }

    @Override
    public int getItemCount() {
        return requirementsArrayList.size();
    }

    public static  class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvhn,tvbg,reqsts;
        CardView reqcardView;
        CircularProgressIndicator cpi;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvhn=itemView.findViewById(R.id.tvhosname);
            tvbg=itemView.findViewById(R.id.tvbloodgrp);
            reqcardView=itemView.findViewById(R.id.donationcardview);
            cpi=itemView.findViewById(R.id.cpi);
            reqsts=itemView.findViewById(R.id.reqstatus);
        }
    }
}