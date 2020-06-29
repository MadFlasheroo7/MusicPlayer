package com.example.musicplayer;


import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AllSongs<createDataParse> extends Fragment {
    public static ContentResolver contentResolver1;
    public static ContentResolver contentResolver;
    public ArrayList<String> songlist;
    public ArrayList<String> newList;
    private ListView listView;
    private createDataParse createDataParse;

//    ListView listView;
//    String[] songNames;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
//    private ArrayList<File > readSongs(File root){
//        ArrayList<File> arrayList = new ArrayList<>();
//        File[] files = root.listFiles();
//        assert files != null;
//        for (File file: files){
//            if (file.isDirectory()){
//                arrayList.addAll(readSongs(file));
//            }else{
//                if (file.getName().endsWith(".mp3")){
//                    arrayList.add(file);
//                }
//            }
//        }
//        return arrayList;
//    }
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        createDataParse = (createDataParse) context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        listView = view.findViewById(R.id.listView);
        contentResolver = contentResolver1;
        setContent();
    }
    public void setContent(){
        boolean searchedList = false;
        songlist = new ArrayList<>();
        newList = new ArrayList<>();
        getMusic();
        songAdapter
    }
    public void getMusic() {
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri, null, null, null, null);
        if (songCursor != null && songCursor.moveToFirst()) {
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int songPath = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);

            do {
                songsList.add(new SongsList(songCursor.getString(songTitle), songCursor.getString(songArtist), songCursor.getString(songPath)));
            } while (songCursor.moveToNext());
            songCursor.close();
        }
    }
    public void runTimePermission(){
        Dexter.withContext(getContext())
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
//                       Toast.makeText(AllSongs.this,"PerMission Granted",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
//                        Toast.makeText(AllSongs.this,"PerMission Required",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        runTimePermission();
//        container.findViewById(R.id.listView);

//            final ArrayList<File> songs = readSongs(Environment.getExternalStorageDirectory());
//            songNames = new String[songs.size()];
//            for (int i = 0; i < songs.size(); i++) {
//                songNames[i] = songs.get(i).getName().replace(".mp3", "");
//            }
//            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),R.layout.fragment_all_songs,R.id.listView,songNames);
//            listView.setAdapter(adapter);
//            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                String songName = listView.getItemAtPosition(position).toString();
//                startActivity(new Intent(getContext(), NowPlaying.class)
//                        .putExtra("songs", songs).putExtra("songName", songName));
//            }
//        });
        return inflater.inflate(R.layout.fragment_all_songs, container, false);
    }
}
