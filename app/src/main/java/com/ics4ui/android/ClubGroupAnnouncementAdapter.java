package com.ics4ui.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ClubGroupAnnouncementAdapter extends RecyclerView.Adapter<ClubGroupAnnouncementAdapter.ClubGroupAnnouncementViewHolder> {
    ArrayList<String> announcementArray;

    public ClubGroupAnnouncementAdapter(@NonNull ArrayList<String> announcementArray) {
        this.announcementArray = announcementArray;
    }

    @Override
    public void onBindViewHolder(@NonNull ClubGroupAnnouncementViewHolder holder, int position) {
        String announcements = announcementArray.get(position);
        holder.announcement.setText(announcements);
    }

    @NonNull
    @Override
    public ClubGroupAnnouncementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.announcement_list_item, parent, false);
        return new ClubGroupAnnouncementAdapter.ClubGroupAnnouncementViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ClubGroupAnnouncementViewHolder extends RecyclerView.ViewHolder {
        TextView announcement;
        public ClubGroupAnnouncementViewHolder(@NonNull View itemView) {
            super(itemView);
            announcement = itemView.findViewById(R.id.announcement);
        }
    }
}
