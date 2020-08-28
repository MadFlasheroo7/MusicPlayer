package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import android.os.Bundle;

public class Settings_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.userProfile, new ProfileSettings())
                .commit();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.themeSettings, new ThemeSettings())
                .commit();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.abtSettings , new AbtSettings())
                .commit();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.updateSettings , new updateSettings())
                .commit();
    }
    public static class ProfileSettings extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.profile_preferences, rootKey);
        }
    }
    public static class ThemeSettings extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.theme_preferences, rootKey);
        }
    }
    public static class AbtSettings extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.abt_preferences, rootKey);
        }
    }
    public static class updateSettings extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.update_preferences, rootKey);
        }
    }
}