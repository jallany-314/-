package com.bytedance.todolist.activity;

import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bytedance.todolist.R;
import com.bytedance.todolist.database.TodoListEntity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wangrui.sh
 * @since Jul 11, 2020
 */
public class TodoListItemHolder extends RecyclerView.ViewHolder {
    TextView mContent;
    TextView mTimestamp;
    Button mbtn;
    CheckBox mcbox;

    public TodoListItemHolder(@NonNull View itemView) {
        super(itemView);
        mContent = itemView.findViewById(R.id.tv_content);
        mTimestamp = itemView.findViewById(R.id.tv_timestamp);
        mbtn = itemView.findViewById(R.id.button2);
        mcbox = itemView.findViewById(R.id.checkBox2);
    }

    public void bind(TodoListEntity entity) {
        mContent.setText(entity.getContent());
        String tmp = entity.getContent();
        if(entity.getStatus() == 1){
            mContent.setPaintFlags(mContent.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            mContent.setTextColor(Color.parseColor("#FF708090"));
            mcbox.setChecked(true);
        }
        else{
            mContent.setPaintFlags(mContent.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            mContent.setTextColor(Color.parseColor("#FF000000"));
            mcbox.setChecked(false);
        }
        mTimestamp.setText(formatDate(entity.getTime()));
    }

    private String formatDate(Date date) {
        DateFormat format = SimpleDateFormat.getDateInstance();
        return format.format(date);
    }
}
