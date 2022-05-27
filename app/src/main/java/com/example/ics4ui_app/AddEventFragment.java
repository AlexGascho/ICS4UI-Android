package com.example.ics4ui_app;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddEventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddEventFragment extends Fragment {
    private DatePickerDialog startDateDialog;
    DatePickerDialog endDateDialog;
    private Button startDateButton;
    Button endDateButton;

    TextView titleText;
    EditText titleInput;
    EditText descriptionInput;
    EditText locationInput;
    EditText groupInput;
    Integer i=0;

    public AddEventFragment() {
        // Required empty public constructor
    }

    public static AddEventFragment newInstance(String param1, String param2) {
        AddEventFragment fragment = new AddEventFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDatePicker();
        startDateButton = startDateButton.findViewById(R.id.startDateButton);
        startDateButton.setText(getTodaysDate());
    }

    private String getTodaysDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        month = month+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                startDateButton.setText(date);

            }
        };

        //Get todays date for starting point when choosing date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Choosing Style for Date Picker
        int style = AlertDialog.THEME_HOLO_LIGHT;

        startDateDialog = new DatePickerDialog(getActivity(), style, dateSetListener, year, month, day);
    }


    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + "" + year;
    }

    private String getMonthFormat(int month) {
        if (month==1)
            return "JAN";
        if (month==2)
            return "FEB";
        if (month==3)
            return "MAR";
        if (month==4)
            return "APR";
        if (month==5)
            return "MAY";
        if (month==6)
            return "JUN";
        if (month==7)
            return "JUL";
        if (month==8)
            return "AUG";
        if (month==9)
            return "SEP";
        if (month==10)
            return "OCT";
        if (month==11)
            return "NOV";
        if (month==12)
            return "DEC";

        return "JAN";
    }

    public void openStartDatePicker(View view){
        startDateDialog.show();
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


        view.findViewById(R.id.createEventButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Event newEvent = new Event();
                newEvent.title = titleInput.getText().toString();
                newEvent.description = descriptionInput.getText().toString();
                newEvent.location = locationInput.getText().toString();
                newEvent.group = groupInput.getText().toString();
                CalendarView.listOfEvents.add(newEvent);
                Toast toast = Toast.makeText(getContext(),"Event added Succesfully",Toast.LENGTH_SHORT);
                toast.show();
                //titleText.setText(CalendarView.listOfEvents.get(i).title+" "+CalendarView.listOfEvents.get(i).description+" "+CalendarView.listOfEvents.get(i).location+" "+CalendarView.listOfEvents.get(i).group);
                //i++;

                FrameLayout frameLayout = (FrameLayout) getView().findViewById(R.id.add_event_container);
                frameLayout.setVisibility(View.GONE);

            }
        });
    }
}