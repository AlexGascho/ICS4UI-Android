package com.ics4ui.android;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ics4ui.android.databinding.FragmentEventBinding;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class EventFragment extends Fragment {
    FragmentEventBinding binding;
    DatabaseReference dbase;
    FirebaseAuth auth;
    String eventKey;
    String formatedDate;

    public EventFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbase = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEventBinding.inflate(inflater, container, false);
        binding.eventCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new DayFragment()).commit();            }
        });
        binding.editEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"Under Construction", Toast.LENGTH_SHORT);
            }
        });
        binding.deleteEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        // Inflate the layout for this fragment

        FirebaseUser user = auth.getCurrentUser();
        eventKey = this.getArguments().getString("eventKey");
        formatedDate = this.getArguments().getString("date");


        binding = FragmentEventBinding.inflate(inflater, container, false);
        Map<String, String> eventInfo = new HashMap<>();
        Query query = dbase.child("users").child(user.getUid()).child("events").child(formatedDate).child(eventKey);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Event event = snapshot.getValue(Event.class);
                    //setup Time Formatter
                    SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm aaa", Locale.ENGLISH);

                    binding.eventName.setText(event.getTitle());
                    binding.eventStartTime.setText(timeFormat.format(event.getStartTime()));
                    binding.eventEndTime.setText(timeFormat.format(event.getEndTime()));
                    binding.eventLocation.setText(event.getLocation());
                    binding.eventDesc.setText(event.getDescription());
                    binding.eventGroup.setText(event.getGroup());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return binding.getRoot();
    }
}
