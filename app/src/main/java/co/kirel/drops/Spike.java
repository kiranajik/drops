package co.kirel.drops;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

public class Spike {
    Bitmap spike[] = new Bitmap[1];
    int spikeX, spikeY, spikeVelocity;
    Random random;

    public Spike(Context context){
        spike[0]= BitmapFactory.decodeResource(context.getResources(),R.drawable.blood);
        random = new Random();
        resetPosition();

    }
    public Bitmap getSpike(){
        return spike[0];
    }

    public int getSpikeWidth() {
        return spike[0].getWidth();
    }
    public int getSpikeHeight(){
        return spike[0].getHeight();
    }

    public void resetPosition() {
        spikeX = random.nextInt(GameView.dWidth - getSpikeWidth());
        spikeY = -200 + random.nextInt(600)* -1;
        spikeVelocity = 35 + random.nextInt(16);
    }
}
