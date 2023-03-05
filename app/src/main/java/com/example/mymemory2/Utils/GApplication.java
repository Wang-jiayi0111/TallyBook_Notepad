package com.example.mymemory2.Utils;

import android.app.Application;

import com.example.mymemory2.Database.NotepadDB;

public class GApplication extends Application {
    private NotepadDB noteHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        noteHelper = new NotepadDB(this);
        noteHelper.open();
    }

}
