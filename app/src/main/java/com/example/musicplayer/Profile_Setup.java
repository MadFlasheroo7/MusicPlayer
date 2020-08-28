package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import android.os.Bundle;

public class Profile_Setup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile__setup);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.userSetup, new ProfileSetup())
                .commit();
    }
    public static class ProfileSetup extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.profile_setup_preferences, rootKey);
        }
    }
}