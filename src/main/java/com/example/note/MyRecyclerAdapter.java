package com.example.note;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder>  {
    private List<String> mDatas;
    private Context mContext;
    private LayoutInflater inflater;
    private Cursor mmcursor;
    private DBHelper mmDbhelper;
    int k=0;
    private OnItemClickListener mOnItemClickListener;
    public MyRecyclerAdapter(Context context,Cursor cursor,DBHelper dbHelper){
        this.mContext=context;
        this.mmcursor=cursor;
        this.mmDbhelper=dbHelper;
        inflater= LayoutInflater. from(mContext);
    }
    @Override
    public int getItemCount() {
        return mmcursor.getCount();
    }

    //填充onCreateViewHolder方法返回的holder中的控件
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        mmcursor.moveToPosition(mmcursor.getCount()-position-1);
        holder.tv_time.setText(mmcursor.getString(1));
        holder.tv_content.setText(mmcursor.getString(2));
        //实现接口
        if( mOnItemClickListener!= null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(position);
                }
            });
        }
    }

    //重写onCreateViewHolder方法，返回一个自定义的ViewHolder
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.list_item,parent, false);
        MyViewHolder holder= new MyViewHolder(view);
        return holder;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_time;
        TextView tv_content;
        public MyViewHolder(View view) {
            super(view);
            tv_time=(TextView)view.findViewById(R.id.time);
            tv_content=(TextView) view.findViewById(R.id.content);
        }
    }
    public interface OnItemClickListener{
        void onClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener ){
        this. mOnItemClickListener=onItemClickListener;
    }
}