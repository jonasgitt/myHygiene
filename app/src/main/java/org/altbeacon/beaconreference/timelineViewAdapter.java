package org.altbeacon.beaconreference;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.vipulasri.timelineview.TimelineView;

import org.w3c.dom.Text;

import java.util.List;

public class timelineViewAdapter extends RecyclerView.Adapter<timelineViewAdapter.timelineViewHolder> {

    private List<timelineViewModel> mDataList;
    private LayoutInflater mInflater;

    int mExpandedPosition=-1;

    timelineViewAdapter(Context context, List<timelineViewModel> data){
        this.mInflater = LayoutInflater.from(context);
        this.mDataList = data;
    }




    @NonNull
    @Override
    public timelineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_timeline, null);
        return new timelineViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull timelineViewHolder holder, int position) {

        timelineViewModel mModel = mDataList.get(position);

        //do stuff to change marker color

        if(!mModel.date.isEmpty()){
            holder.mDate.setVisibility(View.VISIBLE);
            holder.mDate.setText(mModel.date);
        }
        holder.mMessage.setText(mModel.message);
        holder.mHHEs.setText(mModel.HHEtime);



        final boolean isExpanded = position==mExpandedPosition;
        holder.mHHEs.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.itemView.setActivated(isExpanded);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpandedPosition = isExpanded ? -1:position;
                //TransitionManager.beginDelayedTransition(recyclerView);
                notifyDataSetChanged();
            }
        });
    }


    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());
    }


    @Override
    public int getItemCount() {
        return mDataList.size();
    }



    //
    public class timelineViewHolder extends RecyclerView.ViewHolder {
        TimelineView mTimelineView;
        TextView mDate;
        TextView mMessage;
        TextView mHHEs;

        timelineViewHolder(View itemView, int viewType) {
            super(itemView);


            mDate = (TextView) itemView.findViewById(R.id.text_timeline_date);
            mMessage = (TextView) itemView.findViewById(R.id.text_timeline_title);
            mHHEs = (TextView) itemView.findViewById(R.id.text_HHEs);
            mTimelineView = (TimelineView) itemView.findViewById(R.id.timeline);

            mTimelineView.initLine(viewType);
        }


    }
}
