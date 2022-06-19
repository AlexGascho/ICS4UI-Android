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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class ClubGroupAnnouncementAdapter extends RecyclerView.Adapter<ClubGroupAnnouncementAdapter.ViewHolder> {
    Map<String, String> announcementMap;
    ArrayList<String> keys = new ArrayList<>();
    ArrayList<String> values = new ArrayList<>();

    public ClubGroupAnnouncementAdapter(Map<String, String> announcementMap) {
        this.announcementMap = announcementMap;
        values.addAll(announcementMap.values());
        keys.addAll(announcementMap.keySet());
    }

    @Override
    public ClubGroupAnnouncementAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.club_group_announcement_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ClubGroupAnnouncementAdapter.ViewHolder holder, int position) {
        holder.clubGroupTitle.setText(values.get(position));
        holder.clubGroupAnnouncement.setText(keys.get(position));
    }

    @Override
    public int getItemCount() {
        return this.announcementMap.size();
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
