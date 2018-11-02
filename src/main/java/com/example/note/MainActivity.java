package com.example.note;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private Cursor cursor;
    private DBHelper dbHelper;
    private RecyclerView recyclerView;
    private MyRecyclerAdapter adapter;
    private int _id=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper=new DBHelper(this);
        cursor=dbHelper.select();
        recyclerView=(RecyclerView)findViewById(R.id.recycle);
        adapter=new MyRecyclerAdapter(this,cursor,dbHelper);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2);
        StaggeredGridLayoutManager staggeredGridLayoutManager= new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        //设置布局管理器
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置Adapter
        recyclerView.setAdapter(adapter);
        Log.e("0","getcount:"+adapter.getItemCount());
        //设置增加或删除条目的动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter.setOnItemClickListener(new MyRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                cursor.moveToPosition(cursor.getCount()-1-position);
                _id=cursor.getInt(0);
                Intent intent=new Intent(MainActivity.this,Modify.class);
                intent.putExtra("id",_id);
                intent.putExtra("data",cursor.getString(2));//getString(1)显示cursor该列的内容
                Log.e("0","MainActivity to modify_contetn:"+cursor.getString(2));
                Log.e("0","id:"+_id);
                startActivity(intent);
                finish();
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Content.class);
                startActivity(intent);
            }
        });
    }
}
