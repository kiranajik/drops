package co.kirel.drops;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;


public class myRewardData extends AppCompatActivity {
    TextView company,gift,rewcode;
    ImageView logo,copy;
    String Co,Gi,code;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reward_data);
        company = findViewById(R.id.CoCo);
        gift = findViewById(R.id.GiGi);
        rewcode = findViewById(R.id.RewCode);
        logo = findViewById(R.id.imageView15);
        copy = findViewById(R.id.imageView11);
        Co = getIntent().getStringExtra("company");
        Gi = getIntent().getStringExtra("gift");
        code = getIntent().getStringExtra("code");
        Context c = getApplicationContext();
        int id = c.getResources().getIdentifier("drawable/"+Co, null, c.getPackageName());


        logo.setImageResource(id);
        company.setText(Co.toUpperCase());
        gift.setText(Gi);
        rewcode.setText(code);

        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Reward Value",code);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(getApplicationContext(), "The Reward Code Copied To Clipboard", Toast.LENGTH_SHORT).show();
            }
        });

    }
}