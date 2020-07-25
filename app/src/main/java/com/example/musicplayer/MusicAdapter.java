package com.example.musicplayer;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<MusicFiles> mFiles;

    public MusicAdapter(Context mContext,ArrayList<MusicFiles> mFiles){

        this.mFiles = mFiles;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.song_tab, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.file_Name.setText(mFiles.get(position).getTitle());
        byte[] image = albumArt(mFiles.get(position).getPath());
        if (image != null)
        {
            Glide.with(mContext).asBitmap().load(image).into(holder.album_Art);
        }else {
            Glide.with(mContext).load(R.drawable.ic_album).into(holder.album_Art);
        }
        //if any song name is clicked it will play it in now playing activity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,NowPlaying.class);
                intent.putExtra("Position", position);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
            return mFiles.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView file_Name;
        ImageView album_Art;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            file_Name = itemView.findViewById(R.id.music_File_Name);
            album_Art = itemView.findViewById(R.id.music_art);
        }
    }

    //this function fetches the cover art from song and displays it in all songs list
    private byte[] albumArt(String uri)
    {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }
}
