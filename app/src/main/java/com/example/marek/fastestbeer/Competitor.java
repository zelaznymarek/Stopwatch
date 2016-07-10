package com.example.marek.fastestbeer;

import io.realm.RealmObject;
import io.realm.annotations.Required;

/**
 * Created by Marek on 2016-07-10.
 */
public class Competitor extends RealmObject {

    @Required
    private String mName;
    private long mTime;

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public long getmTime() {
        return mTime;
    }

    public void setmTime(long mTime) {
        this.mTime = mTime;
    }
}
