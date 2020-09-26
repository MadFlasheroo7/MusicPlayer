package com.example.musicplayer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.github.appintro.AppIntro2;
import com.github.appintro.AppIntroFragment;
import com.github.appintro.AppIntroPageTransformerType;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class App_intro extends AppIntro2 {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addSlide(AppIntroFragment.newInstance("!! Welcome !!",
                "Hey There, good to see you,\nthis app is made with love by JAYESH SETH as his first android project",
                R.drawable.ic_welcome,
                0,0,0,0,0,
                R.drawable.introbg_1));
        addSlide(AppIntroFragment.newInstance("!! First Project !!",
                "As u read it earlier its my first android project \nSo u may get some issues or some things might not work \n so my contact links are in About section ,contact me to share bugs :)",
                R.drawable.ic_first,
                0,0,0,0,0,
                R.drawable.introbg_2));
        addSlide(AppIntroFragment.newInstance("!! Thank You !!",
                "Thank you so much for trying out my music player,its my pleasure that you are trying this app\nand as this app request storage permission to fetch songs",
                R.drawable.ic_thank_you,
                0,0,0,0,0,
                R.drawable.introbg_3));
        setTransformer(new AppIntroPageTransformerType.Parallax(
                5.0, -4.0, 2.0));
        askForPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},3,true);
        sharedPreferences = getApplicationContext().getSharedPreferences("IntroPreference", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if (sharedPreferences != null){
            boolean checkShared = sharedPreferences.getBoolean("CheckStated",false);
            if (checkShared){
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        }
    }

    @Override
    protected void onUserDeniedPermission(@NotNull String permissionName) {
        super.onUserDeniedPermission(permissionName);
        Toast.makeText(this, "WE REQUIRE PERMISSION TO PLAY YOUR SONGS", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onUserDisabledPermission(@NotNull String permissionName) {
        super.onUserDisabledPermission(permissionName);
        Toast.makeText(this, "WE REQUIRE PERMISSION TO PLAY YOUR SONGS", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onSkipPressed(@Nullable Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        Toast.makeText(this, "DAMN I WORKED HARD ON IT, AND YOU DIDN'T EVEN SEE :-(", Toast.LENGTH_LONG).show();
        editor.putBoolean("CheckStated",false).commit();
    }

    @Override
    protected void onDonePressed(@Nullable Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        Toast.makeText(this, "THANK YOU for staying till last slide ,Now enjoy your music :-)", Toast.LENGTH_LONG).show();
        editor.putBoolean("CheckStated",true).commit();
    }
}