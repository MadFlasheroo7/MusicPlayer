package com.example.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Splash_Screen extends AppCompatActivity
{
    private  static int SPLASH_SCREEN = 2000;
    Animation topAnim,bottomAnim;
    ImageView music,player,mbox;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_activity);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        topAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bottom_animation);

        music = findViewById(R.id.Music);
        player = findViewById(R.id.player);
        mbox = findViewById(R.id.Mbox);

        music.setAnimation(topAnim);
        player.setAnimation(bottomAnim);
        mbox.setAnimation(topAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Splash_Screen.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN);
    }
}

