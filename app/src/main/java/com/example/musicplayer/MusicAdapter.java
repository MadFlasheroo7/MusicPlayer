package com.example.musicplayer;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;

import static com.example.musicplayer.MainActivity.CHANNEL_1_ID;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<MusicFiles> mFiles;
    private NotificationManager notificationManager;

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
        final byte[] image = albumArt(mFiles.get(position).getPath());
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


        holder.menu_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                PopupMenu popupMenu = new PopupMenu(mContext,view);
                popupMenu.getMenuInflater().inflate(R.menu.adapter_more_popup,popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.delete:
//                                Toast.makeText(mContext,"Delete clicked",Toast.LENGTH_SHORT).show();
                                deleteFile(position,view);
                                break;
                        }
                        return true;
                    }
                });
            }
        });
    }

    private void deleteFile(final int position, View v)
    {
        Uri contentUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,Long.parseLong(mFiles.get(position).getId()));
        File file = new File(mFiles.get(position).getPath());
        boolean deleted = file.delete();
        if (deleted) {
            mContext.getContentResolver().delete(contentUri,null,null);
            mFiles.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mFiles.size());
            Snackbar.make(v, "File Deleted", Snackbar.LENGTH_LONG).show();
        }else {
            Snackbar.make(v, "File cannot be Deleted", Snackbar.LENGTH_LONG).show();
        }
    }
    @Override
    public int getItemCount() {
            return mFiles.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView file_Name;
        ImageView album_Art,menu_more;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            file_Name = itemView.findViewById(R.id.music_File_Name);
            album_Art = itemView.findViewById(R.id.music_art);
            menu_more = itemView.findViewById(R.id.more_menu);
        }
    }

    //this function fetches the cover art from song and displays it in all songs list
    private static byte[] albumArt(String uri)
    {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }
}
