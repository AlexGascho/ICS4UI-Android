
package com.ics4ui.android;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.ics4ui.android.databinding.FragmentDayBinding;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class DayFragment extends Fragment {
    FragmentDayBinding binding;

    public static DayFragment newInstance(Long date) {
        DayFragment fragment = new DayFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDayBinding.inflate(inflater, container, false);

        Long date = this.getArguments().getLong("date");
        SimpleDateFormat formatDayMonth = new SimpleDateFormat("MMMM d", Locale.CANADA);
        String formattedDate = formatDayMonth.format(date);
        binding.eventDay.setText(formattedDate);

        binding.dayClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_main, new CalendarFragment()).commit();
            }
        });

        return binding.getRoot();
    }

}