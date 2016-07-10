package com.example.marek.fastestbeer;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity {

    public static Handler sHandler;
    private final int playPause = 0;
    private final int reset = 1;
    private int secs = 0;
    private int mins = 0;
    private int millis = 0;
    private long currentTime = 0L;
    private boolean isBound = false;
    private MyService myService;
    private Intent intent;
    @BindView(R.id.timer)
    TextView time;



    @OnClick(R.id.fab_playPause)
    public void playPause() {

        myService.startStop();

    }

    @OnClick(R.id.fab_reset)
    public void reset() {

        myService.reset();
        mins = 0;
        secs = 0;
        millis = 0;
        setTime();

    }

    @OnClick(R.id.fab_exit)
    public void exit() {
        onDestroy();
    }

    @OnClick(R.id.fab_save)
    public void save() {


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        intent = new Intent(this, MyService.class);

        MainActivity.sHandler = new Handler() {

            @Override
            public void handleMessage(Message timeMsg) {
                super.handleMessage(timeMsg);

                currentTime = Long.valueOf(timeMsg.obj.toString());

                secs = (int) (currentTime / 1000);
                mins = secs / 60;
                secs = secs % 60;
                millis = (int) (currentTime % 1000);
                setTime();
            }
        };


    }

    @Override
    protected void onResume() {
        super.onResume();

        bindService(intent, myConnection, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(intent);
        finishAffinity();

    }

    private ServiceConnection myConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            MyService.LocalBinder binder = (MyService.LocalBinder) service;
            myService = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    public void setTime() {
        time.setText("" + mins + ":" + String.format("%02d", secs) + ":"
                + String.format("%03d", millis));
    }

}

