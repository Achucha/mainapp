package com.example.mainapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FavoriteSongDB extends AppCompatActivity {

        ListView lvSong;
        List<Song> lsData = new ArrayList<Song>();
        SongAdapter adapter;
        SongDB  songDb = new SongDB(FavoriteSongDB.this);

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_favorite_song_db);
            lvSong = findViewById(R.id.lvSong);

            songDb.initData();

            lsData = songDb.getAllSong();

            adapter = new SongAdapter((List<Song>) FavoriteSongDB.this, (Context) lsData);

            lvSong.setAdapter((ListAdapter) adapter);
        }

        @Override
        protected void onPostResume() {
            super.onPostResume();

            lsData.clear();
            lsData.addAll(songDb.getAllSong());
            adapter.notifyDataSetChanged();
            lvSong.invalidateViews();
            lvSong.refreshDrawableState();
        }

    }