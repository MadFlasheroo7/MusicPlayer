/*
    Hey world Iam Jayesh Seth aka "Mad_Flasher" this is my first android project ,and it was successful due to android documentation and very kind developer friends
    i'll try my best comment the code so that every one can understand i have done..........
    .
    .
    .
   Hope it helps you in some way
   .
   .
   Keep Coding


  It was started on june 10th of 2020
  and any changes done will be updated here on github also
 */
package com.example.musicplayer;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    static ArrayList<MusicFiles> musicFiles;
    public static final int REQUEST_CODE = 1;
    static boolean shuffleBoolean = false, repeatBoolean = false;
    public static final String CHANNEL_1_ID = "channel1";
//    public static final String CHANNEL_2_ID = "channel2";


    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        runTimePermission();
        CreateNotificationChannel();

        //Making Navigation drawer
        final DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);

        //Giving on click to our hamburger icon
        findViewById(R.id.hamburger).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setItemIconTintList(null);

        NavController navController = Navigation.findNavController(this,R.id.navHostFragment);
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.menuHome,R.id.menuNowPlaying,R.id.menuAbout).build();
//        NavigationUI.setupActionBarWithNavController(this,navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


//        final TextView textTitle = findViewById(R.id.textTitle);
//
//        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
//            @Override
//            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
//                textTitle.setText(destination.getLabel());
//            }
//        });

    }

    private void CreateNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(
                    CHANNEL_1_ID,"Music Player's notification", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("This is my first notification");
//            NotificationChannel notificationChannel2 = new NotificationChannel(
//                    CHANNEL_2_ID,"Music Player's notification", NotificationManager.IMPORTANCE_LOW);
//            notificationChannel2.setDescription("This is my second notification");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
//            manager.createNotificationChannel(notificationChannel2);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.navHostFragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)|| super.onSupportNavigateUp();
    }



    //Using MediaStore to fetch audio files from device
    @RequiresApi(api = Build.VERSION_CODES.R)
    public static ArrayList<MusicFiles> getAudio(Context context){
        ArrayList<MusicFiles> tempAudioList = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] proj = {
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA, // for Path
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media._ID
        };
        @SuppressLint("Recycle")
        Cursor cursor = context.getContentResolver().query(uri,proj,null,null,null,null);

        if (cursor != null){
            while (cursor.moveToNext()){
                String album = cursor.getString(0);
                String title = cursor.getString(1);
                String duration = cursor.getString(2);
                String path = cursor.getString(3);
                String artist = cursor.getString(4);

                String id = cursor.getString(5);
                MusicFiles musicFiles = new MusicFiles(path,title,artist,duration,album,id);
                Log.e("Path : " + path,"Album : " + album);
                Log.e("Title : " + title,"Artist : " + artist);
                tempAudioList.add(musicFiles);
            }
            cursor.close();
        }
        return tempAudioList;
    }

    //Requesting permission to read Internal Storage
    @RequiresApi(api = Build.VERSION_CODES.R)
    public void runTimePermission(){
        if (ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE);
        }else {
//            Toast.makeText(this,"!! PerMission Granted !!",Toast.LENGTH_LONG).show();

            // if the permission is granted it will show the audio files
            musicFiles = getAudio(this);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this,"!! PerMission Granted !!",Toast.LENGTH_LONG).show();
            }else
            {
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE);

            }
        }
    }


}