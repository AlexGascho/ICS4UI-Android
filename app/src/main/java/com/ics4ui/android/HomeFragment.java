package com.ics4ui.android;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.ics4ui.android.databinding.FragmentHomeBinding;

import org.apache.commons.collections4.MultiValuedMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    DatabaseReference dbase;
    Map<String, String> clubGroupAnnouncements = new HashMap<>();
    ArrayList<String> globalAnnouncements = new ArrayList<>();

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
        dbase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
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

        getGlobalAnnouncements();

        getClubGroupAnnouncements();

        return binding.getRoot();
    }

    private void getGlobalAnnouncements() {
        Query query = dbase.child("announcements");
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (!globalAnnouncements.contains(snapshot.getValue(Announcements.class).getAnnouncement())) {
                    globalAnnouncements.add(snapshot.getValue(Announcements.class).getAnnouncement());
                }
                HomeAnnouncementAdapter adapter = new HomeAnnouncementAdapter(globalAnnouncements);
                binding.announcementRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
                binding.announcementRecycler.setAdapter(adapter);
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) { }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private void getClubGroupAnnouncements() {
        user = firebaseAuth.getCurrentUser();

        Query query = dbase.child("clubsGroups");

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                getAnnouncements(snapshot.getKey());
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void getAnnouncements(String clubGroup) {
        Query query = dbase.child("clubsGroups").child(clubGroup).child("members");

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (user.getUid().equals(snapshot.getKey())) {
                    appendAnnouncement(clubGroup);
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void appendAnnouncement(String clubGroup) {
        Query query = dbase.child("clubsGroups").child(clubGroup).child("announcements");
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (!clubGroupAnnouncements.containsKey(snapshot.getValue(Announcements.class).getAnnouncement())) {
                    clubGroupAnnouncements.put(snapshot.getValue(Announcements.class).getAnnouncement(), clubGroup);
                }
                ClubGroupAnnouncementAdapter adapter = new ClubGroupAnnouncementAdapter(clubGroupAnnouncements);
                binding.clubsGroupsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
                binding.clubsGroupsRecycler.setAdapter(adapter);
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}