package com.ics4ui.android;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
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

public class ClubGroupAnnouncementAdapter extends RecyclerView.Adapter<ClubGroupAnnouncementAdapter.ViewHolder> {
    ArrayList<String> announcementArray;
    String clubGroup;

    public ClubGroupAnnouncementAdapter(ArrayList<String> announcementArray, String clubGroup) {
        this.announcementArray = announcementArray;
        this.clubGroup = clubGroup;
    }

    @Override
    public ClubGroupAnnouncementAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.club_group_announcement_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ClubGroupAnnouncementAdapter.ViewHolder holder, int position) {
        holder.clubGroupTitle.setText(clubGroup);
        holder.clubGroupAnnouncement.setText(this.announcementArray.get(position));
    }

    @Override
    public int getItemCount() {
        return this.announcementArray.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView clubGroupAnnouncement, clubGroupTitle;
        public ViewHolder(View view) {
            super(view);
            this.clubGroupAnnouncement = view.findViewById(R.id.clubGroupAnnouncement);
            this.clubGroupTitle = view.findViewById(R.id.clubGroupTitle);
        }
    }
}
