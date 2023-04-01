package co.kirel.drops;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.Random;

public class GameView extends View {
    Bitmap background, ground, catcher;
    Rect rectBackground, rectGround;
    Context context;
    Handler handler;
    final long UPDATE_MILLIS = 30;
    Runnable runnable;
    Paint textPaint = new Paint();
    Paint timePaint = new Paint();
    float TEXT_SIZE = 120;
    int points = 0;
    int time;

    static int dWidth, dHeight;
    Random random;
    float catcherX, catcherY;
    float oldX;
    float oldCatcherX;
    Spike spike1,spike2, spike3;
    CountdownTimer countdownTimer;
    boolean timerFinished;

    public void startGame() {
        // Start the game and create a new CountdownTimer with a 60 second countdown and the current points value
        countdownTimer = new CountdownTimer( 30000, 1000);
        countdownTimer.start();// Start the timer
    }
    public GameView(Context context) {
        super(context);
        this.context = context;
        background = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
        ground = BitmapFactory.decodeResource(getResources(), R.drawable.ground);
        catcher = BitmapFactory.decodeResource(getResources(), R.drawable.catcher);
        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        dWidth = size.x;
        dHeight = size.y;
        rectBackground = new Rect(0,0,dWidth,dHeight);
        rectGround = new Rect(0,dHeight - ground.getHeight(),dWidth,dHeight);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
        textPaint.setColor(Color.rgb(255,165,0));
        textPaint.setTextSize(TEXT_SIZE);
        textPaint.setTextAlign(Paint.Align.LEFT);
        timePaint.setColor(Color.BLACK);
        timePaint.setTextSize(TEXT_SIZE);
        timePaint.setTextAlign(Paint.Align.RIGHT);
        random = new Random();
        catcherX = dWidth/2 - catcher.getWidth() / 2;
        catcherY = dHeight - catcher.getHeight()*2;
        spike1 = new Spike(context);
        spike2 = new Spike(context);
        spike3 = new Spike(context);
    }
    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        canvas.drawBitmap(background, null, rectBackground, null);
        canvas.drawBitmap(ground, null, rectGround, null);
        canvas.drawBitmap(catcher, catcherX, catcherY, null);
        if(spike1.spikeY + spike1.getSpikeHeight() <= dHeight - ground.getHeight()){
            canvas.drawBitmap(spike1.getSpike(), spike1.spikeX, spike1.spikeY, null);
            spike1.spikeY += spike1.spikeVelocity;
        }
        else{
            spike1.resetPosition();
        }
        if(spike1.spikeX + spike1.getSpikeWidth() >= catcherX
                && spike1.spikeX <= catcherX + catcher.getWidth()
                && spike1.spikeY + spike1.getSpikeWidth()>= catcherY
                && spike1.spikeY + spike1.getSpikeWidth() <= catcherY + catcher.getHeight()){
            points += 10;
            spike1.resetPosition();
        }
        if(spike2.spikeY + spike2.getSpikeHeight() <= dHeight - ground.getHeight()){
            canvas.drawBitmap(spike2.getSpike(), spike2.spikeX, spike2.spikeY, null);
            spike2.spikeY += spike2.spikeVelocity;
        }
        else{
            spike2.resetPosition();
        }
        if(spike2.spikeX + spike2.getSpikeWidth() >= catcherX
                && spike2.spikeX <= catcherX + catcher.getWidth()
                && spike2.spikeY + spike2.getSpikeWidth()>= catcherY
                && spike2.spikeY + spike2.getSpikeWidth() <= catcherY + catcher.getHeight()){
            points += 10;
            spike2.resetPosition();
        }
        if(spike3.spikeY + spike3.getSpikeHeight() <= dHeight - ground.getHeight()){
            canvas.drawBitmap(spike3.getSpike(), spike3.spikeX, spike3.spikeY, null);
            spike3.spikeY += spike3.spikeVelocity;
        }
        else{
            spike3.resetPosition();
        }
        if(spike3.spikeX + spike3.getSpikeWidth() >= catcherX
                && spike3.spikeX <= catcherX + catcher.getWidth()
                && spike3.spikeY + spike3.getSpikeWidth()>= catcherY
                && spike3.spikeY + spike3.getSpikeWidth() <= catcherY + catcher.getHeight()){
            points += 10;
            spike3.resetPosition();
        }
        canvas.drawText(""+points,20, TEXT_SIZE, textPaint);
        canvas.drawText(""+time,dWidth-300, TEXT_SIZE, timePaint);
        timerFinished = countdownTimer.hasFinished();
        if(timerFinished){
            Intent i =new Intent(context, GameOver.class);
            i.putExtra("points",points);
            context.startActivity(i);
        }
        time = countdownTimer.getTime();
        handler.postDelayed(runnable, UPDATE_MILLIS);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        float touchX = event.getX();
        float touchY = event.getY();
        if(touchY >= catcherY){
            int action = event.getAction();
            if (action == MotionEvent.ACTION_DOWN){
                oldX = event.getX();
                oldCatcherX = catcherX;
            }
            if(action == MotionEvent.ACTION_MOVE){
                float shift = oldX - touchX;
                float newCatcherX = oldCatcherX - shift;
                if(newCatcherX <= 0){
                    catcherX=0;
                }
                else if(newCatcherX >= dWidth - catcher.getWidth()){
                    catcherX = dWidth - catcher.getWidth();
                }
                else{
                    catcherX = newCatcherX;
                }
            }
        }
        return true;
    }

}
