package com.example.hw8;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.PathUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import static android.content.ContentValues.TAG;

public class CameraActivity extends Activity implements SurfaceHolder.Callback {
    private SurfaceView mSurfaceView;
    private SurfaceHolder mHolder;
    private boolean mIsDrawing = true;
    private Thread mThread;

    private ImageView mImageView;
    private VideoView mVideoView;
    private Button mButton1;

    private Camera mCamera;
    private MediaRecorder mMediaRecorder;
    private Camera.Parameters mParameters;
    private boolean isRecording = false;
    private static int mOptVideoWidth = 1920;
    private static int mOptVideoHeight = 1080;

    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        setTitle("拍摄界面");

        initView();
        //mImageView.findViewById(R.id.imageView);
        mButton1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                record();
            }
        });

        prepareVideoRecorder();
    }

    private void initView(){
        mSurfaceView = findViewById(R.id.surfaceView2);
        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(this);
        mVideoView = findViewById(R.id.videoView);

        initCamera();

        mButton1 = findViewById(R.id.button3);
    }

    private boolean prepareVideoRecorder() {
        mMediaRecorder = new MediaRecorder();

        mCamera.unlock();
        mMediaRecorder.setCamera(mCamera);

        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

        //mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
        //mMediaRecorder.setVideoSize(mOptVideoWidth, mOptVideoHeight);

        path = getPath();
        mMediaRecorder.setPreviewDisplay(mHolder.getSurface());

        if (path != null) {

            File dir = new File(path + "/VideoRecorderTest");
            if (!dir.exists()) {
                dir.mkdir();
            }
            else {
                dir.delete();
            }
            path = dir + "/" + getDate() + ".mp4";
            mMediaRecorder.setOutputFile(path);

            try {
                mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                mMediaRecorder.prepare();
            } catch (IOException e) {
                releaseMediaRecorder();
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCamera == null) {
            initCamera();
        }
        mCamera.startPreview();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCamera.stopPreview();
    }

    /*private void getCameraOptimalVideoSize() {
        try {
            Camera.Parameters parameters = mCamera.getParameters();
            List<Camera.Size> mSupportedPreviewSizes = parameters.getSupportedPreviewSizes();
            List<Camera.Size> mSupportedVideoSizes = parameters.getSupportedVideoSizes();
            Camera.Size optimalSize = CameraHelper.getOptimalVideoSize(mSupportedVideoSizes,
                    mSupportedPreviewSizes, getWidth(), getHeight());
            mOptVideoWidth = optimalSize.width;
            mOptVideoHeight = optimalSize.height;
            Log.d(TAG, "prepareVideoRecorder: optimalSize:" + mOptVideoWidth + ", " + mOptVideoHeight);
        } catch (Exception e) {
            Log.e(TAG, "getCameraOptimalVideoSize: ", e);
        }
    }*/

    private void initCamera(){
        mCamera = Camera.open();
        Camera.Parameters parameters = mCamera.getParameters();
        //parameters.setPictureFormat(ImageFormat.JPEG);
        //parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        //parameters.set("orientation", "potrait");
        parameters.set("rotation", 90);
        mCamera.setParameters(parameters);
        mCamera.setDisplayOrientation(90);
    }

    public void record() {
        if (isRecording) {
            mButton1.setText("录制");
            mMediaRecorder.stop();
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
            mCamera.lock();

            mVideoView.setVisibility(View.VISIBLE);
            mVideoView.setVideoPath(path);
            mVideoView.start();
        } else {
            if (prepareVideoRecorder()) {
                mButton1.setText("暂停");
                mMediaRecorder.start();
            }
        }
        isRecording = !isRecording;
    }

    private void releaseMediaRecorder() {
        if (mMediaRecorder != null) {
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
            mCamera.lock();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try{
            mCamera.setPreviewDisplay(surfaceHolder);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        if(surfaceHolder.getSurface() == null) {
            return;
        }

        mCamera.stopPreview();

        try {
            mCamera.setPreviewDisplay(surfaceHolder);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }

    public String getPath(){
        File sdDir;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);
        if (sdCardExist)
        {
            sdDir = Environment.getExternalStorageDirectory();
            return sdDir.toString();
        }
        return null;
    }

    public static String getDate(){
        Calendar ca = Calendar.getInstance();
        int year = ca.get(Calendar.YEAR);           // 获取年份
        int month = ca.get(Calendar.MONTH);         // 获取月份
        int day = ca.get(Calendar.DATE);            // 获取日
        int minute = ca.get(Calendar.MINUTE);       // 分
        int hour = ca.get(Calendar.HOUR);           // 小时
        int second = ca.get(Calendar.SECOND);       // 秒

        String date = "" + year + (month + 1 )+ day + hour + minute + second;
        Log.d(TAG, "date:" + date);

        return date;
    }
}
