package com.bytedance.todolist.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bytedance.todolist.R;
import com.bytedance.todolist.database.TodoListDao;
import com.bytedance.todolist.database.TodoListDatabase;
import com.bytedance.todolist.database.TodoListEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangrui.sh
 * @since Jul 11, 2020
 */

public class TodoListAdapter extends RecyclerView.Adapter<TodoListItemHolder> {
    public interface OnItemClickListenter{
        void onClick(View view, int position);
    };

    OnItemClickListenter listener;
    public void setOnitemClickListenner(OnItemClickListenter listener){
        this.listener = listener;
    }

    public void onClick(View view) {
        if(listener != null){
            listener.onClick(view, (Integer)view.getTag());
        }
    }

    private List<TodoListEntity> mDatas;

    public TodoListAdapter() {
        mDatas = new ArrayList<>();
    }
    @NonNull
    @Override
    public TodoListItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TodoListItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final TodoListItemHolder holder, final int position) {
        holder.bind(mDatas.get(position));
        holder.mbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(position);
                setData(mDatas);
            }
        });
        holder.mcbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.mcbox.isChecked()) {
                    holder.mContent.setPaintFlags(holder.mContent.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.mContent.setTextColor(Color.parseColor("#FF708090"));
                    /*String tmp = mDatas.get(position).getContent();
                    tmp += "\33";
                    mDatas.get(position).setContent(tmp);*/
                    //mDatas.get(position).set(mDatas.get(position).getId() * -1);
                    mDatas.get(position).setStatus(1);
                }
                else {
                    holder.mContent.setPaintFlags(holder.mContent.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    holder.mContent.setTextColor(Color.parseColor("#FF000000"));
                    /*String tmp = mDatas.get(position).getContent();
                    tmp = tmp.substring(tmp.length() - 1);
                    mDatas.get(position).setContent(tmp);*/
                    //mDatas.get(position).setId(mDatas.get(position).getId() * -1);
                    mDatas.get(position).setStatus(0);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @MainThread
    public void setData(List<TodoListEntity> list) {
        mDatas = list;
        notifyDataSetChanged();
    }

    public List<TodoListEntity> getData(){
        return mDatas;
    }

    public void remove(int position){
        mDatas.remove(position);
        //notifyDataSetChanged();// 提醒list刷新，没有动画效果
        notifyItemRemoved(position); // 提醒item删除指定数据，这里有RecyclerView的动画效果
    }
}
