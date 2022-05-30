package com.example.ics4ui_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddEventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddEventFragment extends Fragment {
    TextView titleText;
    EditText titleInput;
    EditText descriptionInput;
    EditText locationInput;
    EditText groupInput;
    Integer i=0;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View AddEventFragmentLayout = inflater.inflate(R.layout.fragment_add_event,container,false);
        titleText = AddEventFragmentLayout.findViewById(R.id.addEventTitle);
        titleInput = AddEventFragmentLayout.findViewById(R.id.titleTextInput);
        descriptionInput = AddEventFragmentLayout.findViewById(R.id.descriptionTextInput);
        locationInput = AddEventFragmentLayout.findViewById(R.id.locationTextInput);
        groupInput = AddEventFragmentLayout.findViewById(R.id.groupTextInput);
        return AddEventFragmentLayout;

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.startTimeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog(view);
            }
        });
        view.findViewById(R.id.createEventButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Event newEvent = new Event();
                newEvent.title = titleInput.getText().toString();
                newEvent.description = descriptionInput.getText().toString();
                newEvent.location = locationInput.getText().toString();
                newEvent.group = groupInput.getText().toString();
                CalendarView.listOfEvents.add(newEvent);

                titleText.setText(CalendarView.listOfEvents.get(i).title+" "+CalendarView.listOfEvents.get(i).description+" "+CalendarView.listOfEvents.get(i).location+" "+CalendarView.listOfEvents.get(i).group);
                i++;
                FrameLayout frameLayout = (FrameLayout) getView().findViewById(R.id.add_event_container);
                frameLayout.setVisibility(View.GONE);
            }
        });
    }
}