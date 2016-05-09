package com.jnu.correlativesearch.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jnu.correlativesearch.database.MyDBHelper;
import com.jnu.correlativesearch.utils.RecordUtils;
import com.jnu.correlativesearch.beans.RelatedRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhuang on 2016/3/17.
 */
public class RecordDaoImpl implements RecordDao{

    private Context context;
    private MyDBHelper dbHelper;

   // private SQLiteDatabase db;

    @Override
    public RelatedRecord getRecordById(Integer id) {
        String sql = "id = ?";
        String table = MyDBHelper.TABLE;
        String sql1 = sql;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] args = new String[]{String.valueOf(id)};
        //db.query(true,MyDBHelper.TABLE,null,"where id = ?")
        Cursor cursor = db.query(true,MyDBHelper.TABLE,null,sql,args,null,null,null,null);

        RelatedRecord record = null;
        if(cursor != null){
            if(cursor.moveToNext()){
                record = RecordUtils.fromCursorToRecord(cursor);
            }else{
                record = new RelatedRecord();
            }
            cursor.close();
        }
        return record;
    }

    @Override
    public List<RelatedRecord> getRecordsByContent(String content) {
        String sql = "name like ?";
        String contentStr = "%" + content + "%";
        String[] args = new String[]{contentStr};
        List<RelatedRecord> records = new ArrayList<RelatedRecord>();
        RelatedRecord record;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(true,MyDBHelper.TABLE,null,sql,args,null,null,null,null);
        while(cursor.moveToNext()){
            record = RecordUtils.fromCursorToRecord(cursor);
            records.add(record);
        }
        cursor.close();
        return records;
    }

    @Override
    public long insertRecord(RelatedRecord record) {
        ContentValues contentValues = RecordUtils.fromRecordToContentValues(record);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long i = db.insert(MyDBHelper.TABLE,null,contentValues);
        return i;
    }

    @Override
    public List<RelatedRecord> getAllRecords() {

        List<RelatedRecord> records = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String table = MyDBHelper.TABLE;
        Cursor cursor = db.query(true, table, null, null, null, null, null, null, null);
        RelatedRecord record;
        if(cursor != null){
            while(cursor.moveToNext()){
                record = new RelatedRecord();
                record.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                record.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                record.setMime(cursor.getString(cursor.getColumnIndexOrThrow("mime")));
                record.setStart_time(cursor.getLong(cursor.getColumnIndexOrThrow("start_time")));
                record.setEnd_time(cursor.getLong(cursor.getColumnIndexOrThrow("end_time")));
                record.setPath(cursor.getString(cursor.getColumnIndexOrThrow("path")));
                records.add(record);
            }
        }
        return records;
    }

    @Override
    public List<RelatedRecord> queryRecords(String content , Long startTime, Long endTime , String  mime) {
        String sql = "name like '%" + content + "%' ";
        Long sTime = null;
        Long eTime = null;

        if(startTime != null && startTime.equals(endTime) && endTime != null){

            sTime = startTime - 900000; //起始时间与结束时间相同时前后扩大15分钟区间
            eTime = endTime + 900000;
        }else{
            sTime = startTime;
            eTime = endTime;
        }

        if(sTime != null && eTime != null){

            sql = sql + " and (start_time between " + String.valueOf(sTime) + " and " + String.valueOf(eTime) +
                    " or end_time between " + String.valueOf(sTime) + " and " + String.valueOf(eTime) +
                    " or ( start_time <= " + String.valueOf(sTime) + " and end_time >= " + String.valueOf(eTime) + " )) ";

        }else if(sTime != null){

            sql = sql + " and end_time >= " + String.valueOf(sTime) + " ";

        }else if(eTime != null){

            sql = sql + " and start_time <= " + String.valueOf(eTime) + " ";

        }

        if(mime != null && !mime.equals("") &&  !mime.equals("ALL")  && !mime.equals("null")){

            sql = sql + " and mime = '" + mime + "' ";
        }

        sql = sql + "--";


        List<RelatedRecord> records = new ArrayList<RelatedRecord>();
        RelatedRecord record;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(true,MyDBHelper.TABLE,null,sql,null,null,null,null,null);
        while(cursor.moveToNext()){

            record = RecordUtils.fromCursorToRecord(cursor);
            records.add(record);
        }
        cursor.close();

        RecordUtils.sortRecords(records);

        return records;
    }

    @Override
    public List<RelatedRecord> queryFirstTime(long time){
        long oneDayMillis = 60 * 60 * 1000 * 24;
        //String timeStamp = "";
        //timeStamp = String.valueOf(time - )
        String sql = " start_time >= " + String.valueOf(time - oneDayMillis) + " --";
        List<RelatedRecord> records = new ArrayList<RelatedRecord>();
        RelatedRecord record;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(true,MyDBHelper.TABLE,null,sql,null,null,null,null,null);
        while(cursor.moveToNext()){

            record = RecordUtils.fromCursorToRecord(cursor);
            records.add(record);
        }
        cursor.close();
        RecordUtils.sortRecords(records);
        return records;
    }


    public RecordDaoImpl(Context context) {
        this.context = context;
        dbHelper = MyDBHelper.getInstance(this.context);
        //db = dbHelper.getWritableDatabase();
    }
}
