package com.example.musicplayer.Services;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.musicplayer.MusicFiles;
import com.example.musicplayer.R;

public class Notification_services extends Application {
    public static final String channelID = "channel ID";
    public static final String channelID_2= "channel ID 2";
    public static final String channel_play_pause = "action_play_pause";
    public static final String channel_next = "action_next";
    public static final String channel_previous = "action_previous";
    public static final String channel_repeat = "action_repeat";
    public static final String channel_shuffle = "action_shuffle";

    public static Notification notification;

//    public static void create_notification(Context context, MusicFiles musicFiles,int size){
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
////            NotificationChannel notificationChannel = new NotificationChannel(
////                    CHANNEL_1_ID,"Music Player's notification", NotificationManager.IMPORTANCE_HIGH);
////            notificationChannel.setDescription("This is my first notification");
////            NotificationManager manager = getSystemService(NotificationManager.class);
////            manager.createNotificationChannel(notificationChannel);
//            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
//            MediaSessionCompat mediaSessionCompat = new MediaSessionCompat(context,"tag");
//
////            Bitmap albumArt = BitmapFactory.decodeResource(context.getResources(),musicFiles.getAlbum());
//            notification = new Notification.Builder(context,channelID)
//                    .setSmallIcon(R.drawable.ic_notification_icon)
//                    .setContentTitle(musicFiles.getTitle())
//                    .setContentText(musicFiles.getArtist())
////                    .setLargeIcon(albumArt)
//                    .setOnlyAlertOnce(true)
//                    .setShowWhen(false)
////                    .setPriority(No.PRIORITY_LOW)
//                    .build();
//            notificationManager.notify(1,notification);
//        }
//
//    }

    @Override
    public void onCreate() {
        super.onCreate();
        create_notification();
    }

    private void create_notification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(
                    channelID,"Music Player's notification", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("This is my first notification");
            NotificationChannel notificationChannel2 = new NotificationChannel(
                    channelID_2,"Music Player's notification", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel2.setDescription("This is my first notification");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
            notificationManager.createNotificationChannel(notificationChannel2);
        }
    }
}
