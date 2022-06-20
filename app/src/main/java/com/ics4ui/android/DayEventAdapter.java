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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class DayEventAdapter extends FirebaseRecyclerAdapter<Event, DayEventAdapter.DayEventViewHolder> {

    DatabaseReference dbase;
    FirebaseAuth auth;


    public DayEventAdapter(@NonNull FirebaseRecyclerOptions<Event> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull DayEventViewHolder holder, int position, @NonNull Event model) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm aaa", Locale.ENGLISH);
        String eventTimeString = timeFormat.format(model.getStartTime()) + " - " + timeFormat.format(model.getEndTime());
        holder.eventTitle.setText(model.getTitle());
        holder.eventTime.setText(eventTimeString);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                String formatedDate = dateFormat.format(model.getStartTime());

                dbase = FirebaseDatabase.getInstance().getReference();
                auth = FirebaseAuth.getInstance();

                FirebaseUser user = auth.getCurrentUser();

                String eventKey = model.getEventId();

                Bundle bundle = new Bundle();
                bundle.putString("date",formatedDate);
                bundle.putString("eventKey",eventKey);

                EventFragment eventFragment = new EventFragment();
                eventFragment.setArguments(bundle);

                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, eventFragment).commit();

            }
        });
    }

    @NonNull
    @Override
    public DayEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list_item, parent, false);
        return new DayEventAdapter.DayEventViewHolder(view);
    }

    class DayEventViewHolder extends RecyclerView.ViewHolder {
        TextView eventTitle, eventTime;
        RelativeLayout relativeLayout;
        public DayEventViewHolder(@NonNull View itemView) {
            super(itemView);
            eventTitle = itemView.findViewById(R.id.eventTitle);
            eventTime = itemView.findViewById(R.id.eventTime);
            relativeLayout = itemView.findViewById(R.id.eventRow);
        }
    }
}
