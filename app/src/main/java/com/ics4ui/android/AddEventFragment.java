package com.ics4ui.android;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ics4ui.android.databinding.FragmentAddEventBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddEventFragment extends Fragment implements View.OnClickListener {
    private FragmentAddEventBinding binding;

    private FirebaseAuth firebaseAuth;
    DatabaseReference dbase;
    Integer i=0;

    private Calendar startDateTime;
    private Calendar endDateTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddEventBinding.inflate(inflater, container,false);

        startDateTime = Calendar.getInstance();
        endDateTime = Calendar.getInstance();

        binding.dayCancel.setOnClickListener(this);
        binding.startTimeButton.setOnClickListener(this);
        binding.endTimeButton.setOnClickListener(this);
        binding.startDateButton.setOnClickListener(this);
        binding.endDateButton.setOnClickListener(this);
        binding.createEventButton.setOnClickListener(this);
        binding.allDaySwitch.setOnClickListener(this);

        return binding.getRoot();
    }

    public void createDatePickerDialog(View view, Button button, Calendar calendar) {
        Calendar c = Calendar.getInstance();
        int currentDay = c.get(Calendar.DAY_OF_MONTH);
        int currentMonth = c.get(Calendar.MONTH);
        int currentYear = c.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = month + "/" + dayOfMonth + "/" + year;
                button.setText(date);

                calendar.set(year, month, dayOfMonth);
            }
        }, currentYear, currentMonth, currentDay);
        datePickerDialog.show();
    }

    public void createTimePickerDialog(View view, Button button, Calendar calendar) {
        Calendar c = Calendar.getInstance();
        int currentHour = c.get(Calendar.HOUR_OF_DAY);
        int currentMinute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minuteOfHour) {
                button.setText(convertToTimeFormat(hourOfDay, minuteOfHour));

                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minuteOfHour);
            }
        }, currentHour, currentMinute, true);

        timePickerDialog.show();
    }

    public String convertToTimeFormat(int hour, int minute) {
        int hourOfDay = hour;
        int  minuteOfHour = minute;
        String suffix = "";

        if (hourOfDay == 0){
            hour = 12;
            suffix = "AM";
        } else if (hourOfDay < 12) {
            suffix = "AM";
        } else if (hourOfDay > 12){
            hour = hourOfDay - 12;
            suffix = "PM";
        } else if (hourOfDay == 12){
            suffix = "PM";
        }

        return hour + ":" + minute + " " + suffix;
    }

    public Event getEventDetails(View view) {
        //creates new event object
        Event newEvent = new Event();

        //sets attributes of event
        newEvent.setTitle(binding.titleTextInput.getText().toString());
        newEvent.setStartTime(startDateTime);

        //Sets default values if empty
        if (TextUtils.isEmpty(binding.descriptionTextInput.getText())) {
            newEvent.setDescription("N/A");
        } else {
            newEvent.setDescription(binding.descriptionTextInput.getText().toString());
        }

        if (TextUtils.isEmpty(binding.locationTextInput.getText())) {
            newEvent.setLocation("N/A");
        } else {
            newEvent.setLocation(binding.locationTextInput.getText().toString());
        }

        if (binding.clubGroupSpinner.getSelectedItem() == null) {
            newEvent.setGroup("None");
        } else {
            newEvent.setGroup(binding.clubGroupSpinner.getSelectedItem().toString());
        }

        if (binding.endDateButton.getText().toString().equals("End Date")) {
            endDateTime.set(Calendar.YEAR, startDateTime.get(Calendar.YEAR));
            endDateTime.set(Calendar.MONTH, startDateTime.get(Calendar.MONTH));
            endDateTime.set(Calendar.DAY_OF_MONTH, startDateTime.get(Calendar.DAY_OF_MONTH));
        }

        newEvent.setStartTime(startDateTime);
        newEvent.setEndTime(endDateTime);

        return newEvent;
    }

    public void writeNewEvent(FirebaseUser account, Event newEvent) {
        dbase = FirebaseDatabase.getInstance().getReference().child("users").child(account.getUid());

        Calendar date = newEvent.getStartTime();
        SimpleDateFormat databaseDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
        String formattedDate = databaseDateFormat.format(date.getTimeInMillis());

        String key = dbase.child("events").child(formattedDate).push().getKey();
        Map<String, Object> eventMap = newEvent.toMap();

        Map<String, Object> update = new HashMap<>();
        update.put("/events/" + formattedDate + "/" + key, eventMap);

        dbase.updateChildren(update);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.createEventButton:
                FirebaseUser account = firebaseAuth.getCurrentUser();

                if (TextUtils.isEmpty(binding.titleTextInput.getText())) {
                    Toast.makeText(getContext(), "Event title is required!", Toast.LENGTH_SHORT).show();
                    break;
                } else if (binding.startTimeButton.getText().toString().equals("Start Time")) {
                    Toast.makeText(getContext(), "Start time is required!", Toast.LENGTH_SHORT).show();
                    break;
                } else if (binding.endTimeButton.getText().toString().equals("End Time")) {
                    Toast.makeText(getContext(), "End time is required!", Toast.LENGTH_SHORT).show();
                    break;
                }

                if (account != null) {
                    //Creates toast (pop up)
                    Toast.makeText(getContext(), "Event Added", Toast.LENGTH_SHORT).show();
                    writeNewEvent(account, getEventDetails(view));
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new CalendarFragment()).commit();

                } else {
                    Toast.makeText(getContext(), "You are not logged in", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                    startActivity(new Intent(getContext(), SignInActivity.class));
                }
                break;
            case R.id.startTimeButton:
                createTimePickerDialog(view, binding.startTimeButton, startDateTime);
                break;
            case R.id.endTimeButton:
                createTimePickerDialog(view, binding.endTimeButton, endDateTime);
                break;
            case R.id.startDateButton:
                createDatePickerDialog(view, binding.startDateButton, startDateTime);
                break;
            case R.id.endDateButton:
                createDatePickerDialog(view, binding.endDateButton, endDateTime);
                break;
            case R.id.dayCancel:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new CalendarFragment()).commit();
                break;
            case R.id.allDaySwitch:
                if (binding.allDaySwitch.isChecked()) {
                    binding.endTimeButton.setVisibility(View.GONE);
                    binding.endDateButton.setVisibility(View.GONE);

                    //Sets end date to end of day
                    endDateTime.set(Calendar.HOUR_OF_DAY, 23);
                    endDateTime.set(Calendar.MINUTE, 59);
                    binding.endTimeButton.setText(convertToTimeFormat(23, 59));
                } else {
                    binding.endTimeButton.setVisibility(View.VISIBLE);
                    binding.endDateButton.setVisibility(View.VISIBLE);
                }
                break;
        }
    }
}