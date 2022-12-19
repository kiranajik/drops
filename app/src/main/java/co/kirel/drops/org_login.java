package co.kirel.drops;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class org_login extends AppCompatActivity {
    EditText orgemail,orgpass;
    Button signin;
    String sorgemail,sorgpass;
    FirebaseAuth auth=FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_login);

        orgemail=findViewById(R.id.org_email);
        orgpass=findViewById(R.id.org_pwd);
        signin=findViewById(R.id.org_signin_btn);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sorgemail=orgemail.getText().toString();
                sorgpass=orgpass.getText().toString();

                auth.signInWithEmailAndPassword(sorgemail,sorgpass)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Toast.makeText(org_login.this, "SignIn Succesfull", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(org_login.this, "SignIn Unsuccesfull", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}