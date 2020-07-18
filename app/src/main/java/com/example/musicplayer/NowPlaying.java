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
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.musicplayer.MainActivity.musicFiles;

public class NowPlaying extends AppCompatActivity {

    TextView song_name, artist_name, duration_played, duration_total;
    ImageButton nxtBtn, prevBtn, playPauseBtn, repeatBtn, shuffleBtn;
    ImageView cover_art, vol_up, vol_down;
    SeekBar track_seek, seek_vol;
    static ArrayList<MusicFiles> listSongs = new ArrayList<>();
    static Uri uri;
    static MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    int position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing);
        initViews();
        getIntentMethod();
        song_name.setText(listSongs.get(position).getTitle());
        artist_name.setText(listSongs.get(position).getArtist());
        track_seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser){
                    mediaPlayer.seekTo(progress * 1000);
//                    duration_played.setText(formattedTime(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        NowPlaying.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null){
                    int currentPosition = mediaPlayer.getCurrentPosition() / 1000;
                    track_seek.setProgress(currentPosition);
                    duration_played.setText(formattedTime(currentPosition));

//                    duration_total.setText(formattedTime(mediaPlayer.getDuration()));
                }
                handler.postDelayed(this,1000);
            }
        });
    }

    private String formattedTime(int currentPosition) {
        String totalOut = "", totalNew = "";
        String seconds = String.valueOf(currentPosition % 60) ;
        String minutes = String.valueOf(currentPosition / 60);
        totalOut = minutes + ":" + seconds;
        totalNew = minutes + ":" + "0" + seconds;
        if (seconds.length() == 1){
            return totalNew;
        }else {
            return totalOut;
        }
//        String finalTimerString = "";
//        String secondsString;
//
//        //Converting total duration into time
//        int hours = (int) (currentPosition / 3600000);
//        int minutes = (int) (currentPosition % 3600000) / 60000;
//        int seconds = (int) ((currentPosition % 3600000) % 60000 / 1000);

//        // Adding hours if any
//        if (hours > 0)
//            finalTimerString = hours + ":";
//
//        // Prepending 0 to seconds if it is one digit
//        if (seconds < 10)
//            secondsString = "0" + seconds;
//        else
//            secondsString = "" + seconds;
//
//        finalTimerString = finalTimerString + minutes + ":" + secondsString;
//
//        // Return timer String;
//        return finalTimerString;
    }

    private void getIntentMethod() {
        position = getIntent().getIntExtra("Position",-1);
        listSongs = musicFiles;
        if (listSongs != null){
            playPauseBtn.setImageResource(R.drawable.ic_pause_btn);
            uri = Uri.parse(listSongs.get(position).getPath());
        }
        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            mediaPlayer.start();
        }else {
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            mediaPlayer.start();
        }
        track_seek.setMax(mediaPlayer.getDuration() / 1000);
//        metaData(uri);
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

//    private void metaData(Uri uri){
//        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//        retriever.setDataSource(uri.toString());
//        int durationTotal = Integer.parseInt(listSongs.get(position).getDuration()) / 1000;
//        duration_total.setText(formattedTime(durationTotal));
//        byte[] albumArt = retriever.getEmbeddedPicture();
//        if (albumArt != null){
//            Glide.with(this)
//                    .asBitmap()
//                    .load(albumArt)
//                    .into(cover_art);
//        }else {
//            Glide.with(this)
//                    .asBitmap()
//                    .load(R.drawable.ic_album)
//                    .into(cover_art);
//        }
//    }
}
