package com.ics4ui.android;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.util.HashMap;
import java.util.Map;

public class AddEventFragment extends Fragment implements View.OnClickListener {
    private static FragmentAddEventBinding binding;

    private FirebaseAuth firebaseAuth;
    DatabaseReference dbase;

    TextView titleText;
    public static Button startTimeButtonInput;
    Integer i=0;

    private static int startTimeHour;
    private static String startTimeMinute;

    public static void setStartTimeHour(int TimeHour) {
        startTimeHour = TimeHour;
    }

    public static void setStartTimeMinute(String TimeMinute) {
        startTimeMinute = TimeMinute;
    }

    public AddEventFragment() {
        // Required empty public constructor
    }

    public void showTimePickerDialog(View view) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
    }
    public static AddEventFragment newInstance(String param1, String param2) {
        AddEventFragment fragment = new AddEventFragment();
        return fragment;
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
        binding.startTimeButton.setHint(Integer.toString(startTimeHour)+":"+startTimeMinute+sfx);
    }
    public static void changeEndTimeButtonText(){

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
        dbase = FirebaseDatabase.getInstance().getReference().child("users").child(account.getUid());
        String key = dbase.child("events").push().getKey();
        Map<String, Object> eventMap = newEvent.toMap();

        Map<String, Object> update = new HashMap<>();
        update.put("/events/" + key, eventMap);

        dbase.updateChildren(update);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.createEventButton:
                //creates new event object
                Event newEvent = new Event();
                //creates new Time object
                Time startTime = new Time();
                //change properties of start time
                startTime.setMinute(startTimeMinute);
                startTime.setHour(startTimeHour);

                //sets attributes of event
                newEvent.setTitle(binding.titleTextInput.getText().toString());
                newEvent.setDescription(binding.descriptionTextInput.getText().toString());
                newEvent.setLocation(binding.locationTextInput.getText().toString());
                newEvent.setGroup(binding.groupTextInput.getText().toString());
                newEvent.setStartTime(startTime);

                //adds start time object to event
                newEvent.setStartTime(startTime);
                //adds event to list in main activity
                MainActivity.addEventToList(newEvent);


                FirebaseUser account = firebaseAuth.getCurrentUser();
                writeNewEvent(account, newEvent);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new CalendarFragment()).commit();
                //Creates toast (pop up)
                Toast.makeText(getContext(), "Event Added", Toast.LENGTH_SHORT).show();

                break;
            case R.id.startTimeButton:
                showTimePickerDialog(view);
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