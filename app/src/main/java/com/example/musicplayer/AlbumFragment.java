package com.example.musicplayer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static com.example.musicplayer.MainActivity.musicFiles;

public class AlbumFragment extends Fragment {

    RecyclerView recyclerView;
    Album_Adapter album_adapter;
    public AlbumFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewAlbum);
        recyclerView.setHasFixedSize(true);
        album_adapter = new Album_Adapter(getContext(), musicFiles);
        recyclerView.setAdapter(album_adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        album_adapter.notifyDataSetChanged();
        return view;
    }
}