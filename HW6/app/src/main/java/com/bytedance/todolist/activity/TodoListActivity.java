package com.bytedance.todolist.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.bytedance.todolist.database.TodoListDao;
import com.bytedance.todolist.database.TodoListDatabase;
import com.bytedance.todolist.database.TodoListEntity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bytedance.todolist.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.Date;
import java.util.List;

public class TodoListActivity extends AppCompatActivity {

    private TodoListAdapter mAdapter;
    private FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_list_activity_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.rv_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new TodoListAdapter();
        loadFromDatabase();
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnitemClickListenner(new TodoListAdapter.OnItemClickListenter() {
            @Override
            public void onClick(View view, int position) {
                Toast.makeText(TodoListActivity.this,"点击了" + position, Toast.LENGTH_SHORT).show();
                saveToDatabase();
            }
        });

        mFab = findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO
                Intent intent = new Intent(TodoListActivity.this, TodoCreateActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        mFab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        TodoListDao dao = TodoListDatabase.inst(TodoListActivity.this).todoListDao();
                        dao.deleteAll();
                        for (int i = 0; i < 20; ++i) {
                            dao.addTodo(new TodoListEntity("This is " + i + " item", new Date(System.currentTimeMillis())));
                            loadFromDatabase();
                        }
                        saveToDatabase();
                        Snackbar.make(mFab, R.string.hint_insert_complete, Snackbar.LENGTH_SHORT).show();
                    }
                }.start();
                return true;
            }
        });
        //loadFromDatabase();
    }

    @Override
    protected void onDestroy(){
        saveToDatabase();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    final String text = data.getExtras().getString("text");
                    new Thread(){
                        @Override
                        public void run(){
                            TodoListDao dao = TodoListDatabase.inst(TodoListActivity.this).todoListDao();
                            dao.addTodo(new TodoListEntity(text, new Date(System.currentTimeMillis())));
                            loadFromDatabase();
                        }
                    }.start();
                }
                break;
            default:
        }
    }

    private void loadFromDatabase() {
        new Thread() {
            @Override
            public void run() {
                TodoListDao dao = TodoListDatabase.inst(TodoListActivity.this).todoListDao();
                final List<TodoListEntity> entityList = dao.loadAll();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.setData(entityList);
                    }
                });
            }
        }.start();
    }

    protected void saveToDatabase(){
        new Thread() {
            @Override
            public void run() {
                TodoListDao dao = TodoListDatabase.inst(TodoListActivity.this).todoListDao();
                dao.deleteAll();
                List<TodoListEntity> list = mAdapter.getData();
                for(int i = 0; i < list.size(); i++){
                    dao.addTodo(list.get(i));
                }
            }
        }.start();
    }
}
