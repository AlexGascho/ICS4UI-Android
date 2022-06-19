package com.ics4ui.android;

import android.annotation.SuppressLint;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class ClubsGroupsManagerFragment extends Fragment implements View.OnClickListener {
    FragmentClubsGroupsManagerBinding binding;

    DatabaseReference dbase;
    ArrayList<String> clubsGroupsList = new ArrayList<>();
    FirebaseAuth auth;
    FirebaseUser user;

    public ClubsGroupsManagerFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbase = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
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

        binding.addClubGroupInput.setOnClickListener(this);
        binding.addClubGroupButton.setOnClickListener(this);

        return binding.getRoot();
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addClubGroupInput:
                createEditTextView(view, binding.addClubGroupInput, "New Club/Group", false);
                break;
            case R.id.addClubGroupButton:
                createClubGroup();
                break;
        }
    }

    private void createClubGroup() {
        String newClubGroup = binding.addClubGroupInput.getText().toString();
        if (!newClubGroup.equals("")) {
            if (!clubsGroupsList.contains(newClubGroup)) {
                dbase.child("clubsGroups").child(newClubGroup).child("members").push();
                dbase.child("clubsGroups").child(newClubGroup).child("members").child(user.getUid()).child("uid").setValue(user.getUid());
                dbase.child("clubsGroups").child(newClubGroup).child("members").child(user.getUid()).child("name").setValue(user.getDisplayName());
                dbase.child("clubsGroups").child(newClubGroup).child("members").child(user.getUid()).child("email").setValue(user.getEmail());
                Toast.makeText(getContext(), "Your club/group has been added.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "This club/group already exists!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Input cannot be empty!", Toast.LENGTH_SHORT).show();
        }
        binding.addClubGroupInput.setText("");
    }

    private void createEditTextView(View view, TextView textView, String title, Boolean isMember) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle(title);

        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.text_input_layout, (ViewGroup) getView(), false);
        final EditText input = (EditText) viewInflated.findViewById(R.id.input);
        input.setText(textView.getText().toString());
        builder.setView(viewInflated);

        builder.setPositiveButton("ENTER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                textView.setText(input.getText().toString());
            }
        });

        builder.show();
    }
}