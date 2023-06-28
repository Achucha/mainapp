package com.example.mainapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class phatNhac extends AppCompatActivity {

    TextView txtTitle, txtTimeSong, txtTimeTotal;
    SeekBar skSong;
    ImageButton btnPrev, btnPlay, btnNext, btnShuff, btnLoop;
    ImageView img;
    List<Song> arrSong;
    MediaPlayer mediaPlayer;
    int position = 0;
    Animation animation;
    boolean checkRandom = false;
    boolean next = true;
    SongDB  songDb = new SongDB(phatNhac.this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phat_nhac);

        anhXa();
        addSong();

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("dulieu");
        String nameSong = bundle.getString("ten");
        for (int i = 0; i < arrSong.size(); i++) {
            if (arrSong.get(i).getTitle().equalsIgnoreCase(nameSong)) {
                position = i;
                break;
            }
        }
        khoiTaoBaiHat();
        setTxtTimeTotal();
        animation = AnimationUtils.loadAnimation(this, R.anim.dia_xoay);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    btnPlay.setImageResource(R.drawable.play);
                }
                else{
                    mediaPlayer.start();
                    btnPlay.setImageResource(R.drawable.pause);
                }
                updateTimeSong();
                img.startAnimation(animation);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying())
                    mediaPlayer.stop();
                if (checkRandom == false)
                    position++;
                else
                    position = (int)(Math.random() * (arrSong.size()));
                if(position >= arrSong.size())
                    position = 0;
                khoiTaoBaiHat();
                btnPlay.setImageResource(R.drawable.pause);
                setTxtTimeTotal();
                updateTimeSong();
                img.startAnimation(animation);
                mediaPlayer.start();
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying())
                    mediaPlayer.stop();
                if ( checkRandom == false)
                    position--;
                else
                    position = (int)(Math.random() * (arrSong.size()));
                if(position < 0)
                    position = arrSong.size()-1;
                khoiTaoBaiHat();
                setTxtTimeTotal();
                updateTimeSong();
                img.startAnimation(animation);
                mediaPlayer.start();
            }
        });

        btnShuff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkRandom == true) {
                    checkRandom = false;
                    btnShuff.setImageResource(R.drawable.noshuffle);
                }
                else {
                    checkRandom = true;
                    btnShuff.setImageResource(R.drawable.shuffle);
                }
            }
        });

        btnLoop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (next == true){
                    next = false;
                    btnLoop.setImageResource(R.drawable.repeatonce);
                }
                else{
                    next = true;
                    btnLoop.setImageResource(R.drawable.repeat);
                }
            }
        });

        skSong.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(skSong.getProgress());
            }
        });
    }

    private void addSong(){
        arrSong = new ArrayList<>();
        SongDB db = new SongDB(phatNhac.this);
        db.initData();
        arrSong = db.getAllSong();
    }

    private void anhXa(){
        txtTitle = (TextView) findViewById(R.id.textviewTitle);
        txtTimeSong = (TextView) findViewById(R.id.textviewTimeSong);
        txtTimeTotal = (TextView) findViewById(R.id.textviewTimeTotal);
        skSong = (SeekBar) findViewById(R.id.seekBar);
        btnPrev = (ImageButton) findViewById(R.id.ibPre);
        btnPlay = (ImageButton) findViewById(R.id.ibPlay);
        btnNext = (ImageButton) findViewById(R.id.ibNext);
        img = (ImageView) findViewById(R.id.imageViewDVD);
        btnShuff = (ImageButton) findViewById(R.id.ibShuff);
        btnLoop = (ImageButton) findViewById(R.id.ibLoop);
    }

    private void khoiTaoBaiHat(){
        mediaPlayer = MediaPlayer.create(phatNhac.this, arrSong.get(position).getFile());
        txtTitle.setText(arrSong.get(position).getTitle());
    }

    private void setTxtTimeTotal(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        txtTimeTotal.setText(simpleDateFormat.format(mediaPlayer.getDuration()));
        skSong.setMax(mediaPlayer.getDuration());
    }

    private void updateTimeSong(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
                txtTimeSong.setText(simpleDateFormat.format(mediaPlayer.getCurrentPosition()));
                skSong.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(this, 500);
                if (mediaPlayer.getCurrentPosition() >= mediaPlayer.getDuration() ){
                    if (checkRandom == false && next == true)
                        position++;
                    else if (checkRandom == true && next == false)
                        position = (int)(Math.random() * (arrSong.size()));
                    if (position >= arrSong.size())
                        position = 0;
                    khoiTaoBaiHat();
                    setTxtTimeTotal();
                    mediaPlayer.start();
                }
            }
        }, 100);

    }

    public void onInsSongClick(View view) {
        Intent insIntent = new Intent(this, SongDB.class);
        insIntent.putExtra("ins_upd", "insert");
        insSong.launch(insIntent);
    }
    ActivityResultLauncher<Intent> insSong= registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    //insert du lieu
                    if(result.getResultCode()==RESULT_OK){
                        Intent data = result.getData();
                        if(data.getStringExtra("action").equals("insert")){
                            //insert du lieu vao database
                            Song song =(Song) data.getExtras().getSerializable("ins");
                            songDb.insSong(song);
                        }
                    }
                }
            });
}



