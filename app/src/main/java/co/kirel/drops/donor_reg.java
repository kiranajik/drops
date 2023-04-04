package co.kirel.drops;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class donor_reg extends AppCompatActivity {
    EditText name,addr,age,dob,bloodgrp,phno,adharno,referred_by;
    Button signup;
    String documentId;
    String ystrdayDate;
    String referrer, referrer2;


    private FirebaseAuth auth=FirebaseAuth.getInstance();
    FirebaseFirestore firestore=FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_reg);
        name=findViewById(R.id.edName);
        addr=findViewById(R.id.edAddress);
        age=findViewById(R.id.edAge);
        dob=findViewById(R.id.edDob);
        bloodgrp=findViewById(R.id.edBloodGroup);
        phno=findViewById(R.id.edPhno);
        adharno=findViewById(R.id.edAdharno);
        signup=findViewById(R.id.dnr_signup_btn);
        referred_by=findViewById(R.id.referred_by);

        Intent intent = getIntent();
        String semail = intent.getStringExtra("DnrEmail");
        String spaswd = intent.getStringExtra("DnrPassword");

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        ystrdayDate= dateFormat.format(cal.getTime());

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                auth.createUserWithEmailAndPassword(semail,spaswd)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {

                                Map<String,Object> data= new HashMap<>();
                                data.put("Name",name.getText().toString());
                                data.put("Address",addr.getText().toString());
                                data.put("Age",age.getText().toString());
                                data.put("DOB",dob.getText().toString());
                                data.put("bloodgroup",bloodgrp.getText().toString());
                                data.put("phonenumber",phno.getText().toString());
                                data.put("Aadhaar Number",adharno.getText().toString());
                                data.put("Verified","no");
                                data.put("nxtDntnDate",ystrdayDate);
                                data.put("Role","DONOR");
                                data.put("LifeCoins",0);

                                if(referred_by.getText().toString().length()==6)
                                {
                                    data.put("Referred_by",referred_by.getText().toString());
                                    data.put("Referral_status","1");
                                    data.put("LifeCoins",50);


                                    findDocumentNameByFieldValue("Donor", "referal_code", referred_by.getText().toString(), new OnDocumentNameCallback() {
                                        @Override
                                        public void onDocumentNameCallback(String documentName) {
                                            if (documentName != null) {
                                                // documentName is the ID of the matching document
                                                referrer2 = documentName;
                                                add_coins(referrer2);
                                                Toast.makeText(donor_reg.this, referrer2, Toast.LENGTH_SHORT).show();
                                            } else {

                                            }
                                        }
                                    });


                                }
                                else
                                {
                                    data.put("Referred_by","none");
                                    data.put("Referral_status","0");
                                    data.put("LifeCoins",0);
                                }


                                firestore.collection("Donor").document(semail).set(data)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(donor_reg.this, "Yes", Toast.LENGTH_SHORT).show();
                                                Intent i= new Intent(donor_reg.this,donor_login.class);
                                                startActivity(i);
                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(donor_reg.this, "FireStoreNo", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(donor_reg.this, "AuthNo", Toast.LENGTH_SHORT).show();
                                Log.d("error",e.toString());
                            }
                        });

            }
        });


    }

    public void findDocumentNameByFieldValue(String collectionName, String fieldName, String fieldValue, final OnDocumentNameCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(collectionName)
                .whereEqualTo(fieldName, fieldValue)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String documentName = documentSnapshot.getId();
                        callback.onDocumentNameCallback(documentName);
                        return;
                    }
                    // if no matching documents found, call callback with null
                    callback.onDocumentNameCallback(null);
                })
                .addOnFailureListener(e -> {
                    callback.onDocumentNameCallback(null);
                });
    }

    public interface OnDocumentNameCallback {
        void onDocumentNameCallback(String documentName);
    }

    public void add_coins(String user)
    {
        DocumentReference docRef = firestore.collection("Donor").document(user);

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    // Get the current value of the "score" field
                    int coins = documentSnapshot.getLong("LifeCoins").intValue();

                    if (coins >= 0) {
                        // Add 50 to the current value of the "score" field
                        int ncoins = coins + 50;

                        // Update the "score" field in Firestore with the new value
                        docRef.update("LifeCoins", ncoins)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("T", "Score successfully updated!");
                                        Toast.makeText(donor_reg.this, "Score successfully updated!", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("T", "Error updating score", e);
                                    }
                                });
                    } else {
                        Log.d("T", "Score field is null");
                    }
                } else {
                    Log.d("T", "No such document");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("T", "Error getting document: " + e);
            }
        });

    }
}