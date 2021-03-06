package com.example.note;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by my on 2016/10/11.
 */
public class Modify extends Activity implements View.OnClickListener {
    private EditText et_show;
    private Button updateButton, deleteButton, backButton;
    private DBHelper dbHelper;
    private Cursor cursor;
    private int id;

    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.modify);
        init();
    }

    private void init() {
        et_show = (EditText) findViewById(R.id.et_modify);
        Intent intent = getIntent();
        String data = intent.getStringExtra("data");
        id = intent.getIntExtra("id", id);

        Html.ImageGetter imageGetter = new Html.ImageGetter(){
            @Override
            public Drawable getDrawable(String s) {
                int width = ScreenUtils.getScreenWidth(Modify.this);
                int height = ScreenUtils.getScreenHeight(Modify.this);
                width/=1.3;
                height/=3;
                Bitmap bitmap = ImageUtils.getSmallBitmap(s,width,480);
                Drawable drawable = new BitmapDrawable(bitmap);
                drawable.setBounds(0,0,width,height);
                return drawable;
            }
        };

        et_show.setText(Html.fromHtml(data, imageGetter,null));
        dbHelper = new DBHelper(this);
        cursor = dbHelper.select();
        updateButton = (Button) findViewById(R.id.btn_update);
        deleteButton = (Button) findViewById(R.id.btn_delete);
        backButton = (Button) findViewById(R.id.btn_back);
        updateButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
    }

    public void updateData() {
        if (id == 0) {
            Toast.makeText(Modify.this, "内容不能为空", Toast.LENGTH_SHORT).show();
        } else {
            dbHelper.update(id, et_show.getText().toString());
            cursor.requery();
            id = 0;
        }
    }
    public void deleteData(){
        if (id==0){
            Toast.makeText(Modify.this, "内容不能为空", Toast.LENGTH_SHORT).show();
        }else{
            dbHelper.delete(id);
            cursor.requery();
            id=0;
        }
    }
    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.btn_update:
                updateData();
                Intent intent1 = new Intent(this,MainActivity.class);
                startActivity(intent1);
                finish();
                break;

            case R.id.btn_delete:
                deleteData();
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_back:
                Intent intent2 = new Intent(this,MainActivity.class);
                startActivity(intent2);
                finish();
                break;
        }

    }
}
