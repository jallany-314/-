package com.domker.study.androidstudy;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.Locale;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Created by wanlipeng on 2019-07-05 16:43
 */
public class ImageActivity extends AppCompatActivity {
    private final static String IMAGE_URL = "https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1484819350&di=080d941034a884bac05802717922279c&src=http://i0.hdslb.com/bfs/archive/7d62f6c814418a75088e380b00c07729c4446a9c.jpg";
    private final static String IMAGE_URL2 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1562328963755&di=f4aa87b95c87dc01ff0ca2c9150845c8&imgtype=0&src=http%3A%2F%2Fwww.uimaker.com%2Fuploads%2Fallimg%2F121105%2F1_121105084854_2.jpg";
    private ImageView mImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_image);

        initButton();
    }

    private void initButton() {
        mImageView = findViewById(R.id.imageView);
        findViewById(R.id.buttonLoad).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImage();
            }
        });
    }

    private void loadImage() {
        RequestOptions cropOptions = new RequestOptions();
        cropOptions = cropOptions.centerCrop();
        Glide.with(ImageActivity.this)
                .load(IMAGE_URL)
                .apply(cropOptions)
                .placeholder(R.drawable.icon_progress_bar)
                .error(R.drawable.icon_failure)
                .fallback(R.drawable.ic_launcher_background)
                .thumbnail(Glide.with(this).load(IMAGE_URL2))
                .transition(withCrossFade(4000))
                .into(mImageView);
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            Glide.with(this).load(imagePath).into(mImageView);
        }
    }*/
}
