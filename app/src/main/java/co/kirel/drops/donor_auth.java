package co.kirel.drops;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class donor_auth extends AppCompatActivity {

    Button donor_login, donor_signup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_auth);

        donor_login = findViewById(R.id.donor_login_btn);
        donor_signup = findViewById((R.id.donor_signup_btn));

        donor_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(donor_auth.this, donor_signup.class);
                startActivity(i);
            }
        });
        donor_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(donor_auth.this, donor_login.class);
                startActivity(i);
            }
        });

    }
}