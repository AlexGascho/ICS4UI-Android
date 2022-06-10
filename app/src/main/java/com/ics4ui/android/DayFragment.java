
package com.ics4ui.android;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ics4ui.android.databinding.FragmentDayBinding;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class DayFragment extends Fragment {
    FragmentDayBinding binding;

    private FirebaseAuth firebaseAuth;
    DayEventAdapter dayEventAdapter;
    DatabaseReference dbase;

    public static DayFragment newInstance(Long date) {
        DayFragment fragment = new DayFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDayBinding.inflate(inflater, container, false);

        FirebaseUser account = firebaseAuth.getCurrentUser();

        dbase = FirebaseDatabase.getInstance().getReference().child("users").child(account.getUid()).child("events");
        binding.eventRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        Long date = this.getArguments().getLong("date");
        SimpleDateFormat formatDayMonth = new SimpleDateFormat("MMMM d", Locale.CANADA);
        String formattedDate = formatDayMonth.format(date);
        binding.eventDay.setText(formattedDate);

        binding.dayClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new CalendarFragment()).commit();
            }
        });

        FirebaseRecyclerOptions<Event> options = new FirebaseRecyclerOptions.Builder<Event>()
                .setQuery(dbase, Event.class)
                .build();

        dayEventAdapter = new DayEventAdapter(options);

        binding.eventRecycler.setAdapter(dayEventAdapter);

        return binding.getRoot();
    }

    @Override
    public void onStart(){
        super.onStart();
        dayEventAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        dayEventAdapter.stopListening();
    }

}