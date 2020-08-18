/*

    Now Playing Activity

 */
package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.media.app.NotificationCompat;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.CaseMap;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.media.session.MediaSessionCompat;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.Random;


import static com.example.musicplayer.MainActivity.CHANNEL_1_ID;
import static com.example.musicplayer.MainActivity.musicFiles;
import static com.example.musicplayer.MainActivity.repeatBoolean;
import static com.example.musicplayer.MainActivity.shuffleBoolean;
import static com.example.musicplayer.MusicAdapter.mFiles;

public class NowPlaying extends AppCompatActivity implements MediaPlayer.OnCompletionListener {

    //Declaring Views
    TextView song_name, artist_name, duration_played, duration_total;
    ImageButton nxtBtn, prevBtn, playPauseBtn, repeatBtn, shuffleBtn;
    ImageView cover_art, vol_up, vol_down;
    SeekBar track_seek, seek_vol;

    static ArrayList<MusicFiles> listSongs;
    static Uri uri;
    static MediaPlayer mediaPlayer;
    AudioManager audioManager;
    private Handler handler = new Handler();
    int position;
    static Context context;

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

        song_name.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        song_name.setSelected(true);

        mediaPlayer.setOnCompletionListener(this);

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

        shuffleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shuffleBoolean)
                {
                    shuffleBoolean = false;
                    shuffleBtn.setImageResource(R.drawable.ic_shuffle_btn2);
                }else {
                    shuffleBoolean = true;
                    shuffleBtn.setImageResource(R.drawable.ic_shuffle_btn_on);
                }
            }
        });

        repeatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (repeatBoolean)
                {
                    repeatBoolean = false;
                    repeatBtn.setImageResource(R.drawable.ic_repeat);
                }else{
                    repeatBoolean = true;
                    repeatBtn.setImageResource(R.drawable.ic_repeat_on);
                }
            }
        });


        Bitmap pic = BitmapFactory.decodeResource(getResources(),R.drawable.box2);
        MediaSessionCompat mediaSessionCompat = new MediaSessionCompat(this,"tsg");
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//        String title = listSongs.get(position).getTitle();
        Notification notification = new androidx.core.app.NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_notification_icon)
                .setLargeIcon(pic)
//                .setContentTitle(title)
                .addAction(R.drawable.ic_repeat,"Repeat",null)
                .addAction(R.drawable.ic_notification_prev,"previous",null)
                .addAction(R.drawable.ic_play,"play",null)
                .addAction(R.drawable.ic_notification_nxt,"Next button",null)
                .addAction(R.drawable.ic_shuffle_btn2,"Shuffle",null)
//                .setStyle(new NotificationCompat.MediaStyle().setShowActionsInCompactView(1,2,3))
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView(1,2,3)
                        .setMediaSession(mediaSessionCompat.getSessionToken()))
                .setPriority(androidx.core.app.NotificationCompat.PRIORITY_HIGH)
                .build();
        notificationManager.notify(1, notification);
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
            Toast.makeText(this,"Pausing your music....",Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this,"Playing your music",Toast.LENGTH_SHORT).show();
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
            if (shuffleBoolean && !repeatBoolean)
            {
                position = getRandom(listSongs.size() - 1);
            }else if (!shuffleBoolean && !repeatBoolean){
                position = ((position + 1) % listSongs.size());
            }
//            position = ((position + 1) % listSongs .size());
            uri = Uri.parse(listSongs.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            metaData(uri);
            duration_total.setText(formattedTime(mediaPlayer.getDuration() / 1000));
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
        mediaPlayer.setOnCompletionListener(this);
    }

    private int getRandom(int i)
    {
        Random random = new Random();
        return random.nextInt(i + 1);
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
        if (shuffleBoolean && !repeatBoolean)
        {
            position = getRandom(listSongs.size() - 1);
        }else if (!shuffleBoolean && !repeatBoolean){
            position = ((position - 1) % listSongs.size());
        }
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
            mediaPlayer.setOnCompletionListener(this);
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
        listSongs = mFiles;
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

//            bitmap = BitmapFactory.decodeByteArray(albumArt, 0, albumArt.length);
            Glide.with(this)
                    .asBitmap()
                    .load(albumArt)
                    .into(cover_art);

            AlbumartAnimation(this,cover_art, null);
        }else
            {
            Glide.with(this)
                    .asDrawable()
                    .load(R.drawable.ic_album)
                    .into(cover_art);
//            Glide.with(this)
//                    .asGif()
//                    .load(R.drawable.cat)
//                    .into(cover_art);
            AlbumartAnimation(this,cover_art, null);
        }
//        AlbumartAnimation(this,cover_art, null);
    }

    //Album art animation
    private void AlbumartAnimation(final Context context, final ImageView imageView, final Bitmap bitmap)
    {
        final Animation anim_OUT = AnimationUtils.loadAnimation(context,android.R.anim.slide_out_right);
        final Animation anim_IN = AnimationUtils.loadAnimation(context,android.R.anim.slide_in_left);
        anim_OUT.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
//                Glide.with(context).load(bitmap).into(imageView);
                anim_IN.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                imageView.startAnimation(anim_IN);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imageView.startAnimation(anim_OUT);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        nxtBtnClicked();
//        if (mediaPlayer != null)
//        {
//            Toast.makeText(this, "Automatically playing next song", Toast.LENGTH_SHORT).show();
//            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
//            mediaPlayer.start();
//            mediaPlayer.setOnCompletionListener(this);
//        }
    }


}
