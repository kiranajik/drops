package co.kirel.drops;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class rewardData extends AppCompatActivity {
    TextView company,gift;
    String Co,Gi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_data);
        company = findViewById(R.id.CoCo);
        gift = findViewById(R.id.GiGi);
        Co = getIntent().getStringExtra("company");
        Gi = getIntent().getStringExtra("gift");
        company.setText(Co);
        gift.setText(Gi);
    }
}