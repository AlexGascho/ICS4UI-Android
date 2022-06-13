package com.ics4ui.android;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ics4ui.android.databinding.FragmentAddEventBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddEventFragment extends Fragment implements View.OnClickListener {
    private FragmentAddEventBinding binding;

    private FirebaseAuth firebaseAuth;
    DatabaseReference dbase;

    TextView titleText;
    public static Button startTimeButtonInput;
    Integer i=0;

    private static int startHour;
    private static int startMinute;
    private static int startYear;
    private static int startMonth;
    private static int startDay;

    private static int endHour;
    private static int endMinute;
    private static int endYear;
    private static int endMonth;
    private static int endDay;

    public AddEventFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAddEventBinding.inflate(inflater, container,false);

        binding.dayCancel.setOnClickListener(this);
        binding.startTimeButton.setOnClickListener(this);
        binding.endTimeButton.setOnClickListener(this);
        binding.createEventButton.setOnClickListener(this);
        binding.titleTextInput.setOnClickListener(this);
        binding.locationTextInput.setOnClickListener(this);
        binding.descriptionTextInput.setOnClickListener(this);
        binding.groupTextInput.setOnClickListener(this);

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void writeNewEvent(FirebaseUser account, Event newEvent) {
        dbase = FirebaseDatabase.getInstance().getReference().child("users").child(account.getUid()).child("events");

        Date date = newEvent.getStartTime();
        SimpleDateFormat databaseDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
        String formattedDate = databaseDateFormat.format(date);

        String key = dbase.child(formattedDate).push().getKey();
        Map<String, Object> eventMap = newEvent.toMap();

        Map<String, Object> update = new HashMap<>();
        update.put("/events/" + formattedDate + "/" + key, eventMap);

        dbase.updateChildren(update);
    }

    public static void setStartTime(int hour, int minute) {
        startHour = hour;
        startMinute = minute;
    }

    public static void setEndTime(int hour, int minute) {
        endHour = hour;
        endMinute = minute;
    }

    public static void setStartDate(int year, int month, int day) {
        startYear = year;
        startMonth = month;
        startDay = day;
    }

    public static void setEndDate(int year, int month, int day) {
        endYear = year;
        endMonth = month;
        endDay = day;
    }


    public void showTimePickerDialog(View view) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
    }

    public void showDatePickerDialog(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    public void createDatePickerDialog(View view, String title) {
//        TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(), new TimePickerDialog.OnTimeSetListener() {
//            @Override
//            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
//                binding.startDateButton.setHint(hourOfDay + ":" + minute);
//            }
//        }, hourOfDay, minute, false);
//        picker.show();
    }

    public void createEditTextDialog(View view, TextView textView, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle(title);

        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.text_input_layout, (ViewGroup) getView(), false);
        final EditText input = (EditText) viewInflated.findViewById(R.id.input);
        builder.setView(viewInflated);

        builder.setPositiveButton("ENTER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                textView.setText(input.getText().toString());
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public static void changeStartTimeButtonText(String sfx){
          //binding.startTimeButton.setHint(Integer.toString(startHour)+":"+startMinute+sfx);
    }
    public static void changeEndTimeButtonText(String sfx){
         //binding.endTimeButton.setHint(Integer.toString(endHour)+":"+endMinute+sfx);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.createEventButton:
                //creates new event object
                Event newEvent = new Event();

                //Create start and end date
                Date startDate = new Date(startYear, startMonth, startDay, startHour, startMinute);
                Date endDate = new Date(endYear, endMonth, endDay, endHour, endMinute);

                //sets attributes of event
                newEvent.setTitle(binding.titleTextInput.getText().toString());
                newEvent.setDescription(binding.descriptionTextInput.getText().toString());
                newEvent.setLocation(binding.locationTextInput.getText().toString());
                newEvent.setGroup(binding.groupTextInput.getText().toString());
                newEvent.setStartTime(startDate);
                newEvent.setEndTime(endDate);

                //adds event to list in main activity

                FirebaseUser account = firebaseAuth.getCurrentUser();
                writeNewEvent(account, newEvent);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new CalendarFragment()).commit();
                //Creates toast (pop up)
                Toast.makeText(getContext(), "Event Added", Toast.LENGTH_SHORT).show();

                break;
            case R.id.startTimeButton:
                showTimePickerDialog(view);
                break;
            case R.id.endTimeButton:
                showTimePickerDialog(view);
                break;
            case R.id.startDateButton:
                showDatePickerDialog(view);
                break;
            case R.id.endDateButton:
                showDatePickerDialog(view);
                break;
            case R.id.titleTextInput:
                createEditTextDialog(view, binding.titleTextInput, "Title");
                break;
            case R.id.locationTextInput:
                createEditTextDialog(view, binding.locationTextInput, "Location");
                break;
            case R.id.descriptionTextInput:
                createEditTextDialog(view, binding.descriptionTextInput, "Description");
                break;
            case R.id.groupTextInput:
                createEditTextDialog(view, binding.groupTextInput, "Groups/Clubs");
                break;
            case R.id.dayCancel:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new CalendarFragment()).commit();
                break;
        }
    }
}