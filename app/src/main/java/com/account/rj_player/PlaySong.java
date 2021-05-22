package com.account.rj_player;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class PlaySong extends AppCompatActivity {
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        updateSeek.interrupt();
    }

    private ImageView previous,next,play;
    private TextView songname;
    private  ArrayList<File>songslist;
    private  MediaPlayer mediaPlayer;
    private int position;
   private Thread updateSeek;
    private SeekBar time;
    private  boolean flag=false;
    private String songname_curr;
    private  int currentposition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);
        songname = findViewById(R.id.textView);
        play = findViewById(R.id.play);
        previous = findViewById(R.id.previous);
        next = findViewById(R.id.next);
        time = findViewById(R.id.seekBar);

        Intent intent = getIntent();
        //Bundle bundle=intent.getExtras();
        songslist = (ArrayList) intent.getParcelableArrayListExtra("songlist");
        position = intent.getIntExtra("position", 0);
         songname_curr= songslist.get(position).getName().replace(".mp3", "");
        Uri uri = Uri.parse(songslist.get(position).toString());
        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);

        time.setMax(mediaPlayer.getDuration());
        songname.setText(songname_curr);
         songname.setSelected(true);

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                try {
                    position=(position+1)%(songslist.size());
                    songname_curr = songslist.get(position).getName().replace(".mp3", "");
                    songname.setText(songname_curr);
                    mp.seekTo(0);
                    mp.stop();
                    Uri uri = Uri.parse(songslist.get(position).toString());
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);

                    time.setMax(mediaPlayer.getDuration());

                    //  currentposition=0;
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.start();

                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag)
                {  Uri uri = Uri.parse(songslist.get(position).toString());
                 mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                mediaPlayer.start();
                    time.setMax(mediaPlayer.getDuration());




                flag=false;
                    play.setImageResource(R.drawable.pause);
                }
            else {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                        play.setImageResource(R.drawable.play);
                    } else {
                        mediaPlayer.start();

                        play.setImageResource(R.drawable.pause);
                    }
                }
                }

        });
       next.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               position=(position+1)%(songslist.size());
               songname_curr = songslist.get(position).getName().replace(".mp3", "");
               songname.setText(songname_curr);
               mediaPlayer.seekTo(0);

               mediaPlayer.stop();
             //  currentposition=0;
               flag=true;
               play.setImageResource(R.drawable.play);

             //  mediaPlayer.release();
           }
       });

       previous.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
              if(position==0)
                  position=songslist.size()-1;
              else
              position=(position-1)%(songslist.size());
               songname_curr = songslist.get(position).getName().replace(".mp3", "");
               songname.setText(songname_curr);
               mediaPlayer.seekTo(0);
               mediaPlayer.stop();

               flag=true;
               play.setImageResource(R.drawable.play);

           }
       });
        time.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                       if(fromUser)
//                        mediaPlayer.seekTo(progress);


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
//        new Timer().scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                time.setProgress(mediaPlayer.getCurrentPosition());
//            }
//        }, 0, 1000);



         updateSeek = new Thread() {
            @Override
            public void run() {
                // currentposition = mediaPlayer.getCurrentPosition();
                try {
                    while (currentposition < mediaPlayer.getDuration()) {
                        currentposition = mediaPlayer.getCurrentPosition();
                        time.setProgress(currentposition);
                        sleep(1000);

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        };
         updateSeek.start();
    }

};