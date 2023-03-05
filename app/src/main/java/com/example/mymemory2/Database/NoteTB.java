package com.example.mymemory2.Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import com.example.mymemory2.Bean.NotebookBean;
import java.util.List;

public class NoteTB {
    public static final String NOTEBOOK_ID = "_id";             //主键
    public static final String NOTEBOOK_CONTENT = "content";    //记事本内容
    public static final String NOTEBOOK_TIME = "notetime";      //记事本创建时间
    static final String NOTEBOOK_TABLE = "note";                //表名

    private Context context;
    private NotepadDB.DatabaseHelper mHelper;
    private SQLiteDatabase mWdb;
    private SQLiteDatabase mRdb;

    public NoteTB(Context ctx){
        this.context = ctx;
    }

    public NoteTB open() throws SQLException {
        mHelper = new NotepadDB.DatabaseHelper(context);
        mWdb = mHelper.getWritableDatabase();
        mRdb = mHelper.getReadableDatabase();
        return this;
    }

    public void close(){
        if(mHelper != null){
            mHelper.close();
        }
    }

    //添加数据
    public boolean insertData(String userContent, String userTime){
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTEBOOK_CONTENT, userContent);
        contentValues.put(NOTEBOOK_TIME, userTime);
        return mWdb.insert(NOTEBOOK_TABLE, null, contentValues) > 0;
    }
    //删除数据
    public boolean deleteData(String id){
        String sql = NOTEBOOK_ID + "=?";
        String[] contentValuesArray = new String[]{String.valueOf(id)};
        return
                mWdb.delete(NOTEBOOK_TABLE, sql, contentValuesArray) > 0;
    }
    //修改数据
    public boolean updateData(String id, String content, String userYear){
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTEBOOK_CONTENT, content);
        contentValues.put(NOTEBOOK_TIME, userYear);
        String sql = NOTEBOOK_ID + "=?";
        String[] strings = new String[]{id};
        return
                mWdb.update(NOTEBOOK_TABLE, contentValues, sql, strings) > 0;
    }
    //查询数据
    public List<NotebookBean> query(){
        List<NotebookBean> list = new ArrayList<NotebookBean>();
        Cursor cursor = mRdb.query(NOTEBOOK_TABLE, null, null, null,
                null, null, NOTEBOOK_ID + " desc");
        if(cursor != null){
            while (cursor.moveToNext()){
                NotebookBean noteInfo = new NotebookBean();
                @SuppressLint("Range")
                String id = String.valueOf(cursor.getInt(cursor.getColumnIndex(NOTEBOOK_ID)));
                @SuppressLint("Range")
                String content = cursor.getString(cursor.getColumnIndex(NOTEBOOK_CONTENT));
                @SuppressLint("Range")
                String time = cursor.getString(cursor.getColumnIndex(NOTEBOOK_TIME));
                noteInfo.setId(id);
                noteInfo.setNotebookContent(content);
                noteInfo.setNotebookTime(time);
                list.add(noteInfo);
            }
            cursor.close();
        }
        return list;
    }
}
