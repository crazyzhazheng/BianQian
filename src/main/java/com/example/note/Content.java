package com.example.note;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by my on 2016/10/11.
 */
public class Content extends Activity implements View.OnClickListener {
    private Button okButton,cancleButton,picButton;
    private EditText contentWrite;
    private DBHelper dbHelper;
    private Cursor cursor;
    private int _id=0;
    private Time time;

    private static final int IMAGE_CODE=1;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content);
        SimpleDateFormat   formatter=new SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss");
        Date curDate =  new Date(System.currentTimeMillis());
        String string=formatter.format(curDate);
        Log.e("0",string);
        init();
    }
    private void init(){
        dbHelper=new DBHelper(this);
        cursor=dbHelper.select();
        okButton = (Button) findViewById(R.id.btn_ok);
        cancleButton = (Button) findViewById(R.id.btn_cancle);
        picButton = (Button) findViewById(R.id.btn_pic);
        contentWrite = (EditText) findViewById(R.id.et_content);
        okButton.setOnClickListener(this);
        cancleButton.setOnClickListener(this);
        picButton.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                addData() ;
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.btn_cancle:
                Intent intent1 = new Intent(this,MainActivity.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.btn_pic:
                Intent getAlbum = new Intent(Intent.ACTION_PICK);
                getAlbum.setType("image/*");
                startActivityForResult(getAlbum,IMAGE_CODE);
                break;
        }
    }
    private void addData(){
        if (contentWrite.getText().toString().equals("")){
            Toast.makeText(Content.this,"内容不能为空",Toast.LENGTH_SHORT).show();
        }else{

            dbHelper.insert(contentWrite.getText().toString());
            cursor.requery();
            contentWrite.setText("");
            _id=0;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bm = null;
        // 外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口
        ContentResolver resolver = getContentResolver();
        if(requestCode == IMAGE_CODE){
            try{
                // 获得图片的uri
                Uri originalUri = data.getData();
                // 最后根据Uri获取图片路径
                String path = getFilePath(this,originalUri);
                insertImg(path);
                //Toast.makeText(AddFlagActivity.this,path,Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(Content.this,"图片插入失败",Toast.LENGTH_SHORT).show();
            }
        }
    }

    //region 插入图片
    private void insertImg(String path){
        String tagPath = "<img src=\""+path+"\"/>";//为图片路径加上<img>标签
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        if(bitmap != null){
            SpannableString ss = getBitmapMime(path,tagPath);
            insertPhotoToEditText(ss);
            contentWrite.append("\n");
        }
    }
    //endregion

    //region 将图片插入到EditText中
    private void insertPhotoToEditText(SpannableString ss){
        Editable et = contentWrite.getText();
        int start = contentWrite.getSelectionStart();
        et.insert(start,ss);
        contentWrite.setText(et);
        contentWrite.setSelection(start+ss.length());
        contentWrite.setFocusableInTouchMode(true);
        contentWrite.setFocusable(true);
    }
    //endregion

    private SpannableString getBitmapMime(String path,String tagPath) {
        SpannableString ss = new SpannableString(tagPath);//这里使用加了<img>标签的图片路径

        int width = ScreenUtils.getScreenWidth(Content.this);
        int height = ScreenUtils.getScreenHeight(Content.this);


        Bitmap bitmap = ImageUtils.getSmallBitmap(path,width,480);
        ImageSpan imageSpan = new ImageSpan(this, bitmap);
        ss.setSpan(imageSpan, 0, tagPath.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    public static String getFilePath(Context context, Uri uri) {
        //这里开始的第二部分，获取图片的路径：低版本的是没问题的，但是sdk>19会获取不到
        Cursor cursor = null;
        String path = "";
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            //好像是android多媒体数据库的封装接口，具体的看Android文档
            cursor = context.getContentResolver().query(uri, proj, null, null, null);
            //获得用户选择的图片的索引值
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            //将光标移至开头 ，这个很重要，不小心很容易引起越界
            cursor.moveToFirst();
            //最后根据索引值获取图片路径   结果类似：/mnt/sdcard/DCIM/Camera/IMG_20171124_013332.jpg
            path = cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return path;
    }
}
