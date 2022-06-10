package com.ics4ui.android;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.text.DateFormat;
import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    static Boolean isStartDateButton;

    public static void setIsStartDateButton(boolean bool){
        isStartDateButton = bool;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) getActivity(),year,month,day);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        if(isStartDateButton==true){
            AddEventFragment.setStartDate(calendar);
            AddEventFragment.changeStartDateButtonText();
        }
        else if(isStartDateButton==false){

            AddEventFragment.setEndDate(calendar);
            AddEventFragment.changeEndDateButtonText();
        }
        //String currentDateString = DateFormat.getDateInstance().format(calendar.getTime());

    }
}
