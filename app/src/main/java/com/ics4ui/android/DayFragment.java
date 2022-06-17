
package com.ics4ui.android;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.ics4ui.android.databinding.FragmentDayBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class DayFragment extends Fragment {
    FragmentDayBinding binding;

    private FirebaseAuth firebaseAuth;
    DayEventAdapter dayEventAdapter;
    DatabaseReference dbase;

    ArrayList<Event> eventList = new ArrayList<>();
    ArrayList<String> clubsGroups = new ArrayList<>();
    Long date;

    public static DayFragment newInstance(Long date) {
        DayFragment fragment = new DayFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        dbase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDayBinding.inflate(inflater, container, false);

        date = this.getArguments().getLong("date");
        clubsGroups = this.getArguments().getStringArrayList("clubsGroups");

        SimpleDateFormat formatDayMonth = new SimpleDateFormat("MMMM d", Locale.CANADA);
        binding.eventDay.setText(formatDayMonth.format(date));
        binding.eventRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        getDayEvents();

        binding.dayClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new CalendarFragment()).commit();
            }
        });

        return binding.getRoot();
    }

    private void getDayEvents() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        SimpleDateFormat databaseDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
        Query query = dbase.child("users").child(user.getUid()).child("events").child(databaseDateFormat.format(date));
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                eventList.add(snapshot.getValue(Event.class));
                dayEventAdapter = new DayEventAdapter(eventList, false);
                binding.eventRecycler.setAdapter(dayEventAdapter);
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

        for (int i = 0; i < clubsGroups.size(); ++i) {
            Query clubGroupQuery = dbase.child("clubsGroups").child(clubsGroups.get(i)).child("events").child(databaseDateFormat.format(date));
            clubGroupQuery.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    eventList.add(snapshot.getValue(Event.class));
                    dayEventAdapter = new DayEventAdapter(eventList, true);
                    binding.eventRecycler.setAdapter(dayEventAdapter);
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
    }
}