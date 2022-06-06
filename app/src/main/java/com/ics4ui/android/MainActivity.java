package com.ics4ui.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ics4ui.android.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static ArrayList<Event> eventList = new ArrayList<>();

    public static Event getEventFromList(int i){
        return eventList.get(i);
    }

    public static void addEventToList(Event event){
        eventList.add(event);
    }

    public static void removeEventFromList(int i){
        eventList.remove(i);
    }


    CalendarFragment calendarFragment = new CalendarFragment();
    HomeFragment homeFragment = new HomeFragment();
    SettingsFragment settingsFragment = new SettingsFragment();

    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;

    String DisplayName;
    String Email;


    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_bar);
        bottomNavigationView.setSelectedItemId(R.id.home);

        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if (account != null) {
            DisplayName = account.getDisplayName();
            Email = account.getEmail();
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.calendar:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, calendarFragment).commit();
                        return true;
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, homeFragment).commit();
                        return true;
                    case R.id.settings:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, settingsFragment).commit();
                        return true;
                }
                return false;
            }
        });
    }

    public void SignOut() {
        googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                finish();
                startActivity(new Intent(getApplicationContext(), SignInActivity.class));
            }
        });
    }
}