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

public class HomeAnnouncementAdapter extends RecyclerView.Adapter<HomeAnnouncementAdapter.ViewHolder> {
    ArrayList<String> announcementArray;

    public HomeAnnouncementAdapter(ArrayList<String> announcementArray) {
        this.announcementArray = announcementArray;
    }

    @Override
    public HomeAnnouncementAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.announcement_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HomeAnnouncementAdapter.ViewHolder holder, int position) {
        holder.announcement.setText(announcementArray.get(position));
    }

    @Override
    public int getItemCount() {
        return this.announcementArray.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView announcement;
        public ViewHolder(View view) {
            super(view);
            this.announcement = view.findViewById(R.id.announcement);
        }
    }
}
