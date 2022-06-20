package com.ics4ui.android;

import android.annotation.SuppressLint;
import android.content.Context;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class DayEventAdapter extends  RecyclerView.Adapter<DayEventAdapter.ViewHolder> {
    ArrayList<Event> eventList;
    Boolean isClubGroupEvent;
    Context context;

    public DayEventAdapter(ArrayList<Event> eventList, Boolean isClubGroupEvent, Context context) {
        this.eventList = eventList;
        this.isClubGroupEvent = isClubGroupEvent;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull DayEventAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm aaa", Locale.ENGLISH);
        String eventTimeString = timeFormat.format(eventList.get(position).getStartTime()) + " - " + timeFormat.format(eventList.get(position).getEndTime());
        holder.eventTitle.setText(eventList.get(position).getTitle());
        holder.eventTime.setText(eventTimeString);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isClubGroupEvent) {
                    Toast.makeText(view.getContext(), "Event info for club/group events is under construction.", Toast.LENGTH_LONG).show();
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("eventKey", eventList.get(position).getEventId());
                    bundle.putString("date", timeFormat.format(eventList.get(position).getStartTime()));

                    EventFragment eventFragment = new EventFragment();
                    eventFragment.setArguments(bundle);

                    FragmentManager manager = ((FragmentActivity)context).getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.fragmentContainerView, eventFragment).attach(eventFragment).commit();
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
