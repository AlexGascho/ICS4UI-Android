package com.ics4ui.android;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class HomeEventAdapter extends FirebaseRecyclerAdapter<Event, HomeEventAdapter.HomeEventViewHolder> {

    public HomeEventAdapter(@NonNull FirebaseRecyclerOptions<Event> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull HomeEventViewHolder holder, int position, @NonNull Event model) {
        holder.description.setText(model.getDescription());
    }

    @NonNull
    @Override
    public HomeEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list_item, parent, false);
        return new HomeEventAdapter.HomeEventViewHolder(view);
    }

    class HomeEventViewHolder extends RecyclerView.ViewHolder {
        TextView description;
        public HomeEventViewHolder(@NonNull View itemView) {
            super(itemView);

            description = itemView.findViewById(R.id.description);
        }
    }
}

