package co.kirel.drops;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Referrals extends AppCompatActivity {

    TextView ref_code_owner, ref_code_view;
    ImageView copy, share;

    private RecyclerView recyclerView;
    private referal_adapter adapter;
    ArrayList<Donor> dataList;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    String code = "";
    String rfc="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referrals);

        ref_code_owner = findViewById(R.id.ref_code_owner);
        ref_code_view = findViewById(R.id.ref_code_view);
        copy = findViewById(R.id.imageView20);
        share = findViewById(R.id.imageView21);
        //logged in user
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String email_ID = auth.getCurrentUser().getEmail();


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Donor").document(email_ID);
        CollectionReference rcf = db.collection("Donor");

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String name = documentSnapshot.getString("Name");
                    ref_code_owner.setText(name);
                    Toast.makeText(Referrals.this, name, Toast.LENGTH_SHORT).show();

                    if(documentSnapshot.contains("referal_code") && !documentSnapshot.get("referal_code").equals(""))
                    {
                        String rcode = documentSnapshot.getString("referal_code");
                        ref_code_view.setText(rcode);
                        Toast.makeText(Referrals.this, "Share Your Referral Code with friends!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        String gen_rcode = generateUniqueCode();
                        Map<String, Object> new_rcode = new HashMap<>();
                        new_rcode.put("referal_code", gen_rcode);
                        docRef.update(new_rcode).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                ref_code_view.setText(gen_rcode);
                                Toast.makeText(Referrals.this, "Your Referral Code Is Generated Successfully", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {}
                        });
                    }
                }
            }
        });

        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Reward Value",ref_code_view.getText().toString());
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(getApplicationContext(), "Referral Code Copied To Clipboard", Toast.LENGTH_SHORT).show();
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Sharing Referral Code"); // Optional subject
                intent.putExtra(Intent.EXTRA_TEXT, "Earn Life Coins While Your Friend Signs & Make their first donation!\n"+"Referral Code: "+ref_code_view.getText().toString());
                startActivity(Intent.createChooser(intent, "Refer a Friend"));

            }
        });

        dataInitialize();

        recyclerView =findViewById(R.id.rcv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapter = new referal_adapter(this,dataList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    public String generateUniqueCode() {

        Toast.makeText(this, "fun called", Toast.LENGTH_SHORT).show();
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        FirebaseFirestore db = FirebaseFirestore.getInstance();

            for (int i = 0; i < 6; i++) {
                int index = (int) (Math.random() * alphabet.length());
                code += alphabet.charAt(index);
            }

        return code;
    }

    private void dataInitialize() {
        dataList = new ArrayList<>(); //DON'T DELETE

        //Try Code
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                firestore.collection("Donor")
                        .whereEqualTo("Referred_by",ref_code_view.getText().toString())
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                                if (error!= null)
                                {
                                    Toast.makeText(Referrals.this, "Error!", Toast.LENGTH_SHORT).show();
                                    return;

                                }

                                for (DocumentChange dc : value.getDocumentChanges())
                                {
                                    if (dc.getType() == DocumentChange.Type.ADDED)
                                    {
                                        dataList.add(dc.getDocument().toObject(Donor.class));
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        });
            }
        }, 1000);
    }




}