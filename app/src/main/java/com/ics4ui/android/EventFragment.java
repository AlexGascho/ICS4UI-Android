package com.ics4ui.android;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ics4ui.android.databinding.FragmentEventBinding;

public class EventFragment extends Fragment {
    FragmentEventBinding binding;

    public EventFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEventBinding.inflate(inflater, container, false);
        binding.eventNameCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new CalendarFragment()).commit();
            }
        });
        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}
