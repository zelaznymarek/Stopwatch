package com.example.marek.fastestbeer;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.SystemClock;

public class MyService extends Service {

    private final int STOP = 0;
    private final int START = 1;
    private final int RESET = 2;
    private boolean isRunning = false;
    private long startTime = 0;
    private long timeInMilliseconds = 0;
    private long timeSwapBuff = 0;
    private long updatedTime;
    private Handler handler = new Handler();

    private final Messenger mMessenger = new Messenger(new IncomingHandler());

    public MyService() {

    }

    @Override
    public void onCreate(){
        super.onCreate();

    }


    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    public long getUpdatedTime(){
        return updatedTime;
    }

    public void startStop(){

        if (isRunning) {
            timeSwapBuff += timeInMilliseconds;
            handler.removeCallbacks(updateTimer);
            isRunning = false;
        } else {
            startTime = SystemClock.uptimeMillis();
            handler.postDelayed(updateTimer, 0);
            isRunning = true;
        }
    }


    public void reset(){

        isRunning=false;
        handler.removeCallbacks(updateTimer);
        startTime = 0L;
        timeInMilliseconds = 0L;
        timeSwapBuff = 0L;
        updatedTime = 0L;

    }




    public Runnable updateTimer = new Runnable() {
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;
            handler.postDelayed(this, 0);
        }
    };


    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case STOP:
                    isRunning = true;
                    startStop();
                    break;
                case START:
                    isRunning = false;
                    startStop();
                    break;
                case RESET:
                    reset();
                    break;
                default:
                    super.handleMessage(msg);
            }

        }
    }



}
