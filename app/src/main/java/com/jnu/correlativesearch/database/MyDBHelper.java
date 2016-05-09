package com.jnu.correlativesearch.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zhuang on 2016/3/16.
 */
public class MyDBHelper extends SQLiteOpenHelper{

    public static final String DBNAME = "RelatedDB";
    private static final int VERSION = 1;
    public static final String TABLE = "related_search";
    private SQLiteDatabase db;
    private static MyDBHelper dbHelper;
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table related_search(" +
                "id integer primary key autoincrement," +
                "name varchar(100) not null default '0'," +
                "mime varchar(50) ," +
                "start_time integer not null default 0," +
                "end_time integer not null default 0," +
                "path varchar(500) default '0')";
        db.execSQL(sql);
    }

    public static MyDBHelper getInstance(Context context){
        if(dbHelper == null){
            synchronized (MyDBHelper.class){
                if(dbHelper == null){
                    dbHelper = new MyDBHelper(context);
                }
            }
        }
        return dbHelper;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "drop table if exists RelatedDB";
        db.execSQL(sql);
    }

    private MyDBHelper(Context context) {
        super(context, DBNAME, null, VERSION);
    }



}
