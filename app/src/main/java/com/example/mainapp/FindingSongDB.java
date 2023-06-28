package com.example.mainapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FindingSongDB extends SQLiteOpenHelper {
    public static final String DB_NAME="findsong.db";
    public static final int DB_VERSION=1;
    public static final String TB_NAME="tbl_song";
    public static final String ID="id";
    public static final String TEN="ten";
    public static final String AVA="tmg";
    Context context;
    public FindingSongDB(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql ="CREATE TABLE " + TB_NAME + "( " + ID+" INTEGER PRIMARY KEY, " + TEN +" TEXT, " + AVA +" INTEGER  )";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void initData(){
        int count = getDbCount();
        if(count==0){
            Song song1 = new Song("Cause I Love You", R.raw.cause_i_love_you_mp3, R.drawable.image_1 );
            Song song2 = new Song("Anh Cứ Đi Đi", R.raw.anh_cu_di_di_mp3, R.drawable.image_2);
            Song song3 = new Song("Điều Anh Biết", R.raw.dieu_anh_biet_mp3, R.drawable.image_3);
            Song song4 = new Song("Em Đã Biết", R.raw.em_da_biet_mp3, R.drawable.image_4);
            Song song5 = new Song("Mình Là Gì Của Nhau", R.raw.minh_la_gi_cua_nhau_mp3, R.drawable.image_5);
            Song song6 = new Song("Nếu Em Còn Tồn Tại", R.raw.neu_em_con_ton_tai_mp3, R.drawable.image_6);
            if(insSong(song1)==-1||insSong(song2)==-1||insSong(song3)==-1||insSong(song4)==-1||insSong(song5)==-1||insSong(song6)==-1)
                Toast.makeText(context,"Insert failed! ",
                        Toast.LENGTH_LONG).show();
        }
    }

    public long insSong(Song song){
        ContentValues values = new ContentValues();
        values.put(TEN,song.getTitle());
        values.put(AVA, song.getImg());
        return this.getWritableDatabase().insert(TB_NAME,null,values);
    }

    public List<Song> getAllSong(){
        List<Song> kq = new ArrayList<>();
        Cursor cursor = this.getReadableDatabase().
                rawQuery("SELECT * FROM " + TB_NAME, null);
        if(cursor.getCount()>0){
            if(cursor.moveToFirst()){
                do{
                    Song song = new Song();
                    song.setFile(cursor.getInt(0));
                    song.setTitle(cursor.getString(1));
                    song.setImg(cursor.getInt(2));
                    kq.add(song);
                }while(cursor.moveToNext());

            }
        }
        cursor.close();
        return kq;
    }

    public int getDbCount(){
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM "+ TB_NAME,null);
        return cursor.getCount();
    }
}
