package com.ics4ui.android;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ics4ui.android.databinding.FragmentSettingsBinding;

import java.util.HashMap;
import java.util.Map;

public class SettingsFragment extends Fragment {
    private FragmentSettingsBinding binding;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser account;
    DatabaseReference dbase;

    public SettingsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);

        dbase = FirebaseDatabase.getInstance().getReference();

        firebaseAuth = FirebaseAuth.getInstance();
        account = firebaseAuth.getCurrentUser();

        binding.displayName.setText(account.getDisplayName());
        binding.email.setText(account.getEmail());

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).SignOut();
            }
        });

        binding.manageClubsGroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new ClubsGroupsManagerFragment()).commit();
            }
        });

        binding.addAnnouncement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEditTextView(view);
            }
        });
    }

    private void createEditTextView(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("Add Announcement!");

        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.text_input_layout, (ViewGroup) getView(), false);
        final EditText input = (EditText) viewInflated.findViewById(R.id.input);
        builder.setView(viewInflated);

        builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (input.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Announcement text field empty!", Toast.LENGTH_SHORT).show();
                } else {
                    Announcements announcement = new Announcements();
                    announcement.setAnnouncement(input.getText().toString());

                    String key = dbase.child("announcements").push().getKey();
                    Map<String, Object> update = new HashMap<>();
                    update.put("/announcements/" + key + "/", announcement);
                    dbase.updateChildren(update);
                    Toast.makeText(getContext(), "Announcement added!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.show();
    }

}
