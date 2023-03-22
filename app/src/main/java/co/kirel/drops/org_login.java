package co.kirel.drops;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class org_login extends AppCompatActivity {
    EditText orgemail,orgpass,orgcode;
    Button signin;
    TextView newhere;
    String sorgemail,sorgpass,sorgcode,rsorgcode,Verified,honame;
    FirebaseAuth auth=FirebaseAuth.getInstance();
    FirebaseFirestore firestore=FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_login);

        orgemail=findViewById(R.id.org_email);
        orgpass=findViewById(R.id.org_pwd);
        orgcode=findViewById(R.id.org_code);
        newhere=findViewById(R.id.newhere);
        signin=findViewById(R.id.org_signin_btn);

        newhere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(org_login.this,org_signup.class);
                startActivity(i);
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sorgemail = orgemail.getText().toString();
                sorgpass = orgpass.getText().toString();
                sorgcode = orgcode.getText().toString();
                String codefrst= sorgcode.substring(0,1);

//                Toast.makeText(org_login.this, codefrst, Toast.LENGTH_SHORT).show();

                if (codefrst.equals("O"))
                {
                    DocumentReference docRef = firestore.collection("Organization").document(sorgemail);
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    rsorgcode = document.getString("Organization Code");
                                    honame=document.getString("Organization Name");
                                    Verified= document.getString("Verified");
//                                    Toast.makeText(org_login.this, rsorgcode, Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.d("error", "No such document");
                                }
                            } else {
                                Log.d("error", "get failed with ", task.getException());
                            }
                        }
                    });
                }
                else if (codefrst.equals("H"))
                {
                    DocumentReference docRef = firestore.collection("Hospital").document(sorgemail);
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    rsorgcode = document.getString("Hospital Code");
                                    honame=document.getString("Hospital Name");
                                    Verified= document.getString("Verified");
//                                    Toast.makeText(org_login.this, rsorgcode, Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.d("error", "No such document");
                                }
                            } else {
                                Log.d("error", "get failed with ", task.getException());
                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(org_login.this, "Invalid Organization code", Toast.LENGTH_SHORT).show();
                }




                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        Toast.makeText(org_login.this, rsorgcode, Toast.LENGTH_SHORT).show();
                        // Actions to do after 5 seconds
                        if (rsorgcode.equals(sorgcode))
                        {
                            auth.signInWithEmailAndPassword(sorgemail, sorgpass)
                                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                        @Override
                                        public void onSuccess(AuthResult authResult) {

                                                    // Actions to do after 5 seconds
                                                    if(Verified.equals("yes")) {
                                                        Toast.makeText(org_login.this, "SignIn Succesfull", Toast.LENGTH_SHORT).show();
                                                        Intent i=new Intent(org_login.this,hospital_home.class);
                                                        i.putExtra("Email",sorgemail);
                                                        i.putExtra("honame",honame);
                                                        i.putExtra("code",rsorgcode);
                                                        startActivity(i);
                                                        finish();
                                                    }else{
                                                        Toast.makeText(org_login.this, "You account is not verified", Toast.LENGTH_SHORT).show();
                                                    }

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(org_login.this, "SignIn Unsuccesfull", Toast.LENGTH_SHORT).show();
                                            Log.e("error", String.valueOf(e));
                                        }
                                    });
                        }else
                        {
                            Toast.makeText(org_login.this, "Organization Code Mismatch", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, 3000);
            }
        });
    }
}