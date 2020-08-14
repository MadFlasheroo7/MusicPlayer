/*  Hey world Iam Jayesh Seth aka "Mad_Flasher"

    This is my first android project ,and it was successful due to android documentation and very kind developer friends
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
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import java.util.ArrayList;

import static com.example.musicplayer.MusicAdapter.mContext;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    static ArrayList<MusicFiles> musicFiles;
    static ArrayAdapter<MusicAdapter> musicAdapter;
    SearchView searchView;
    public static final int REQUEST_CODE = 1;
    static boolean shuffleBoolean = false, repeatBoolean = false;
    public static final String CHANNEL_1_ID = "channel1";


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

        NavController navController = Navigation.findNavController(this,R.id.navHostFragment);
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.menuHome,
                R.id.menuNowPlaying,
                R.id.menuAbout).build();
//        NavigationUI.setupActionBarWithNavController(this,navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setElevation(24);


        /*
            After adding these codes it got fucked up
         */
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.menuTG:
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("https://t.me/mad_flasher_oo7"));
                        startActivity(intent);
                        break;

                    case R.id.menuTwitter:
                        Intent intent1 = new Intent(Intent.ACTION_VIEW);
                        intent1.setData(Uri.parse("https://twitter.com/jayesh_seth_"));
                        startActivity(intent1);
                        break;
                    case R.id.menuInstagram:
                        Intent intent2 = new Intent((Intent.ACTION_VIEW));
                        intent2.setData(Uri.parse("https://www.instagram.com/iam_the_iron_man/"));
                        startActivity(intent2);
                        break;
                    case R.id.menuReddit:
                        Intent intent3 = new Intent((Intent.ACTION_VIEW));
                        intent3.setData(Uri.parse("https://www.reddit.com/user/Mad_flasher"));
                        startActivity(intent3);
                        break;
                    case R.id.menuAbout:
                        Intent intent4 = new Intent(getApplicationContext(),About_activity.class);
                        startActivity(intent4);
                        break;
                    case R.id.menuAllsongs:
                        getSupportFragmentManager().beginTransaction().replace(R.id.navHostFragment,new AllSongs()).commit();
                        Intent intent5 = new Intent(getApplicationContext(),AllSongs.class);
                        startActivity(intent5);
                        break;
                }
                return false;
            }
        });

//        final TextView textTitle = findViewById(R.id.textTitle);
//
//        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
//            @Override
//            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
//                textTitle.setText(destination.getLabel());
//            }
//        });

        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                String userInput = s.toLowerCase();
                ArrayList<MusicFiles> mFiles = new ArrayList<>();
                for (MusicFiles song : musicFiles){
                    if (song.getTitle().toLowerCase().contains(userInput)){
                        mFiles.add(song);
                    }
                }
                AllSongs.musicAdapter.updateList(mFiles);
                return true;
            }
        });
    }


    private void CreateNotificationChannel()
    {
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
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