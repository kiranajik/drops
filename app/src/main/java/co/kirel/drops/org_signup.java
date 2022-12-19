package co.kirel.drops;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class org_signup extends AppCompatActivity {
    ImageView next;
    EditText orgemail,orgpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_signup);
        next=findViewById(R.id.orgsignupnext);
        orgemail=findViewById(R.id.org_emailid);
        orgpass=findViewById(R.id.org_passwd);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(org_signup.this,org_reg.class);
                i.putExtra("OrgEmail",orgemail.getText().toString());
                i.putExtra("OrgPassword",orgpass.getText().toString());
                startActivity(i);
            }
        });
    }
}