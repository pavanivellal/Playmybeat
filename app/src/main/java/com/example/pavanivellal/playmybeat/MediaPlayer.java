package com.example.pavanivellal.playmybeat;

import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import android.os.Handler;

public class MediaPlayer extends AppCompatActivity {

    static android.media.MediaPlayer mPlayer;
    ImageButton buttonPlay, buttonStop, buttonPause, buttonNext, buttonPrev, buttonMute;
    TextView song_name, bpm, status;
    ImageView song_img;
    SeekBar seekBar;
    String s_song_name, img_url, song_url, song_bpm;
    Boolean sound_stat = true;

    private double startTime = 0;
    private double finalTime = 0;
    private Handler myHandler = new Handler();;
    private int forwardTime = 5000;
    private int backwardTime = 5000;
    public static int oneTimeOnly = 0;
    int current_pos = 0;
    int dur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);


        buttonPlay = (ImageButton) findViewById(R.id.play);
        buttonStop = (ImageButton) findViewById(R.id.stop);
        buttonPause = (ImageButton) findViewById(R.id.pause);
        buttonNext = (ImageButton) findViewById(R.id.next);
        buttonPrev = (ImageButton) findViewById(R.id.previous);
        buttonMute = (ImageButton) findViewById(R.id.mute);
        song_name = (TextView) findViewById(R.id.song_name);
        status = (TextView) findViewById(R.id.status);
        bpm = (TextView) findViewById(R.id.song_bpm);
        song_img = (ImageView) findViewById(R.id.song_image);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setClickable(false);


        Intent intent = new Intent(getIntent());
        HashMap<String, String> sel_song = (HashMap<String, String>) intent.getSerializableExtra("sel_song");

        s_song_name = "Song Name : " + sel_song.get("song_name");
        song_url = sel_song.get("mp3_link");
        img_url = sel_song.get("image_link");
        song_bpm = "Tempo : " + sel_song.get("bpm") + "BPM";


        song_name.setText(s_song_name);
        bpm.setText(song_bpm);
        try {

            URL myFileUrl = new URL (img_url);
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();

            InputStream is = conn.getInputStream();
            song_img.setImageBitmap(BitmapFactory.decodeStream(is));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        init_mPlayer(song_url);


        //Set Seekbar
        dur = mPlayer.getDuration();
        seekBar.setMax(dur);

    }


    public void onStop(View v)
    {
        if(mPlayer!=null && mPlayer.isPlaying()){

            mPlayer.pause();
            current_pos = 0;
            seekBar.setProgress((int)current_pos);
            buttonStop.setEnabled(false);
            buttonPlay.setEnabled(true);
            buttonPause.setEnabled(false);
            buttonNext.setEnabled(false);
            buttonPrev.setEnabled(false);
            status.setText("Stop");

        }

    }

    public void onPlay(View v)
    {

        if(mPlayer==null)
        {
            init_mPlayer(song_url);
        }

        mPlayer.seekTo(current_pos);
        mPlayer.start();
        current_pos = mPlayer.getCurrentPosition();
        seekBar.setProgress((int)current_pos);

        buttonPause.setEnabled(true);
        buttonPlay.setEnabled(false);
        buttonStop.setEnabled(true);
        buttonNext.setEnabled(true);
        buttonPrev.setEnabled(true);
        status.setText("Play");

    }

    public void onPause(View v)
    {

        if(mPlayer!=null && mPlayer.isPlaying()){


            mPlayer.pause();
            current_pos = mPlayer.getCurrentPosition();
            buttonPause.setEnabled(false);
            buttonPlay.setEnabled(true);
            buttonStop.setEnabled(false);
            buttonNext.setEnabled(false);
            buttonPrev.setEnabled(false);
            status.setText("Pause");

        }

    }

    public void onNext(View v)
    {
        if(mPlayer!=null) {
            current_pos = current_pos + 5000;
            mPlayer.seekTo(current_pos);
            mPlayer.start();
            current_pos = mPlayer.getCurrentPosition();
            seekBar.setProgress((int) current_pos);
        }

    }

    public void onPrevious(View v)
    {
        if(mPlayer!=null) {
            current_pos = current_pos - 5000;
            mPlayer.seekTo(current_pos);
            mPlayer.start();
            current_pos = mPlayer.getCurrentPosition();
            seekBar.setProgress((int)current_pos);
        }
    }

    public void onMute(View v)
    {
        if(mPlayer!=null) {
            if (sound_stat == true) {
                mPlayer.setVolume(0, 0);
                buttonMute.setImageResource(R.drawable.mute);
                sound_stat = false;

            } else {
                mPlayer.setVolume(0.4f, 0.4f);
                buttonMute.setImageResource(R.drawable.sound);
                sound_stat = true;
            }
        }


    }

    public void init_mPlayer(String song_url)
    {
        //Declare Media player
        mPlayer = new android.media.MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mPlayer.setDataSource(song_url);
        } catch (IllegalArgumentException e) {
            Toast.makeText(getApplicationContext(), "Illegal Argument Exception", Toast.LENGTH_LONG).show();
        } catch (SecurityException e) {
            Toast.makeText(getApplicationContext(), "Security Exception", Toast.LENGTH_LONG).show();
        } catch (IllegalStateException e) {
            Toast.makeText(getApplicationContext(), "Illegal State Exception", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mPlayer.prepare();
        } catch (IllegalStateException e) {
            Toast.makeText(getApplicationContext(), "Illegal State Exception", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Incorrect URL", Toast.LENGTH_LONG).show();
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        // TODO Auto-generated method stub
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

}
