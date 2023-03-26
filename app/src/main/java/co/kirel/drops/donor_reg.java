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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class donor_reg extends AppCompatActivity {
    EditText name,addr,age,dob,bloodgrp,phno,adharno;
    Button signup;
    String ystrdayDate;
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
}