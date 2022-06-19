package com.ics4ui.android;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.ics4ui.android.databinding.FragmentClubsGroupsOptionsBinding;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ClubsGroupsOptionsFragment extends Fragment implements View.OnClickListener {
    FragmentClubsGroupsOptionsBinding binding;

    DatabaseReference dbase;
    FirebaseAuth auth;
    FirebaseUser user;

    String clubName;

    Map<String, String> announcementKeys = new HashMap<>();
    Map<String, User> userList = new HashMap<>();
    ArrayList<String> clubGroupList = new ArrayList<>();

    ArrayAdapter<String> adapter;

    public ClubsGroupsOptionsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbase = FirebaseDatabase.getInstance().getReference();
        clubName = this.getArguments().getString("clubName");
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentClubsGroupsOptionsBinding.inflate(inflater, container, false);

        //Set clicklisteners for both buttons
        binding.addClubGroupAnnouncementButton.setOnClickListener(this);
        binding.removeClubGroupAnnouncementButton.setOnClickListener(this);
        binding.addClubGroupAnnouncementInput.setOnClickListener(this);
        binding.addClubGroupMemberInput.setOnClickListener(this);
        binding.addClubGroupMemberButton.setOnClickListener(this);
        binding.addClubGroupInput.setOnClickListener(this);
        binding.addClubGroupButton.setOnClickListener(this);

        //This fetches the announcements from the database then populates the removeAnnouncementSpinner
        fetchAnnouncements();
        fetchUsers();

        return binding.getRoot();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.addClubGroupAnnouncementButton:
                addClubGroupAnnouncement();
                break;
            case R.id.removeClubGroupAnnouncementButton:
                removeClubGroupAnnouncement();
                break;
            case R.id.addClubGroupAnnouncementInput:
                createEditTextView(view, binding.addClubGroupAnnouncementInput, "Announcement", false);
                break;
            case R.id.addClubGroupMemberButton:
                addClubGroupMember();
                break;
            case R.id.addClubGroupMemberInput:
                createEditTextView(view, binding.addClubGroupMemberInput, "New Member", true);
                break;
            case R.id.addClubGroupInput:
                createEditTextView(view, binding.addClubGroupInput, "New Club/Group", false);
                break;
            case R.id.addClubGroupButton:
                createClubGroup();
                break;
        }
    }

    private void createClubGroup() {
        Query query = dbase.child("clubsGroups");
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                clubGroupList.add(snapshot.getKey());
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) { }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private void addClubGroupMember() {
        String user = binding.addClubGroupMemberInput.getText().toString();
        if (user.equals("")) {
            Toast.makeText(getContext(), "No member was specified!", Toast.LENGTH_SHORT).show();
        } else {
            if (userList.containsKey(user)) {
                User userData = userList.get(user);
                dbase.child("clubsGroups").child(clubName).child("members").child(userData.getUid()).setValue(userData);

                String key = dbase.child("users").child(userData.getUid()).child("memberOf").push().getKey();
                dbase.child("users").child(userData.getUid()).child("memberOf").child(key).child("clubGroup").setValue(clubName);

                Toast.makeText(getContext(), "User added as member!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "User not found in database!", Toast.LENGTH_SHORT).show();
            }
        }
        binding.addClubGroupMemberInput.setText("");
    }

    private void removeClubGroupAnnouncement() {
        try {
            String announcement = binding.removeClubGroupAnnouncementSpinner.getSelectedItem().toString();
            String announcementKey = announcementKeys.get(announcement);

            try {
                dbase.child("clubsGroups")
                        .child(clubName)
                        .child("announcements")
                        .child(announcementKey)
                        .removeValue();
                Toast.makeText(getContext(), "Announcement removed!", Toast.LENGTH_SHORT).show();

                //Restart instance
                ClubsGroupsOptionsFragment optionsFragment = new ClubsGroupsOptionsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("clubName", clubName);
                optionsFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView2, optionsFragment).commit();
            } catch (Exception ignored) {}
        } catch (Exception e) {
            Toast.makeText(getContext(), "No announcement selected!", Toast.LENGTH_SHORT).show();
        }
    }

    private void addClubGroupAnnouncement() {
        if (TextUtils.isEmpty(binding.addClubGroupAnnouncementInput.getText())) {
                    Toast.makeText(getContext(), "Announcement cannot be empty!", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        String key = dbase.child("clubsGroups").child(clubName).child("announcements").push().getKey();
                        Map<String, Object> update = new HashMap<>();
                        update.put("/clubsGroups/" + clubName + "/announcements/" + key + "/announcement/", binding.addClubGroupAnnouncementInput.getText());
                        dbase.updateChildren(update);

                        Toast.makeText(getContext(), "Announcement added!", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "There was an error when adding the announcement!", Toast.LENGTH_SHORT).show();
                    }
                }
    }

    private void createEditTextView(View view, TextView textView, String title, Boolean isMember) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle(title);

        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.text_input_layout, (ViewGroup) getView(), false);
        final EditText input = (EditText) viewInflated.findViewById(R.id.input);
        builder.setView(viewInflated);

        builder.setPositiveButton("ENTER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (isMember) {
                    String addon = "";
                    if (userList.containsKey(input.getText().toString())) {
                        Toast.makeText(getContext(), "Found user: " + userList.get(input.getText().toString()).getName(), Toast.LENGTH_SHORT).show();
                    }
                }
                textView.setText(input.getText().toString());
            }
        });

        builder.show();
    }

    private void fetchUsers() {
        Query query = dbase.child("users");

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String uid = snapshot.getKey();
                User user = snapshot.getValue(User.class);
                user.setUid(uid);
                userList.put(user.getEmail(), user);
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
    }

    private void fetchAnnouncements() {
        ArrayList<String> announcementList = new ArrayList<>();

        Query query = dbase.child("clubsGroups")
                .child(clubName)
                .child("announcements");

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String announcement = snapshot.getValue(Announcements.class).getAnnouncement();
                announcementList.add(announcement);
                announcementKeys.put(announcement, snapshot.getKey());

                populateRemoveAnnouncementSpinner(announcementList);
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
    }

    private void populateRemoveAnnouncementSpinner(ArrayList<String> announcementList) {
        adapter = new ArrayAdapter(getContext(), R.layout.spinner_item, announcementList);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        binding.removeClubGroupAnnouncementSpinner.setAdapter(adapter);
        binding.removeClubGroupAnnouncementSpinner.setPopupBackgroundResource(R.drawable.spinner_dropdown_background);
    }
}
