package co.kirel.drops;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DonationDetails extends AppCompatActivity {

    TextView horgname;
    Button bDate,bTime,bconfirm;
    String ReqId,sdate,stime,honame,BldGrp;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_details);

        bDate=findViewById(R.id.bdate);
        bTime=findViewById(R.id.btime);
        horgname=findViewById(R.id.horgname);
        bconfirm=findViewById(R.id.bconfirm);

        Intent i=getIntent();
        ReqId=i.getStringExtra("ReqId");
        BldGrp=i.getStringExtra("BldGrp");
        Toast.makeText(this, "Req Id"+ReqId, Toast.LENGTH_SHORT).show();

        firestore= FirebaseFirestore.getInstance();
        DocumentReference docRef = firestore.collection("Requirements").document(ReqId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        sdate= document.getString("End Date");
                        stime= document.getString("End Time");
                        honame= document.getString("honame");
                        bDate.setText(sdate);
                        bTime.setText(stime);
                        horgname.setText(honame);
                    } else {
                        Log.d("error", "No such document");
                    }
                } else {
                    Log.d("error", "get failed with ", task.getException());
                }
            }
        });

        bconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(DonationDetails.this,DonationQr.class);
                i.putExtra("ReqId",ReqId);
                i.putExtra("honame",honame);
                i.putExtra("BldGrp",BldGrp);
                startActivity(i);
                finish();
            }
        });
    }
}