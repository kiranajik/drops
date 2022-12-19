package co.kirel.drops;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class donor_signup extends AppCompatActivity {
    ImageView next;
    EditText dnremail,dnrpaswd;
    String semail,spaswd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_signup);
        next=findViewById(R.id.imageView2);
        dnremail=findViewById(R.id.donor_email);
        dnrpaswd=findViewById(R.id.donor_pwd);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                semail=dnremail.getText().toString();
                spaswd=dnrpaswd.getText().toString();
                Toast.makeText(donor_signup.this, "Yes", Toast.LENGTH_SHORT).show();
                Intent i=new Intent(donor_signup.this,donor_reg.class);
                i.putExtra("Email",semail);
                i.putExtra("Password",spaswd);
                startActivity(i);
            }
        });
    }
}