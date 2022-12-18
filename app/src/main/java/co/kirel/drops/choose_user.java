package co.kirel.drops;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class choose_user extends AppCompatActivity {

    Button org_choose_btn, donor_choose_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_user);

        org_choose_btn = findViewById(R.id.org_choose_btn);
        donor_choose_btn = findViewById(R.id.donor_choose_btn);

        org_choose_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(choose_user.this,org_auth.class);
                startActivity(i);
            }
        });
        donor_choose_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(choose_user.this,donor_auth.class);
                startActivity(i);
            }
        });


    }
}