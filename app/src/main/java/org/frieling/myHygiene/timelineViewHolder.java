package org.frieling.myHygiene;

import android.view.View;

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
