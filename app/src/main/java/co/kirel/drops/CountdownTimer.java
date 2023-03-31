package co.kirel.drops;

import android.os.CountDownTimer;

public class CountdownTimer extends CountDownTimer {
    int seconds;

    public CountdownTimer(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        // Display the remaining time in seconds
        seconds = (int) (millisUntilFinished / 1000);
    }

    @Override
    public void onFinish() {
        this.cancel();
    }
    public int getTime(){
        return seconds;
    }
}
