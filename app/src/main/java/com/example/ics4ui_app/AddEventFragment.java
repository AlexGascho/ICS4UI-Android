package com.example.ics4ui_app;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddEventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddEventFragment extends Fragment {

    TextView titleText;
    EditText titleInput;
    EditText descriptionInput;
    String title;
    String description;
    String location;
    String group;
    Integer i=0;
    List<Event> listOfEvents = new ArrayList<Event>();



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddEventFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddEventFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddEventFragment newInstance(String param1, String param2) {
        AddEventFragment fragment = new AddEventFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View AddEventFragmentLayout = inflater.inflate(R.layout.fragment_add_event,container,false);
        titleText = AddEventFragmentLayout.findViewById(R.id.addEventTitle);
        titleInput = AddEventFragmentLayout.findViewById(R.id.titleTextInput);
        descriptionInput = AddEventFragmentLayout.findViewById(R.id.descriptionTextInput);
        return AddEventFragmentLayout;

    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.createEventButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = titleInput.getText().toString();
                description = descriptionInput.getText().toString();
                Event newEvent = new Event();
                newEvent.title = title;
                newEvent.description = description;
                listOfEvents.add(newEvent);
                //titleText.setText((listOfEvents.get(0).title).toString());
                titleText.setText(listOfEvents.get(i).title+" "+listOfEvents.get(i).description);
                i++;
            }
        });
    }
}