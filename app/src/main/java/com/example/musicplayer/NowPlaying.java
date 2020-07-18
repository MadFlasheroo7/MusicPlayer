package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class NowPlaying extends AppCompatActivity {

    TextView song_name, artist_name, duration_played, duration_total;
    ImageButton nxtBtn, prevBtn, playPauseBtn, repeatBtn, shuffleBtn;
    ImageView cover_art, vol_up, vol_down;
    SeekBar track_seek, seek_vol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing);
        initViews();
    }
    
    private void initViews() {
        song_name = findViewById(R.id.titleTrack);
        artist_name = findViewById(R.id.song_artist);
        duration_played = findViewById(R.id.trackDuration);
        duration_total = findViewById(R.id.trackDuration_total);

        nxtBtn = findViewById(R.id.nxtBtn);
        prevBtn = findViewById(R.id.previousBtn);
        playPauseBtn = findViewById(R.id.play_pause_Btn);
        repeatBtn = findViewById(R.id.repeatBtn);
        shuffleBtn = findViewById(R.id.shuffleBtn);

        cover_art = findViewById(R.id.coverArt);
        vol_up = findViewById(R.id.volUp);
        vol_down = findViewById(R.id.volDown);

        track_seek = findViewById(R.id.seek_Track);
        seek_vol = findViewById(R.id.seek_Vol);
    }

}
