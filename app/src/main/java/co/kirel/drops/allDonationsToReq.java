package co.kirel.drops;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class allDonationsToReq extends AppCompatActivity {
    FirebaseFirestore firestore;
    private ArrayList<Donations> donsArraylist;
    private RecyclerView donrecview;
    String reqId;
    TextView tvreqId;
    allDonAdapter allDonAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_donations_to_req);
        firestore= FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        reqId = intent.getStringExtra("ReqId");
        tvreqId=findViewById(R.id.tvreqId);
        tvreqId.setText(reqId);
        dataInitialize();
        donrecview=findViewById(R.id.alldonrecview);
        donrecview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        donrecview.setHasFixedSize(true);
        allDonAdapter = new allDonAdapter(getApplicationContext(),donsArraylist);
        donrecview.setAdapter(allDonAdapter);
        allDonAdapter.notifyDataSetChanged();
    }
    private void dataInitialize() {
        donsArraylist = new ArrayList<>(); //DON'T DELETE

        //Try Code
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                firestore.collection("Donations")
                        .whereEqualTo("RequirementId",reqId)
                        .whereEqualTo("DonationStatus","Yes")
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                                if (error!= null)
                                {
                                    Toast.makeText(allDonationsToReq.this, "ERROR", Toast.LENGTH_SHORT).show();
                                    Log.e("Firestore error",error.getMessage());
                                    return;
                                }

                                for (DocumentChange dc : value.getDocumentChanges())
                                {
                                    if (dc.getType() == DocumentChange.Type.ADDED)
                                    {
                                        donsArraylist.add(dc.getDocument().toObject(Donations.class));
                                    }
                                    allDonAdapter.notifyDataSetChanged();
                                }
                            }
                        });
            }
        }, 1000);
    }
}