package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
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


    ImageButton previousBtn,nxtBtn, play_pause_btn;
    TextView trackTitle,trackDuration1,trackDuration2;
    SeekBar seek_Track,seek_Vol;

    String songname;
    static MediaPlayer mediaPlayer;
    int position;

    ArrayList<File> songName;
//    Thread updateSeekBar;
    AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing);


        play_pause_btn = findViewById(R.id.play_pause_Btn);
        previousBtn = findViewById(R.id.previousBtn);
        nxtBtn = findViewById(R.id.nxtBtn);

        trackTitle = findViewById(R.id.titleTrack);
        trackDuration1 = findViewById(R.id.trackDuration1);
        trackDuration2 = findViewById(R.id.trackDuration2);

        seek_Track = findViewById(R.id.seek_Track);
        seek_Vol = findViewById(R.id.seek_Vol);

        // Volume seek bar code
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        assert audioManager != null;
        int maxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int curVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        seek_Vol = findViewById(R.id.seek_Vol);
        seek_Vol.setMax(maxVol);
        seek_Vol.setProgress(curVol);
        seek_Vol.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //Track seek Bar code
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                seek_Track.setProgress(mediaPlayer.getCurrentPosition());
            }
        },0,500);

        seek_Track = findViewById(R.id.seek_Track);
        seek_Track.setMax(mediaPlayer.getDuration());
        seek_Track.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mediaPlayer.seekTo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        if (mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        Intent i = getIntent();
        Bundle bundle = i.getExtras();

        assert bundle != null;
        songName = (ArrayList) bundle.getParcelableArrayList("songs");
        assert songName != null;
        songname = songName.get(position).getName();
        final String songTitle = i.getStringExtra("songName");
        trackTitle.setText(songTitle);
        trackTitle.setSelected(true);

        // Play Pause Button
        play_pause_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seek_Track.setMax(mediaPlayer.getDuration());
                if (mediaPlayer.isPlaying()){
                    play_pause_btn.setBackgroundResource(R.drawable.ic_play_btn);
                    mediaPlayer.pause();
                }else {
                    play_pause_btn.setBackgroundResource(R.drawable.ic_pause_btn);
                    mediaPlayer.start();
                }
            }
        });

        //Forward btn code
        nxtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                position = ((position+1)%songName.size());

                Uri u = Uri.parse(songName.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(),u);

                songname = songName.get(position).getName();
                trackTitle.setText(songname);
                mediaPlayer.start();
            }
        });
        //Previous Btn code
        previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();

                position = ((position-1)<0)?(songName.size()-1):(position-1);
                Uri u = Uri.parse(songName.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(),u);

                songname = songName.get(position).getName();
                trackTitle.setText(songname);
                mediaPlayer.start();
            }
        });
    }


}
