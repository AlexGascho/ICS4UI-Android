package com.ics4ui.android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.ics4ui.android.databinding.FragmentCalendarBinding;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateLongClickListener;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class CalendarFragment extends Fragment implements OnDateSelectedListener, OnMonthChangedListener, OnDateLongClickListener {
    FragmentCalendarBinding binding;
    private FirebaseAuth firebaseAuth;
    DatabaseReference dbase;

    private ArrayList<CalendarDay> eventDateList = new ArrayList<>();
    private ArrayList<String> userClubGroups = new ArrayList<>();
    Bundle bundle = new Bundle();

    public CalendarFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        dbase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCalendarBinding.inflate(inflater, container, false);

        FirebaseUser user = firebaseAuth.getCurrentUser();
        Query eventKeys = dbase.child("users").child(user.getUid()).child("events");

        getUserClubGroups(user);

        eventKeys.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {
                    try {
                        binding.calendarView.addDecorator(new EventDecorator(convertStringToCalendarDay(snapshot.getKey())));
                    } catch (Exception ignored) {}
                }
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

                binding.addEventImageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new AddEventFragment())
                                .commit();
                    }
                });

        binding.filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new FilterFragment())
                        .commit();
            }
        });

        binding.calendarView.setOnDateChangedListener(this);
        binding.calendarView.setOnDateLongClickListener(this);
        binding.calendarView.setOnMonthChangedListener(this);
        CalendarDay currentDate = CalendarDay.today();
        binding.calendarView.setDateSelected(currentDate, true);

        return binding.getRoot();
    }

    private void getUserClubGroups(FirebaseUser user) {
        Query query = dbase.child("users").child(user.getUid()).child("memberOf");
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String clubGroup = snapshot.child("clubGroup").getValue().toString();
                userClubGroups.add(clubGroup);
                bundle.putStringArrayList("clubsGroups", userClubGroups);
                Query clubEvent = dbase.child("clubsGroups").child(clubGroup).child("events");
                clubEvent.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        binding.calendarView.addDecorator(new EventDecorator(convertStringToCalendarDay(snapshot.getKey())));
                    }
                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {}
                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });
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


    private CalendarDay convertStringToCalendarDay(String key) {
        Date date;
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            date = dateFormat.parse(key);
        }catch(ParseException e){
            date = new Date(0, 0, 0);
        }
        return CalendarDay.from(date.getYear()+1900, date.getMonth()+1, date.getDate());
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onDateLongClick(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date) {

    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        Calendar newDate = Calendar.getInstance();
        newDate.set(date.getYear(), date.getMonth() - 1, date.getDay()); //CalendarDay months start at 1 while Calendar starts at 0

        bundle.putLong("date", newDate.getTimeInMillis());

        DayFragment dayFragment = new DayFragment();
        dayFragment.setArguments(bundle);

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, dayFragment).commit();
    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

    }

}
