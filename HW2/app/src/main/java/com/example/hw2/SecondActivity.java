package com.example.hw2;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {
    private static final String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);
        Log.i(TAG, "ItemActivity onCreate");
        Bundle bundle = this.getIntent().getExtras();
        int num = bundle.getInt("1");
        initView(num);
    }

    private void initView(int num){
        TextView tv = findViewById(R.id.textView5);
        tv.setText("点击了" + num + "号");
    }
}
