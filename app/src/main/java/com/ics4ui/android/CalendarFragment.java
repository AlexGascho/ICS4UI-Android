package com.ics4ui.android;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.ics4ui.android.databinding.FragmentCalendarBinding;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateLongClickListener;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.Calendar;


public class CalendarFragment extends Fragment implements OnDateSelectedListener, OnMonthChangedListener, OnDateLongClickListener {
    FragmentCalendarBinding binding;
    ImageButton addEventImageButton;

    public CalendarFragment() {

        int width = 60;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCalendarBinding.inflate(inflater, container, false);

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

        binding.calendarView.setOnDateChangedListener(this);
        binding.calendarView.setOnDateLongClickListener(this);
        binding.calendarView.setOnMonthChangedListener(this);

        //Adds event indicators
        CalendarDay test = CalendarDay.from(2022, 6, 28);
        binding.calendarView.addDecorator(new EventDecorator(test));

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public class EventDecorator implements DayViewDecorator {

        private final CalendarDay dates;

        public EventDecorator(CalendarDay dates) {
            this.dates = dates;
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return dates.equals(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new DotSpan(10, Color.MAGENTA));
        }
    }

    @Override
    public void onDateLongClick(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date) {

    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        Calendar newDate = Calendar.getInstance();
        newDate.set(date.getYear(), date.getMonth() - 1, date.getDay()); //CalendarDay months start at 1 while Calendar starts at 0

        Bundle bundle = new Bundle();
        bundle.putLong("date", newDate.getTimeInMillis());

        DayFragment dayFragment = new DayFragment();
        dayFragment.setArguments(bundle);

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, dayFragment).commit();
    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

    }
}
