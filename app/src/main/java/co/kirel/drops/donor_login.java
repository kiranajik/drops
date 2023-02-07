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

public class donor_login extends AppCompatActivity {
    EditText email,paswd;
    Button signin;
    String semail,spaswd,Verified;
    TextView newhere;
    FirebaseFirestore firestore;
    private FirebaseAuth auth=FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_login);

        email=findViewById(R.id.user_email);
        paswd=findViewById(R.id.user_pwd);
        newhere=findViewById(R.id.newhere);
        signin=findViewById(R.id.donor_signin_btn);
        firestore= FirebaseFirestore.getInstance();

        newhere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(donor_login.this,donor_signup.class);
                startActivity(i);
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                semail=email.getText().toString();
                spaswd=paswd.getText().toString();

                auth.signInWithEmailAndPassword(semail,spaswd)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                DocumentReference docRef = firestore.collection("Donor").document(semail);
                                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                Verified= document.getString("Verified");
                                            } else {
                                                Log.d("error", "No such document");
                                            }
                                        } else {
                                            Log.d("error", "get failed with ", task.getException());
                                        }
                                    }
                                });
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        // Actions to do after 5 seconds
                                        if(Verified.equals("yes")) {
                                            Toast.makeText(donor_login.this, "Successfully Logined", Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(donor_login.this, donor_home.class);
                                            i.putExtra("Email", semail);
                                            startActivity(i);
                                            finish();
                                        }else{
                                            Toast.makeText(donor_login.this, "Donar not verified", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }, 2000);

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(donor_login.this, "Failed Login", Toast.LENGTH_SHORT).show();
                                Log.d("login",e.getMessage());
                            }
                        });
            }
        });

    }

    private void reload() {
    }
}