package com.ics4ui.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class DayEventAdapter extends  RecyclerView.Adapter<DayEventAdapter.ViewHolder> {
    ArrayList<Event> eventList;
    Boolean isClubGroupEvent;

    public DayEventAdapter(ArrayList<Event> eventList, Boolean isClubGroupEvent) {
        this.eventList = eventList;
        this.isClubGroupEvent = isClubGroupEvent;
    }

    @Override
    public void onBindViewHolder(@NonNull DayEventAdapter.ViewHolder holder, int position) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm aaa", Locale.ENGLISH);
        String eventTimeString = timeFormat.format(eventList.get(position).getStartTime()) + " - " + timeFormat.format(eventList.get(position).getEndTime());
        holder.eventTitle.setText(eventList.get(position).getTitle());
        holder.eventTime.setText(eventTimeString);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isClubGroupEvent) {
                    Toast.makeText(view.getContext(), "Event info for club/group events is under construction.", Toast.LENGTH_LONG).show();
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("title", "test title");
                    bundle.putString("desc", "test desc");

                    EventFragment eventFragment = new EventFragment();
                    eventFragment.setArguments(bundle);

                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, eventFragment).commit();
                }
            }
        });
    }

    @NonNull
    @Override
    public DayEventAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list_item, parent, false);
        return new DayEventAdapter.ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return this.eventList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView eventTitle, eventTime;
        RelativeLayout relativeLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventTitle = itemView.findViewById(R.id.eventTitle);
            eventTime = itemView.findViewById(R.id.eventTime);
            relativeLayout = itemView.findViewById(R.id.eventRow);
        }
    }
}
