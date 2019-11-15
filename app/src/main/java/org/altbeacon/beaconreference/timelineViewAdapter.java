package org.altbeacon.beaconreference;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.vipulasri.timelineview.TimelineView;

public class timelineViewAdapter extends RecyclerView.Adapter {

    @NonNull
    @Override
    public timelineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_timeline, null);
        return new timelineViewHolder(view, viewType);
    }

    @Override
    //This method is called for each ViewHolder to bind it to the adapter. This is where we will pass our data to our ViewHolder.
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());
    }


    @Override
    public int getItemCount() {
        return 0;
    }
}
