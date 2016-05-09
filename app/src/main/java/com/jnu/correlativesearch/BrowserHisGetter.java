package com.jnu.correlativesearch;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jnu.correlativesearch.beans.RelatedRecord;
import com.jnu.correlativesearch.dao.RecordDao;
import com.jnu.correlativesearch.dao.RecordDaoImpl;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * Created by GoneAir on 2016/3/26.
 */
public class BrowserHisGetter{
    private static String path = "data/data/com.android.browser/databases/browser2.db";

    public BrowserHisGetter(){

    }

    public void getBrowserHis(long startTime , long endTime , Context context){
        getRoot();
        RecordDao rdi = new RecordDaoImpl(context);
        SQLiteDatabase db = SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = db.query("history",new String[] {"title","url","date"},"date between " + startTime + " and " + endTime + " --",null,null,null,null);
        while (cursor.moveToNext()){
            RelatedRecord record = new RelatedRecord();
            record.setMime("text/html");
            record.setName(cursor.getString(cursor.getColumnIndex("title")));
            long timeStamp = cursor.getLong(cursor.getColumnIndex("date"));
            record.setStart_time(timeStamp);
            record.setEnd_time(timeStamp);
            record.setPath(cursor.getString(cursor.getColumnIndex("url")));

            Log.e("BrowserHis", cursor.getString(cursor.getColumnIndex("title")));
            Log.e("BrowserHis",cursor.getString(cursor.getColumnIndex("url")));
            rdi.insertRecord(record);
        }

    }

    public void getRoot(){
        Process process = null;
        DataOutputStream os = null;
        DataInputStream is = null;
        //String command = "chmod 777 " + getPackageCodePath();
        try{
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            is = new DataInputStream(process.getInputStream());
            os.writeBytes("chmod -R 777 /data/data/com.android.browser/" + "\n");
            os.writeBytes("chmod -R 777 /data/data/com.android.browser/databases/" + "\n");
            os.writeBytes("chmod 777 /data/data/com.android.browser/databases/browser2.db" + "\n");

            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();

        }
        catch(Exception e){
            System.err.println(e);
        }
    }
}
