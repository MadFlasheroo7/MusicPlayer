package com.example.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

public class Splash_Screen extends AppCompatActivity
{
    Animation topAnim,bottomAnim;
    ImageView music,player,mbox;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_activity);
        sysUI();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        topAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bottom_animation);

        music = findViewById(R.id.Music);
        player = findViewById(R.id.player);
        mbox = findViewById(R.id.Mbox);

        music.setAnimation(topAnim);
        player.setAnimation(bottomAnim);
        mbox.setAnimation(topAnim);

        int SPLASH_SCREEN = 2000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Splash_Screen.this,App_intro.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_SCREEN);
    }
    private void sysUI(){
        View view = getWindow().getDecorView();
        view.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN|
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_IMMERSIVE|View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|
                        View.SYSTEM_UI_FLAG_LOW_PROFILE
        );
    }
}

