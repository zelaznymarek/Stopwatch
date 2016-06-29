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

public class MainActivity extends Activity {

    private final int STOP_TIME = 0;
    private final int START_TIME = 1;
    private final int RESET_TIME = 2;
    private MyService myService;
    private TextView time;
    private int secs = 0;
    private int mins = 0;
    private int hrs = 0;
    private int millis = 0;
    private long currentTime = 0L;
    private boolean isBound = false;
    private boolean isRunning = false;
    private Handler handler = new Handler();
    private Messenger myMessenger;



    public Runnable getTime = new Runnable() {
        @Override
        public void run() {
            try {
                currentTime = myService.getUpdatedTime();
            } catch (NullPointerException e){
                time.setText("00:00:00");
            }
            secs = (int) (currentTime / 1000);
            mins = secs / 60;
            secs = secs % 60;
            millis = (int) (currentTime % 1000);
            time.setText("" + mins + ":" + String.format("%02d", secs) + ":"
                    + String.format("%03d", millis));
            handler.postDelayed(this, 0);
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(getApplicationContext(), MyService.class);
        startService(intent);
        bindService(intent, myConnection, Context.BIND_AUTO_CREATE);

        time = (TextView) findViewById(R.id.timer);

        findViewById(R.id.fab_playPause).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Message msg;

                if(isRunning) {
                    msg = Message.obtain(null, STOP_TIME, 0, 0);
                } else {
                    msg = Message.obtain(null, START_TIME, 0, 0);
                }

                Bundle bundle = new Bundle();
                bundle.putInt("action", START_TIME);
                msg.setData(bundle);

                try {
                    myMessenger.send(msg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.fab_reset).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Message msg = Message.obtain(null, RESET_TIME, 0, 0);
                try {
                    myMessenger.send(msg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.fab_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        findViewById(R.id.fab_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        handler.postDelayed(getTime, 0);

    }

    private ServiceConnection myConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            isBound = true;
            myMessenger = new Messenger(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

            myMessenger = null;
            isBound = false;
        }
    };


}

