package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static com.example.musicplayer.MainActivity.musicFiles;


public class NowPlaying extends AppCompatActivity {

    TextView title_track, artist_name, trackDuration, trackDurationTotal;
    ImageView cover_art ,volumeUP ,volumeDOWN;
    ImageButton nxt_btn ,play_pause_btn ,prev_btn ,shuffle_btn, repeat_btn;
    SeekBar seek_track ,seek_vol;
    static ArrayList<MusicFiles> listSongs = new ArrayList<>();
    static Uri uri;
    static MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private Thread playThread, prevThread, nxtThread;
    int position = -1;

//    private boolean checkFlag = false,repeatFlag = false,playContinueFlag = false;
//    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing);
        initviews();
        getIntentMethod();
        title_track.setText(listSongs.get(position).getTitle());
        artist_name.setText(listSongs.get(position).getArtist());
        seek_track.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser){
                    mediaPlayer.seekTo(progress * 1000);
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
                    int currentPosition = mediaPlayer.getCurrentPosition() /1000;
                    seek_track.setProgress(currentPosition);
                    trackDuration.setText(formattedTime(currentPosition));
                }
                handler.postDelayed(this,1000);
            }
        });
    }

//    @Override
//    protected void onPostResume() {
//        playThreadBtn();
//        prevThreadBtn();
//        nxtThreadBtn();
//        super.onPostResume();
//    }
//
//    private void nxtThreadBtn() {
//    }
//
//    private void prevThreadBtn() {
//    }
//
//    private void playThreadBtn() {
//        playThread = new Thread(){
//            @Override
//            public void run() {
//                super.run();
//                play_pause_btn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        playPauseBtnClicked();
//                    }
//                });
//            }
//        };
//        playThread.start();
//    }

//    private void playPauseBtnClicked() {
//        if (mediaPlayer.isPlaying()){
//            play_pause_btn.setImageResource(R.drawable.ic_play_btn);
//            seek_track.setMax(mediaPlayer.getDuration() / 1000);
//            NowPlaying.this.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    if (mediaPlayer != null){
//                        int currentPosition = mediaPlayer.getCurrentPosition() / 1000;
//                        seek_track.setProgress(currentPosition);
//                    }
//                    handler.postDelayed(this,1000);
//                }
//            });
//        }else {
//            play_pause_btn.setImageResource(R.drawable.ic_pause_btn);
//            mediaPlayer.start();
//            seek_track.setMax(mediaPlayer.getDuration() / 1000);
//            NowPlaying.this.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    if (mediaPlayer != null){
//                        int currentPosition = mediaPlayer.getCurrentPosition() / 1000;
//                        seek_track.setProgress(currentPosition);
//                    }
//                    handler.postDelayed(this,1000);
//                }
//            });
//        }
//    }

    private String formattedTime(int currentPosition) {
        String totalOut = "";
        String totalNew = "";
        String seconds = String.valueOf(currentPosition % 60);
        String minutes = String.valueOf(currentPosition / 60);
        totalOut = minutes + ":" + seconds;
        totalNew = minutes + ":" + "0" + seconds;
        if (seconds.length() == 1){
            return totalNew;
        }else {
            return totalOut;
        }
    }


    private void getIntentMethod() {
        position = getIntent().getIntExtra("Position",-1);
        listSongs =  musicFiles;
        if (listSongs != null){
            play_pause_btn.setImageResource(R.drawable.ic_pause_btn);
            uri = Uri.parse(listSongs.get(position).getPath());
        }
        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
        mediaPlayer.start();
        seek_track.setMax(mediaPlayer.getDuration() / 1000);
//        metaData(uri);
    }

    private void initviews() {
//TextView
        title_track = findViewById(R.id.titleTrack);
        artist_name = findViewById(R.id.song_artist);
        trackDuration = findViewById(R.id.trackDuration);
        trackDurationTotal = findViewById(R.id.trackDuration_total);
//ImageView
        cover_art = findViewById(R.id.coverArt);
        volumeUP = findViewById(R.id.volUp);
        volumeDOWN = findViewById(R.id.volDown);
//Image Button
        nxt_btn = findViewById(R.id.nxtBtn);
        play_pause_btn = findViewById(R.id.play_pause_Btn);
        prev_btn = findViewById(R.id.previousBtn);
        shuffle_btn = findViewById(R.id.shuffleBtn);
        repeat_btn = findViewById(R.id.repeatBtn);
//SeekBar
        seek_track = findViewById(R.id.seek_Track);
        seek_vol = findViewById(R.id.seek_Vol);
    }

//    private void metaData(Uri uri)
//    {
//        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//        retriever.setDataSource(uri.toString());
//        int durationTotal = Integer.parseInt(listSongs.get(position).getDuration()) / 1000;
//        trackDurationTotal.setText(formattedTime(durationTotal));
//        byte[] art = retriever.getEmbeddedPicture();
//        if (art != null){
//            Glide.with(this)
//                    .asBitmap()
//                    .load(art)
//                    .into(cover_art);
//        }else {
//            Glide.with(this)
//                    .asBitmap()
//                    .load(R.drawable.ic_album)
//                    .into(cover_art);
//        }
//    }
}