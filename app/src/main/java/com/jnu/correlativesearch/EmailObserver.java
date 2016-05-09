package com.jnu.correlativesearch;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.FileObserver;
import android.util.Log;

import com.jnu.correlativesearch.beans.RelatedRecord;
import com.jnu.correlativesearch.dao.RecordDao;
import com.jnu.correlativesearch.dao.RecordDaoImpl;

/**
 * Created by GoneAir on 2016/3/22.
 */
public class EmailObserver extends FileObserver {

    private String path = "/data/data/com.android.email/databases/EmailProvider.db";
    private Context context;

    public EmailObserver(String path,Context context){
        super(path);
        this.context = context;
    }
    @Override
    public void onEvent(int event, String path) {

        Log.e("EmailObserver","111111");

        SQLiteDatabase db = SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READONLY);
        RelatedRecord record = new RelatedRecord();
        RecordDao rdi = new RecordDaoImpl(context);

        Log.e("EmailObserver","2222222");

        switch(event){
            case FileObserver.MODIFY:
                Cursor cursor = db.query("Message",new String[] {"displayName,timeStamp,subject"},"1 = 1 order by timeStamp desc limit 1 --",null,null,null,null);
                while (cursor.moveToNext()){
                    String displayName = cursor.getString(cursor.getColumnIndex("displayName"));
                    String subject = cursor.getString(cursor.getColumnIndex("subject"));
                    long timeStamp = cursor.getLong(cursor.getColumnIndex("timeStamp"));
                    record.setName(displayName);
                    record.setPath(subject);
                    record.setStart_time(timeStamp);
                    record.setEnd_time(timeStamp);
                    record.setMime("application/email");
                }
                rdi.insertRecord(record);
                break;
            default:
                ;

        }

    }
}
