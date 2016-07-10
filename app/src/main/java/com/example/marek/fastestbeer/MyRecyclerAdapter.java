package com.example.marek.fastestbeer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Marek on 2016-06-01.
 */
public class MyRecyclerAdapter extends RecyclerView.Adapter<ListRowViewHolder> {

    private List<Competitor> mCompetitorList;
    private Competitor mCompetitor;
    private Context mContext;
    private int focusedItem = 0;

    public MyRecyclerAdapter(Context context, List<Competitor> listItemsList){
        this.mContext = context;
        this.mCompetitorList = listItemsList;
    }

    @Override
    public ListRowViewHolder onCreateViewHolder(final ViewGroup viewGroup, int position){
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.competitor, null);
        ListRowViewHolder holder = new ListRowViewHolder(v);


        return holder;
    }

    @Override
    public void onBindViewHolder(final ListRowViewHolder listRowViewHolder, int position){
        Competitor mCompetitor = mCompetitorList.get(position);
        listRowViewHolder.itemView.setSelected(focusedItem == position);

        listRowViewHolder.getLayoutPosition();

        long timeInMilis = mCompetitor.getmTime();
        int secs = (int) (timeInMilis / 1000);
        int mins = secs / 60;
        secs = secs % 60;
        int millis = (int) (timeInMilis % 1000);

        String drinkScore = mCompetitor.getmName() + " - " + mins + ":" + String.format("%02d", secs)
                + ":" + String.format("%03d", millis);

        listRowViewHolder.score.setText(Html.fromHtml(drinkScore));

    }



    public int getItemCount(){
        return (null != mCompetitorList ? mCompetitorList.size() : 0);
    }

}
