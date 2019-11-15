package org.altbeacon.beaconreference;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.vipulasri.timelineview.TimelineView;

public class timelineViewHolder extends RecyclerView.ViewHolder {
    public TimelineView mTimelineView;

    public timelineViewHolder(View itemView, int viewType) {
        super(itemView);
        mTimelineView = (TimelineView) itemView.findViewById(R.id.timeline);
        mTimelineView.initLine(viewType);
    }


}
