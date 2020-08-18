package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class About_activity extends AppCompatActivity {

    ImageButton telegram,instagram,twitter,reddit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_ui);
        telegram = findViewById(R.id.TG);
        instagram = findViewById(R.id.insta);
        twitter = findViewById(R.id.tweet);
        reddit = findViewById(R.id.reddit);

        telegram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("https://t.me/mad_flasher_oo7"));
                startActivity(intent);
            }
        });

        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.instagram.com/iam_the_iron_man/"));
                startActivity(intent);
            }
        });

        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("https://twitter.com/jayesh_seth_"));
                startActivity(intent);
            }
        });

        reddit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.reddit.com/user/Mad_flasher"));
                startActivity(intent);
            }
        });
    }
}