package co.kirel.drops;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;


public class rewardData extends AppCompatActivity {
    TextView company,gift,code;
    String ccode;
    String Co,Gi,email;
    Integer myCoins,cost;

    ImageView logo,copy;
    Button redbtn;
    FirebaseFirestore firestore=FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_data);
        redbtn = findViewById(R.id.redbtn);
        company = findViewById(R.id.CoCo);
        gift = findViewById(R.id.GiGi);
        code = findViewById(R.id.RewCode);
        copy = findViewById(R.id.imageView11);
        logo = findViewById(R.id.imageView15);
        Co = getIntent().getStringExtra("company");
        Gi = getIntent().getStringExtra("gift");
        cost = getIntent().getIntExtra("cost",0);
        email = getIntent().getStringExtra("email");
        ccode = getIntent().getStringExtra("code");
        company.setText(Co);
        gift.setText(Gi);


        Context c = getApplicationContext();
        int id = c.getResources().getIdentifier("drawable/"+Co, null, c.getPackageName());
        logo.setImageResource(id);

        // Get a reference to the document
        DocumentReference docRef = firestore.collection("Donor").document(email);

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    // Get the data of a single field
                    myCoins = Math.toIntExact(documentSnapshot.getLong("LifeCoins"));
                }
            }
        });

        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Reward Value",ccode);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(getApplicationContext(), "The Reward Code Copied To Clipboard", Toast.LENGTH_SHORT).show();
            }
        });

        redbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cost <= myCoins){
                    Map<String,Object> data= new HashMap<>();
                    Map<String,Object> dataD= new HashMap<>();
                    data.put("Redeemer",email);
                    dataD.put("LifeCoins",myCoins-cost);



                    Query query = firestore.collection("Rewards").whereEqualTo("company", Co);

                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String documentId = document.getId();
                                    firestore.collection("Donor").document(email).update(dataD);
                                    firestore.collection("Rewards").document(documentId).update(data)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {

                                                    redbtn.setVisibility(View.INVISIBLE);
                                                    code.setText(ccode);
                                                    Toast.makeText(rewardData.this, "Reward Redeemed Successfully", Toast.LENGTH_SHORT).show();
                                                    Toast.makeText(rewardData.this, "Redirecting in 4 Seconds", Toast.LENGTH_SHORT).show();

                                                    Handler handler = new Handler();
                                                    handler.postDelayed(new Runnable() {
                                                        public void run() {
                                                            Intent i= new Intent(rewardData.this,donor_home.class);
                                                            i.putExtra("Email",email);
                                                            i.putExtra("source","reward");
                                                            startActivity(i);
                                                            finish();
                                                        }
                                                    }, 4000);

                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(rewardData.this, "FireStoreNo", Toast.LENGTH_SHORT).show();
                                                }
                                            });


                                    Log.d("E", "Document ID: " + documentId);
                                }
                            } else {
                                Log.d("E", "Error getting documents: ", task.getException());
                            }
                        }
                    });



                }
                else
                {
                    Toast.makeText(c, "There is No Enough Coins to Buy this reward", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}