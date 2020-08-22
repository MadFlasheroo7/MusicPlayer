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

public class Album_Adapter extends RecyclerView.Adapter<Album_Adapter.MyHolder> {
    private Context mContext;
    static ArrayList<MusicFiles> albumFlies;

    public Album_Adapter(Context mContext, ArrayList<MusicFiles> albumFlies) {
        this.mContext = mContext;
        this.albumFlies = albumFlies;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.album_layout,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        holder.albumName.setText(albumFlies.get(position).getAlbum());
        final byte[] image = albumArt(albumFlies.get(position).getPath());
        if (image != null)
        {
            Glide.with(mContext)
                    .asBitmap()
                    .load(image)
                    .into(holder.albumImage);
        }else {
            Glide.with(mContext)
                    .load(R.drawable.ic_album)
                    .into(holder.albumImage);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext,Album_details.class);
                intent.putExtra("albumName",albumFlies.get(position).getAlbum());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return albumFlies.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        ImageView albumImage;
        TextView albumName;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            albumImage = itemView.findViewById(R.id.album_img);
            albumName  = itemView.findViewById(R.id.album_name);
        }
    }
    private static byte[] albumArt(String uri)
    {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }
}
