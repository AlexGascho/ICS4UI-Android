package com.ics4ui.android;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    int timeHour;
    int timeMinute;
    Calendar calendar;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        //Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        timeHour = hourOfDay;
        timeMinute = minute;
        if(timeHour==0){
            timeHour = 12;
            AddEventFragment.setSuffix("AM");
        }
        else if(timeHour>12){
            timeHour = timeHour-12;
            AddEventFragment.setSuffix("PM");
        }
        else if(timeHour==12){
            AddEventFragment.setSuffix("PM");
        }
        // Adds a zero if minute is less than 10 so that time goes from displaying as 6:5 to 6:05
        if(timeMinute<10){
            //fills zero
            AddEventFragment.setConditionalZero("0");
        }
        else{
            //emptys zero
            AddEventFragment.setConditionalZero("");
        }

        //initialize calendar
        AddEventFragment.setStartTimeHour(timeHour);
        AddEventFragment.setStartTimeMinute(timeMinute);


    }
}
