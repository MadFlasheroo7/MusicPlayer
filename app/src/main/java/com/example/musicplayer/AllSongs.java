package com.example.musicplayer;


import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


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

public class AllSongs extends Fragment {
    ListView listView;
    String[] songNames;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    private ArrayList<File > readSongs(File root){
        ArrayList<File> arrayList = new ArrayList<>();
        File[] files = root.listFiles();
        assert files != null;
        for (File file: files){
            if (file.isDirectory()){
                arrayList.addAll(readSongs(file));
            }else{
                if (file.getName().endsWith(".mp3")){
                    arrayList.add(file);
                }
            }
        }
        return arrayList;
    }

    public void runTimePermission(){
        Dexter.withContext(getContext())
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {


                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

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
        container.findViewById(R.id.listView);

            final ArrayList<File> songs = readSongs(Environment.getExternalStorageDirectory());
            songNames = new String[songs.size()];
            for (int i = 0; i < songs.size(); i++) {
                songNames[i] = songs.get(i).getName().replace(".mp3", "");
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),R.layout.fragment_all_songs,R.id.listView,songNames);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String songName = listView.getItemAtPosition(position).toString();
                startActivity(new Intent(getContext(), NowPlaying.class)
                        .putExtra("songs", songs).putExtra("songName", songName));
            }
        });
        return inflater.inflate(R.layout.fragment_all_songs, container, false);
    }
}
