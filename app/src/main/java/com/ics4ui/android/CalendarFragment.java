package com.ics4ui.android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.ics4ui.android.databinding.FragmentCalendarBinding;

import java.util.Date;


public class CalendarFragment extends Fragment {
    FragmentCalendarBinding binding;
    Long oldDate;
    ImageButton addEventImageButton;
    CalendarView calendarView;

    public CalendarFragment() {

        int width = 60;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCalendarBinding.inflate(inflater, container, false);

        oldDate = binding.calendarView.getDate();

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

        binding.calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                //Subtracting 1900 from year since Java adds 1900 for some reason
                Date newDate = new Date(year-1900, month, day);


                if (newDate.getTime() != oldDate) {
                    oldDate = newDate.getTime();
                    calendarView.setDate(oldDate);
                }

                Bundle bundle = new Bundle();
                bundle.putLong("date", oldDate);

                DayFragment dayFragment = new DayFragment();
                dayFragment.setArguments(bundle);

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, dayFragment).commit();
            }
        });

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
