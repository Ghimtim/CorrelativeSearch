package com.jnu.correlativesearch;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.jnu.correlativesearch.beans.RelatedRecord;
import com.jnu.correlativesearch.dao.RecordDao;
import com.jnu.correlativesearch.dao.RecordDaoImpl;


/**
 * Created by GoneAir on 2016/4/2.
 */
public class SmsObserver extends ContentObserver {

    private Context context;

    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    public SmsObserver(android.os.Handler handler,Context context) {
        super(handler);
        this.context = context;
    }

    @Override
    public void onChange(boolean selfChange) {
        Log.e("SMS Deliver","testtest");

        Cursor cursor = context.getContentResolver().query(Uri.parse(
                "content://sms/outbox"), null, null, null, null);

        Log.e("SMS Deliver","testtest123123");

        RelatedRecord record = new RelatedRecord();
        RecordDao rdi = new RecordDaoImpl(context);
        String addr = "";

        while (cursor.moveToNext()){
            addr = cursor.getString(cursor.getColumnIndex("address"));
            ContactGetter cg = new ContactGetter();
            record.setMime("application/sms");
            record.setName(cg.getContactName(context.getContentResolver(),addr) + "/" + addr);
            record.setPath(cursor.getString(cursor.getColumnIndex("body")));
            record.setStart_time(cursor.getLong(cursor.getColumnIndex("date")));
            record.setEnd_time(cursor.getLong(cursor.getColumnIndex("date")));

            Log.e("SMS Deliver",cursor.getString(cursor.getColumnIndex("address")) );
            Log.e("SMS Deliver",cursor.getString(cursor.getColumnIndex("body")));
            Log.e("SMS Deliver",cursor.getLong(cursor.getColumnIndex("date")) + "");

            rdi.insertRecord(record);

        }

        super.onChange(selfChange);
    }
}
