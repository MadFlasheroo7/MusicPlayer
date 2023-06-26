package com.example.musicplayer;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.app.AppCompatDelegate;
//import androidx.constraintlayout.widget.ConstraintLayout;
//import androidx.preference.Preference;
//import androidx.preference.PreferenceFragmentCompat;
//import androidx.preference.PreferenceManager;
//import androidx.preference.SwitchPreferenceCompat;
//
//import android.content.SharedPreferences;
//import android.graphics.Color;
//import android.os.Bundle;
//
//public class Settings_Activity extends AppCompatActivity {
//
//    SharedPreferences sharedPreferences;
//    SharedPreferences.Editor editor;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_settings);
//        Settings();
//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.userProfile, new ProfileSettings())
//                .commit();
//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.themeSettings, new ThemeSettings())
//                .commit();
//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.abtSettings , new AbtSettings())
//                .commit();
//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.updateSettings , new updateSettings())
//                .commit();
//    }
//    public static class ProfileSettings extends PreferenceFragmentCompat {
//        @Override
//        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
//            setPreferencesFromResource(R.xml.profile_preferences, rootKey);
//        }
//    }
//    public void Settings(){
//        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
//    }
//    public static class ThemeSettings extends PreferenceFragmentCompat {
//        @Override
//        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
//            setPreferencesFromResource(R.xml.theme_preferences, rootKey);
////            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//            final SwitchPreferenceCompat ModeSwitch = findPreference("darkMode");
//            assert ModeSwitch != null;
//            ModeSwitch.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//                @Override
//                public boolean onPreferenceClick(Preference preference) {
////                    ModeSwitch.setSummaryOn();
//                    if (ModeSwitch.isChecked()){
//                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                    }else {
//                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                    }
//                    return false;
//                }
//            });
//            SwitchPreferenceCompat BlackMode = findPreference("blackMode");
//            assert BlackMode != null;
//            BlackMode.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//                @Override
//                public boolean onPreferenceClick(Preference preference) {
//                    return false;
//                }
//            });
//        }
//    }
//    public static class AbtSettings extends PreferenceFragmentCompat {
//        @Override
//        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
//            setPreferencesFromResource(R.xml.abt_preferences, rootKey);
//        }
//    }
//    public static class updateSettings extends PreferenceFragmentCompat {
//        @Override
//        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
//            setPreferencesFromResource(R.xml.update_preferences, rootKey);
//        }
//    }
//}