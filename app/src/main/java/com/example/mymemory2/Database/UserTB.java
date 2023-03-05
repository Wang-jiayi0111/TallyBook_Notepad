package com.example.mymemory2.Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.mymemory2.Bean.User;
import com.example.mymemory2.Database.NotepadDB.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class UserTB {
    public static final String USER_ID = "_id";             //主键
    public static final String USER_ACCOUNT = "account";    //账号
    public static final String USER_PWD = "password";       //密码
    public static final String USER_NAME = "username";      //用户名
    public static final String USER_TABLE = "user_info";    //表名

    private Context context;
    private DatabaseHelper mHelper;
    private SQLiteDatabase mWdb;
    private SQLiteDatabase mRdb;

    public UserTB(Context ctx){
        this.context = ctx;
    }

    public UserTB open() throws SQLException{
        mHelper = new DatabaseHelper(context);
        mWdb = mHelper.getWritableDatabase();
        mRdb = mHelper.getReadableDatabase();
        return this;
    }

    public void close(){
        if(mHelper != null){
            mHelper.close();
        }
    }

    public long delete(User user){
        return mWdb.delete(USER_TABLE, "account=?", new String[]{user.account});
    }

    //插入，即注册
    public long insert(User user){
        //SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("account",user.getAccount());
        values.put("password",user.getPassword());
        return mWdb.insert(USER_TABLE,null,values);
    }

    //通过账号密码查询，即登录
    @SuppressLint("Range")
    public List<User> selectByAccountAndPass(String account, String password) {
        //SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = mRdb.query(USER_TABLE, null, "account=? and password=?", new String[]{account, password}, null, null, null);
        List<User> userList = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String account1 = cursor.getString(cursor.getColumnIndex("account"));
                String password1 = cursor.getString(cursor.getColumnIndex("password"));
                User user = new User();
                user.setAccount(account1);
                user.setPassword(password1);
                userList.add(user);
            }
            return userList;
        }
        cursor.close();
        return null;
    }

    //通过账号查询，即账号是否存在
    @SuppressLint("Range")
    public List<User> selectByAccount(String account) {
        //SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = mRdb.query(USER_TABLE, null, "account=?", new String[]{account}, null, null, null);
        List<User> userList = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String account1 = cursor.getString(cursor.getColumnIndex("account"));
                User user = new User();
                user.setAccount(account1);
                userList.add(user);
            }
            return userList;
        }
        return null;
    }
}
