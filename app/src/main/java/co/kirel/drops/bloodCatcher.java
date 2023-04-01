package co.kirel.drops;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

public class bloodCatcher extends AppCompatActivity {
    private Integer backPressed=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_catcher);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public void startGame(View view) {
        backPressed=0;
        GameView gameView = new GameView(this);
        gameView.startGame();
        setContentView(gameView);
    }
    @Override
    public void onBackPressed() {

        if (backPressed<1) {
            backPressed+=1;
            Toast.makeText(this, "Press back again to exit",
                    Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();       // bye
        }
    }

}