package com.example.marek.fastestbeer;


import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class Scores extends Activity {

    private List<Competitor> mCompetitorList;
    private MyRecyclerAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private Realm mRealm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        mRealm = Realm.getInstance(new RealmConfiguration.Builder(this)
                .name(MainActivity.REALM_NAME)
                .build());

        mCompetitorList = getScores();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).color(Color.BLACK).build());

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        updateList();

    }

    public void updateList(){

        mAdapter = new MyRecyclerAdapter(Scores.this, mCompetitorList);
        mRecyclerView.setAdapter(mAdapter);

    }

    public List<Competitor> getScores(){

        return mRealm.where(Competitor.class).findAllSorted("mTime");

    }

}
