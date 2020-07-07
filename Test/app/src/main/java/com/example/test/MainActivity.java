package com.example.test;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = findViewById(R.id.button1);
        final TextView tv = findViewById(R.id.editText);
        final Switch sw = findViewById(R.id.switch1);
        final CheckBox cb = findViewById(R.id.checkBox);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sw.isChecked() && cb.isChecked())
                    tv.setText("You're right");
                else
                    tv.setText("Wrong answer");
            }
        });

        Log.i("111111", "onCreate: 1111");
    }
}
