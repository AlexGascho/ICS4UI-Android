package com.ics4ui.android;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.annotations.Nullable;
import com.ics4ui.android.databinding.FragmentClubsGroupsManagerBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ClubsGroupsManagerFragment extends Fragment {
    FragmentClubsGroupsManagerBinding binding;

    DatabaseReference dbase;
    ArrayList<String> clubsGroupsList = new ArrayList<>();
    Map<String, String> announcementKeys = new HashMap<String, String>();
    ArrayAdapter <String> adapter;

    public ClubsGroupsManagerFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentClubsGroupsManagerBinding.inflate(inflater, container, false);

        Query query = dbase.child("clubsGroups");
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {
                    try {
                        clubsGroupsList.add(snapshot.getKey());
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, clubsGroupsList);
                        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                        binding.selectClubsGroupsSpinner.setAdapter(adapter);
                        binding.selectClubsGroupsSpinner.setPopupBackgroundResource(R.drawable.spinner_dropdown_background);
                    } catch (Exception ignored) {}
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        binding.selectClubsGroupsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedText = binding.selectClubsGroupsSpinner.getSelectedItem().toString();
                if (!selectedText.equals("")) {
                    ClubsGroupsOptionsFragment optionsFragment = new ClubsGroupsOptionsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("clubName", selectedText);
                    optionsFragment.setArguments(bundle);

                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView2, optionsFragment).commit();

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        return binding.getRoot();
    }

}

