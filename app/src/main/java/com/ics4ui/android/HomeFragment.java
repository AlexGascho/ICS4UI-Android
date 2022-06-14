package com.ics4ui.android;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ics4ui.android.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;

    private FirebaseAuth firebaseAuth;

    DatabaseReference dbase;
    HomeAnnouncementAdapter announcementAdapter, clubsGroupsAnnouncementAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbase = FirebaseDatabase.getInstance().getReference().child("announcements");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container,false);

        binding.instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://www.instagram.com/elmira_lancers/?hl=en")));
            }
        });

        binding.announcementRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.clubsGroupsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<Announcements> options = new FirebaseRecyclerOptions.Builder<Announcements>()
                .setQuery(dbase.child("genericAnnouncements"), Announcements.class)
                .build();
        FirebaseRecyclerOptions<Announcements> options2 = new FirebaseRecyclerOptions.Builder<Announcements>()
                .setQuery(dbase.child("clubsGroupsAnnouncements"), Announcements.class)
                .build();

        announcementAdapter = new HomeAnnouncementAdapter(options);
        clubsGroupsAnnouncementAdapter = new HomeAnnouncementAdapter(options2);

        binding.announcementRecycler.setAdapter(announcementAdapter);
        binding.clubsGroupsRecycler.setAdapter(clubsGroupsAnnouncementAdapter);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        announcementAdapter.startListening();
        clubsGroupsAnnouncementAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        announcementAdapter.stopListening();
        clubsGroupsAnnouncementAdapter.startListening();
    }
}