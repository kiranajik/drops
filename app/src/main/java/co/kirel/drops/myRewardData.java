package co.kirel.drops;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;


public class myRewardData extends AppCompatActivity {
    TextView company,gift,rewcode;
    String Co,Gi,code;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reward_data);
        company = findViewById(R.id.CoCo);
        gift = findViewById(R.id.GiGi);
        rewcode = findViewById(R.id.RewCode);
        Co = getIntent().getStringExtra("company");
        Gi = getIntent().getStringExtra("gift");
        code = getIntent().getStringExtra("code");
        company.setText(Co);
        gift.setText(Gi);
        rewcode.setText(code);

    }
}