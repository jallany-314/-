package com.domker.study.androidstudy;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.MediaController;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 使用系统VideoView播放 resource 视频
 */
public class VideoActivity extends AppCompatActivity {
    private final Uri VIDEO_URI = Uri.parse("http://qthttp.apple.com.edgesuite.net/1010qwoeiuryfg/sl.m3u8");
    private Button buttonPlay;
    private Button buttonPause;
    private VideoView videoView;
    private SeekBar seekBar;
    private TextView textView;

    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_video);
        setTitle("VideoView");

        buttonPause = findViewById(R.id.buttonPause);
        buttonPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.pause();
            }
        });

        buttonPlay = findViewById(R.id.buttonPlay);
        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.postDelayed(runnable, 0);
                videoView.start();
                seekBar.setMax(videoView.getDuration());
            }
        });

        videoView = findViewById(R.id.videoView);
        //videoView.setVideoPath(getVideoPath());
        videoView.setVideoURI(VIDEO_URI);

        seekBar = findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(!videoView.isPlaying()) {
                    videoView.seekTo(progress);
                    textView.setText(time(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        textView = findViewById(R.id.textView2);
        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                if (videoView.isPlaying()) {
                    int current = videoView.getCurrentPosition();
                    seekBar.setProgress(current);
                    textView.setText(time(videoView.getCurrentPosition()));
                }
                handler.postDelayed(runnable, 500);
            }
        };
    }

    protected String time(long millionSeconds) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millionSeconds);
        return simpleDateFormat.format(c.getTime());
    }
}
