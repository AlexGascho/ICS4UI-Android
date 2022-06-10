package com.ics4ui.android;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class DayEventAdapter extends FirebaseRecyclerAdapter<Event, DayEventAdapter.DayEventViewHolder> {

    public DayEventAdapter(@NonNull FirebaseRecyclerOptions<Event> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull DayEventViewHolder holder, int position, @NonNull Event model) {
        Time startTime = model.getStartTime();
        Time endTime = model.getEndTime();
        String eventTimeString = startTime.getHour() + ":" + startTime.getMinute() + " - " + endTime.getHour() + ":" + endTime.getMinute();

        holder.eventTitle.setText(model.getTitle());
        holder.eventTime.setText(eventTimeString);
    }

    @NonNull
    @Override
    public DayEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list_item, parent, false);
        return new DayEventAdapter.DayEventViewHolder(view);
    }

    class DayEventViewHolder extends RecyclerView.ViewHolder {
        TextView eventTitle, eventTime;
        public DayEventViewHolder(@NonNull View itemView) {
            super(itemView);
            eventTitle = itemView.findViewById(R.id.eventTitle);
            eventTime = itemView.findViewById(R.id.eventTime);
        }
    }
}
