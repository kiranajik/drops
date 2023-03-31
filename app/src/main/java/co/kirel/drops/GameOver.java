package co.kirel.drops;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameOver extends AppCompatActivity {
    Button over;
    TextView points;
    Integer p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        Intent i = getIntent();
        p = i.getIntExtra("points",0);
        points = findViewById(R.id.pointstv);
        points.setText(p.toString()+" Points");
        over = findViewById(R.id.endbtn);
        over.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(GameOver.this,donor_home.class);
                startActivity(i);
            }
        });
    }
}