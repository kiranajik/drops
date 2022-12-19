package co.kirel.drops;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class donor_login extends AppCompatActivity {
    EditText email,paswd;
    Button signin;
    String semail,spaswd;
    private FirebaseAuth auth=FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_login);

        email=findViewById(R.id.user_email);
        paswd=findViewById(R.id.user_pwd);
        signin=findViewById(R.id.donor_signin_btn);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                semail=email.getText().toString();
                spaswd=paswd.getText().toString();

                auth.signInWithEmailAndPassword(semail,spaswd)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Toast.makeText(donor_login.this, "Successfully Logined", Toast.LENGTH_SHORT).show();
                                Intent i=new Intent(donor_login.this,donor_home.class);
                                startActivity(i);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(donor_login.this, "Failed Login", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

    }

    private void reload() {
    }
}