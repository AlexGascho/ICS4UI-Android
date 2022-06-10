package com.ics4ui.android;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddEventFragment extends Fragment {
    private static FragmentAddEventBinding binding;

    private FirebaseAuth firebaseAuth;
    DatabaseReference dbase;

    TextView titleText;
    Integer i=0;

    private static int startTimeHour;
    private static String startTimeMinute;
    private static int endTimeHour;
    private static String endTimeMinute;
    private static Calendar startDate;
    private static Calendar endDate;


    public static void setStartTimeHour(int timeHour) {
        startTimeHour = timeHour;
    }

    public static void setStartTimeMinute(String TimeMinute) {
        startTimeMinute = TimeMinute;
    }

    public static void setEndTimeHour(int timeHour){
        endTimeHour = timeHour;
    }

    public static void setEndTimeMinute(String timeMinute){
        endTimeMinute = timeMinute;
    }

    public static void setStartDate(Calendar c){
        startDate = c;
    }

    public static void setEndDate(Calendar c){
        endDate = c;
    }

    public AddEventFragment() {
        // Required empty public constructor
    }

    public void showTimePickerDialog(View view) {
        DialogFragment timePicker = new TimePickerFragment();
        timePicker.show(getActivity().getSupportFragmentManager(), "timePicker");
    }

    public void showDatePickerDialog(View view){
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getActivity().getSupportFragmentManager(),"datePicker");
    }

    public static AddEventFragment newInstance(String param1, String param2) {
        AddEventFragment fragment = new AddEventFragment();
        return fragment;
    }

    public void createEditTextDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("Title");

        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("ENTER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                binding.titleTextInput.setText(input.getText().toString());
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
        binding.startTimeButton.setHint(Integer.toString(startTimeHour)+":"+startTimeMinute+sfx);
    }

    public static void changeEndTimeButtonText(String sfx){
        binding.endTimeButton.setHint(Integer.toString(endTimeHour)+":"+endTimeMinute+sfx);
    }

    public static void changeStartDateButtonText(){
        binding.startDateButton.setHint("test");
    }

    public static void changeEndDateButtonText(){
        binding.endDateButton.setHint("test");
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

        binding.startTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerFragment.setIsStartButton(true);
                showTimePickerDialog(view);

            }
        });

        binding.endTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerFragment.setIsStartButton(false);
                showTimePickerDialog(view);
            }
        });

        binding.startDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment.setIsStartDateButton(true);
                showDatePickerDialog(view);
            }
        });

        binding.endDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment.setIsStartDateButton(false);
                showDatePickerDialog(view);
            }
        });

        binding.titleTextInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createEditTextDialog(view);
            }
        });

        binding.createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //creates new event object
                Event newEvent = new Event();
                //creates new Time object
                Time startTime = new Time();
                    //change properties of start time
                startTime.setMinute(startTimeMinute);
                startTime.setHour(startTimeHour);

                //creating endTime time object
                Time endTime = new Time();
                endTime.setMinute(endTimeMinute);
                endTime.setHour(endTimeHour);

                //sets attributes of event
                newEvent.setTitle(binding.titleTextInput.getText().toString());
                newEvent.setDescription(binding.descriptionTextInput.getText().toString());
                newEvent.setLocation(binding.locationTextInput.getText().toString());
                newEvent.setGroup(binding.groupTextInput.getText().toString());
                //adds start time object to event
                newEvent.setStartTime(startTime);
                //adds end time object to event
                newEvent.setEndTime(endTime);
                //adds event to list in main activity
                MainActivity.addEventToList(newEvent);


                FirebaseUser account = firebaseAuth.getCurrentUser();
                writeNewEvent(account, newEvent);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new CalendarFragment()).commit();
                //Creates toast (pop up)
                Toast.makeText(getContext(), "Event Added", Toast.LENGTH_SHORT).show();
            }
        });

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void writeNewEvent(FirebaseUser account, Event newEvent) {
        dbase = FirebaseDatabase.getInstance().getReference().child("users").child(account.getUid());
        String key = dbase.child("events").push().getKey();
        Map<String, Object> eventMap = newEvent.toMap();

        Map<String, Object> update = new HashMap<>();
        update.put("/events/" + key, eventMap);

        dbase.updateChildren(update);
    }

}