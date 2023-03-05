package com.example.mymemory2.Database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NotepadDB {
    public static final String DATABASE_NAME = "Notepad.db";

    public static final int DATABASE_VERSION = 1;

    //用户表建表语句
    private static final String CREATE_TABLE_USER = "create table if not exists "+ UserTB.USER_TABLE +" ("+
            UserTB.USER_ID +" integer PRIMARY KEY AUTOINCREMENT NOT NULL," +
            UserTB.USER_ACCOUNT + " VARCHAR NOT NULL," +
            UserTB.USER_NAME + " VARCHAR DEFAULT 'momo', " +
            UserTB.USER_PWD +" VARCHAR NOT NULL);";

    //记事本表建表语句
    private static final String CREATE_TABLE_NOTE = "create table if not exists " + NoteTB.NOTEBOOK_TABLE +"(" +
            NoteTB.NOTEBOOK_ID + " integer primary key autoincrement, " +
            NoteTB.NOTEBOOK_CONTENT + " text, " +
            NoteTB.NOTEBOOK_TIME +" text);";

    //记账本表建表语句
    private static final String CREATE_TABLE_TALLY = "create table if not exists " + TallyTB.TALLY_TABLE + "(" +
            TallyTB.TALLY_ID + " integer primary key autoincrement," +
            TallyTB.TALLY_DATE + " text," +
            TallyTB.TALLY_TYPE + " text," +
            TallyTB.TALLY_MONEY + " float," +
            TallyTB.TALLY_STATE + " text);";

    private Context context;
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDatabase;

    public NotepadDB(Context ctx)
    {
        this.context = ctx;
        this.mDBHelper = new DatabaseHelper(this.context);
    }

    public static class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) { super(context, DATABASE_NAME, null, DATABASE_VERSION); }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL(CREATE_TABLE_NOTE);//创建学生表
            db.execSQL(CREATE_TABLE_USER);//创建教师表
            db.execSQL(CREATE_TABLE_TALLY);//创建教师表
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {        }
    }

    public void open() throws SQLException {
        this.mDatabase = this.mDBHelper.getWritableDatabase();

    }

    public void close()
    {
        this.mDBHelper.close();
    }
}
