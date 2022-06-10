package com.ics4ui.android;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
    static Boolean isStartButton; //if true then this will change start time button if false will change end time button


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_YEAR);

        //Create a new instance of TimePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day
                DateFormat.getDateFormat(getActivity()));
    }

    public static void setIsStartButton(Boolean bool){
        isStartButton = bool;
    }

    public void onDateSet(DatePicker view, int dateYear, int dateMonth, int dateDay) {
        if(!isStartButton){
            AddEventFragment.setStartYearMonthDay(dateYear, dateMonth, dateDay);
        }
        else if(isStartButton){
            AddEventFragment.setStartYearMonthDay(dateYear, dateMonth, dateDay);
        }

    }
}
