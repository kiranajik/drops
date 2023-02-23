package co.kirel.drops;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class org_auth extends AppCompatActivity {

    Button org_login, org_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_auth);


        org_login = findViewById(R.id.org_login_btn);
        org_signup = findViewById((R.id.bconfirm));

        org_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(org_auth.this, org_login.class);
                startActivity(i);
            }
        });
        org_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(org_auth.this, org_signup.class);
                startActivity(i);
            }
        });
    }
}