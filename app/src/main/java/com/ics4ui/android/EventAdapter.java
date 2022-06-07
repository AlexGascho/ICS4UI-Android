package com.ics4ui.android;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class EventAdapter extends FirebaseRecyclerAdapter<
        Event, EventAdapter.EventViewHolder> {

    public EventAdapter(@NonNull FirebaseRecyclerOptions<Event> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull EventViewHolder holder, int position, @NonNull Event model) {
        holder.title.setText(model.getTitle());
        holder.description.setText(model.getDescription());
        holder.location.setText(model.getLocation());
        holder.group.setText(model.getGroup());
        holder.startTime.setText(model.getStartTimeString());
        holder.endTime.setText(model.getEndTimeString());
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event, parent, false);
        return new EventAdapter.EventViewHolder(view);
    }


    class EventViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, location, group, startTime, endTime;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            location = itemView.findViewById(R.id.location);
            group = itemView.findViewById(R.id.group);
            startTime = itemView.findViewById(R.id.startTime);
            endTime = itemView.findViewById(R.id.endTime);
        }
    }
}
