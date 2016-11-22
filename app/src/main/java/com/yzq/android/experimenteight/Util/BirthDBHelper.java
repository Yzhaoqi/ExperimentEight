package com.yzq.android.experimenteight.Util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by YZQ on 2016/11/21.
 */

public class BirthDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "Birth.db";
    private static final String TABLE_NAME = "Birth_Info";
    private static final int version = 1;

    public BirthDBHelper(Context context) {
        super(context, DB_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE="CREATE TABLE if not exists "+TABLE_NAME+" (_id INTEGER PRIMARY KEY, name TEXT, birth TEXT, gift TEXT)";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insert(String name, String birth, String gift) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("birth", birth);
        cv.put("gift", gift);
        db.insert(TABLE_NAME, null, cv);
        db.close();
    }

    public void delete(String name) {
        SQLiteDatabase db = getReadableDatabase();
        String whereClause = "name=?";
        String[] whereArgs = {name};
        db.delete(TABLE_NAME, whereClause, whereArgs);
        db.close();
    }

    public void update(String name, String birth, String gift) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("birth", birth);
        cv.put("gift", gift);
        String whereClause = "name=?";
        String[] whereArgs = {name};
        db.update(TABLE_NAME, cv, whereClause, whereArgs);
        db.close();
    }

    public ArrayList<BirthItem> getAlldatabase() {
        ArrayList<BirthItem> birthItemArrayList = new ArrayList<BirthItem>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);
        while(c.moveToNext()) {
            String name = c.getString(c.getColumnIndex("name"));
            String birth = c.getString(c.getColumnIndex("birth"));
            String gift = c.getString(c.getColumnIndex("gift"));
            birthItemArrayList.add(new BirthItem(name, birth, gift));
        }
        db.close();
        return birthItemArrayList;
    }

    public boolean isFindName(String name) {
        SQLiteDatabase db = getReadableDatabase();
        String whereClause = "name=?";
        String[] whereArgs = {name};
        Cursor c = db.query(TABLE_NAME, null, whereClause, whereArgs, null, null, null);
        Boolean b = c.moveToFirst();
        db.close();
        return b;
    }
}
