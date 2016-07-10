package com.example.marek.fastestbeer;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * Created by Marek on 2016-05-31.
 */
public class ListRowViewHolder extends RecyclerView.ViewHolder {

    protected TextView score;
    protected RelativeLayout relativeLayout;


    public ListRowViewHolder(View view) {
        super(view);

        this.score = (TextView) view.findViewById(R.id.score);
        this.relativeLayout = (RelativeLayout) view.findViewById(R.id.row_layout);
        view.setClickable(true);
    }
}
