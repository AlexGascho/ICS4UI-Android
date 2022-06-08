package com.ics4ui.android;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class HomeAnnouncementAdapter extends FirebaseRecyclerAdapter<Announcements, HomeAnnouncementAdapter.HomeAnnouncementViewHolder> {

    public HomeAnnouncementAdapter(@NonNull FirebaseRecyclerOptions<Announcements> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull HomeAnnouncementViewHolder holder, int position, @NonNull Announcements model) {
        holder.announcement.setText(model.getAnnouncement());
    }

    @NonNull
    @Override
    public HomeAnnouncementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.announcement_list_item, parent, false);
        return new HomeAnnouncementAdapter.HomeAnnouncementViewHolder(view);
    }

    class HomeAnnouncementViewHolder extends RecyclerView.ViewHolder {
        TextView announcement;
        public HomeAnnouncementViewHolder(@NonNull View itemView) {
            super(itemView);
            announcement = itemView.findViewById(R.id.announcement);
        }
    }
}
