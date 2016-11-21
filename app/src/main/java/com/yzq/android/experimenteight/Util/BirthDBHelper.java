package com.yzq.android.experimenteight.Util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by YZQ on 2016/11/21.
 */

public class BirthDBHelper extends SQLiteOpenHelper {
    private static final String TABLE_NAME = "Birth_Info";

    public BirthDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE="CREATE TABLE idf not exists"+TABLE_NAME+"(_id INTEGER PRIMARY KEY, name TEXT, birth TEXT, gift TEXT)";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
