package com.lifeistech.android.audioplayer;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.media.MediaPlayer;
import android.widget.TextView;
import java.io.IOException;
import java.util.IllegalFormatCodePointException;
import android.view.View;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;
import android.widget.SeekBar;

public class MainActivity extends ActionBarActivity {

    MediaPlayer mp;
    TextView title;

    Timer timer;
    Handler handler = new Handler();

    SeekBar seekBar;
    TextView currentTimeText, wholeTimeText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mp = MediaPlayer.create(this, R.raw.xxx);

        try {
            mp.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        title = (TextView) findViewById(R.id.title);
        title.setText("かえるのうた");

        seekBar = (SeekBar)findViewById(R.id.seekBar);
        currentTimeText = (TextView)findViewById(R.id.current_time);
        wholeTimeText = (TextView)findViewById(R.id.whole_time);

        int duration = mp.getDuration();

        seekBar.setMax(duration);

        duration = duration / 1000;
        int minutes = duration / 60;
        int seconds = duration % 60;

        String m = String.format(Locale.JAPAN, "%02d",minutes);
        String s = String.format(Locale.JAPAN, "%02d",seconds);

        wholeTimeText.setText(m + ";" + s);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TODO SeekBarが動いた時の処理
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO SeekBarをつまんだ時の処理
                mp.pause();

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO SeekBarを離した時の処理
                int progress = seekBar.getProgress();
                mp.seekTo(progress);
                mp.start();

            }
        });
        }


    // 再生ボタンを押した時
    public void start(View v) {
        mp.start();

        if(timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    int duration = mp.getCurrentPosition() / 1000;

                    int minutes = duration / 60;
                    int seconds = duration % 60;

                    final String m = String.format(Locale.JAPAN, "%02d", minutes);
                    final String s = String.format(Locale.JAPAN, "%02d", seconds);

                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            currentTimeText.setText(m + ";" + s);
                            seekBar.setProgress(mp.getCurrentPosition());
                        }
                    });
                }
            }, 0, 1000);
        }

    }

    // 一時停止ボタンを押した時
    public void pause(View v) {
        mp.pause();
        if(timer != null){
            timer.cancel();
            timer = null;
        }
    }

    public void stop(View v) {
        mp.stop();
        if(timer != null){
            timer.cancel();
            timer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}