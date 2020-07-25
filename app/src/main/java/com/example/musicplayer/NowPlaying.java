/*

    Now Playing Activity

 */
package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.AudioManager;
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

    //Declaring Views
    TextView song_name, artist_name, duration_played, duration_total;
    ImageButton nxtBtn, prevBtn, playPauseBtn, repeatBtn, shuffleBtn;
    ImageView cover_art, vol_up, vol_down;
    SeekBar track_seek, seek_vol;

    static ArrayList<MusicFiles> listSongs = new ArrayList<>();
    static Uri uri;
    static MediaPlayer mediaPlayer;
    AudioManager audioManager;
    private Handler handler = new Handler();
    int position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing);
        initViews();
        getIntentMethod();

        //setting the song title and artist
        song_name.setText(listSongs.get(position).getTitle());
        artist_name.setText(listSongs.get(position).getArtist());

        //this code sets the seek bar progress
        track_seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser)
                {
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
                    int currentPosition = mediaPlayer.getCurrentPosition() / 1000;
                    track_seek.setProgress(currentPosition);
                    duration_played.setText(formattedTime(currentPosition));
                }
                handler.postDelayed(this,1000);
            }
        });

        //this code manages the Media Volume
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        assert audioManager != null;
        int maxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int curVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        seek_vol.setMax(maxVol);
        seek_vol.setProgress(curVol);
        seek_vol.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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

    }

    @Override
    protected void onResume()
    {
        playThreadBtn();
        nxtThreadBtn();
        prevThreadBtn();
        super.onResume();
    }

    private void playThreadBtn()
    {
        Thread playThread = new Thread() {
            @Override
            public void run() {
                super.run();
                playPauseBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playPauseBtnClicked();
                    }
                });
            }
        };
        playThread.start();
    }

    //this function Plays/pause the song
    private void playPauseBtnClicked()
    {
        if (mediaPlayer.isPlaying())
        {
            playPauseBtn.setImageResource(R.drawable.ic_play_btn);
            mediaPlayer.pause();
            track_seek.setMax(mediaPlayer.getDuration() / 1000);
            Toast.makeText(this,"Pausing your Music....",Toast.LENGTH_SHORT).show();
            NowPlaying.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null){
                        int currentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        track_seek.setProgress(currentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });
        }else {
            Toast.makeText(this,"Playing your Music",Toast.LENGTH_SHORT).show();
            playPauseBtn.setImageResource(R.drawable.ic_pause_btn);
            mediaPlayer.start();
            track_seek.setMax(mediaPlayer.getDuration() / 1000);
            NowPlaying.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null){
                        int currentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        track_seek.setProgress(currentPosition);

                    }
                    handler.postDelayed(this,1000);
                }
            });
        }
    }

    private void nxtThreadBtn()
    {
        Thread nxtThread = new Thread() {
            @Override
            public void run() {
                super.run();
                nxtBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nxtBtnClicked();
                    }
                });
            }
        };
        nxtThread.start();
    }

    //this function is for navigating to Next song
    private void nxtBtnClicked()
    {
            mediaPlayer.stop();
            mediaPlayer.release();
            position = ((position + 1) % listSongs.size());
            uri = Uri.parse(listSongs.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            duration_total.setText(formattedTime(mediaPlayer.getDuration() / 1000));
            metaData(uri);
            song_name.setText(listSongs.get(position).getTitle());
            artist_name.setText(listSongs.get(position).getArtist());
            track_seek.setMax(mediaPlayer.getDuration() / 1000);
            Toast.makeText(this,"!! Playing next song !!",Toast.LENGTH_SHORT).show();
            playPauseBtn.setImageResource(R.drawable.ic_pause_btn);
            mediaPlayer.start();
            NowPlaying.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null){
                        int currentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        track_seek.setProgress(currentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });
    }

    private void prevThreadBtn()
    {
        Thread prevThread = new Thread() {
            @Override
            public void run() {
                super.run();
                prevBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        prevBtnClicked();
                    }
                });
            }
        };
        prevThread.start();
    }

    //this function is for navigating to previous song
    private void prevBtnClicked()
    {
            mediaPlayer.stop();
            mediaPlayer.release();
            position = ((position - 1) % listSongs.size());
            uri = Uri.parse(listSongs.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            duration_total.setText(formattedTime(mediaPlayer.getDuration() / 1000));
            metaData(uri);
            song_name.setText(listSongs.get(position).getTitle());
            artist_name.setText(listSongs.get(position).getArtist());
            track_seek.setMax(mediaPlayer.getDuration() / 1000);
            Toast.makeText(this,"!! Playing previous song !!",Toast.LENGTH_SHORT).show();
            NowPlaying.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null){
                        int currentPosition = mediaPlayer.getCurrentPosition() / 1000;
                        track_seek.setProgress(currentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });
            playPauseBtn.setImageResource(R.drawable.ic_pause_btn);
            mediaPlayer.start();
    }

    //Formatting time to show current duration and total duration
    private String formattedTime(int currentPosition)
    {
        String totalOut;
        String totalNew;
        String seconds = String.valueOf(currentPosition % 60) ;
        String minutes = String.valueOf(currentPosition / 60);
        totalOut = minutes + ":" + seconds;
        totalNew = minutes + ":" + "0" + seconds;
        if (seconds.length() == 1){
            return totalNew;
        }else {
            return totalOut;
        }
    }

    //This method plays the song
    private void getIntentMethod()
    {
        position = getIntent().getIntExtra("Position",0);
        listSongs = musicFiles;
        if (listSongs != null){
            playPauseBtn.setImageResource(R.drawable.ic_pause_btn);
            uri = Uri.parse(listSongs.get(position).getPath());
        }
        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
        mediaPlayer.start();
        track_seek.setMax(mediaPlayer.getDuration() / 1000);
        duration_total.setText(formattedTime(mediaPlayer.getDuration() / 1000));
        metaData(uri);
    }

    //Initializing the above declared Views
    private void initViews()
    {
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

    //this function checks weather the audio file has a embedded image or not
    private void metaData(Uri uri)
    {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri.toString());
        byte[] albumArt = retriever.getEmbeddedPicture();
        if (albumArt != null){
            Glide.with(this)
                    .asBitmap()
                    .load(albumArt)
                    .into(cover_art);
        }else {
            Glide.with(this)
                    .asDrawable()
                    .load(R.drawable.ic_album)
                    .into(cover_art);
        }

    }

}
