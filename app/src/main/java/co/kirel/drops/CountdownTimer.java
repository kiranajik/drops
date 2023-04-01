package co.kirel.drops;

import android.os.CountDownTimer;

public class CountdownTimer extends CountDownTimer {
    int seconds;
    private boolean hasFinished = false;

    public CountdownTimer(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }
    public boolean hasFinished() {
        return hasFinished;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        // Display the remaining time in seconds
        seconds = (int) (millisUntilFinished / 1000);
    }

    @Override
    public void onFinish() {
        hasFinished = true;
        this.cancel();
    }
    public int getTime(){
        return seconds;
    }
}
