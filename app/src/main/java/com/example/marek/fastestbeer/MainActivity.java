package com.example.marek.fastestbeer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainActivity extends Activity {

    public static final String REALM_NAME = "FastBeer";
    public static Handler sHandler;
    private int secs = 0;
    private int mins = 0;
    private int millis = 0;
    private long currentTime = 0L;
    private boolean isBound = false;
    private boolean isRuning = false;
    private MyService myService;
    private Intent mIntent;
    private Realm mRealm;
    private AlertDialog.Builder mDialogBuilder;
    @BindView(R.id.timer)
    TextView time;
    @BindView(R.id.fab_playPause)
    FloatingActionButton fabPlayPause;



    @OnClick(R.id.fab_playPause)
    public void playPause() {

        myService.startStop();
        if(!isRuning){
            fabPlayPause.setImageResource(R.drawable.ic_pause);
            isRuning = true;
        } else if(isRuning){
            fabPlayPause.setImageResource(R.drawable.ic_start);
            isRuning = false;
        }

    }

    @OnClick(R.id.fab_reset)
    public void reset() {

        myService.reset();
        mins = 0;
        secs = 0;
        millis = 0;
        setTime();
        fabPlayPause.setImageResource(R.drawable.ic_start);
        isRuning=false;

    }

    @OnClick(R.id.fab_exit)
    public void exit() {
        myService.reset();
        stopService(mIntent);
        finishAffinity();
    }

    @OnClick(R.id.fab_save)
    public void save() {

        saveTime();

    }

    @OnClick(R.id.fab_scores)
    public void showScores() {
        mIntent = new Intent(this, Scores.class);
        startActivity(mIntent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mRealm = Realm.getInstance(new RealmConfiguration.Builder(this)
                        .name(MainActivity.REALM_NAME)
                        .build());

        Global.lastId = getLastId();

        mIntent = new Intent(this, MyService.class);
        startService(mIntent);
        bindService(mIntent, myConnection, Context.BIND_AUTO_CREATE);

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
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
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

    public void saveTime() {

        mDialogBuilder = new AlertDialog.Builder(this);
        final EditText editText = new EditText(this);

        mDialogBuilder.setTitle("Drinker name");
        mDialogBuilder.setMessage("Who was drinking?");
        mDialogBuilder.setView(editText);
        mDialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which){

                mRealm.beginTransaction();
                Competitor competitor = mRealm.createObject(Competitor.class);

                competitor.setId(Global.lastId+1);
                competitor.setmName(editText.getText().toString());
                competitor.setmTime(currentTime);

                mRealm.commitTransaction();
                Toast.makeText(getApplicationContext(), "Saved!", Toast.LENGTH_SHORT).show();

            }
        });

        mDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }

        });

        AlertDialog saveDialog = mDialogBuilder.create();
        saveDialog.show();

    }

    public int getLastId(){
        mRealm.beginTransaction();
        RealmResults<Competitor> results = mRealm.where(Competitor.class).findAllSorted("id");
        mRealm.commitTransaction();

        if(results.isEmpty()){

            return 0;

        } else {

            return results.get(results.size() - 1).getId();
        }
    }

}

