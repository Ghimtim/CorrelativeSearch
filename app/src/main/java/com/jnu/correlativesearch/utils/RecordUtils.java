package com.jnu.correlativesearch.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.jnu.correlativesearch.beans.RelatedRecord;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhuang on 2016/3/17.
 */
public class RecordUtils {


    public static RelatedRecord fromCursorToRecord(Cursor cursor){
        Integer id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        String mime = cursor.getString(cursor.getColumnIndexOrThrow("mime"));
        long start_time = cursor.getLong(cursor.getColumnIndexOrThrow("start_time"));
        long end_time = cursor.getLong(cursor.getColumnIndexOrThrow("end_time"));
        String path = cursor.getString(cursor.getColumnIndexOrThrow("path"));

        RelatedRecord record = new RelatedRecord();

        record.setId(id);
        record.setEnd_time(end_time);
        record.setName(name);
        record.setMime(mime);
        record.setStart_time(start_time);
        record.setPath(path);
        return record;
    }


    public static Map<String,Object> fromRecordToMap(RelatedRecord record){
        Map recordMap = new HashMap<String,Object>();
        recordMap.put("id", record.getId());
        recordMap.put("name", record.getName());
        recordMap.put("mime", record.getMime());
        recordMap.put("start_time", record.getStart_time());
        recordMap.put("end_time", record.getEnd_time());
        recordMap.put("path", record.getPath());

        return recordMap;
    }

    public static ContentValues fromMapToContentValues(Map<String,Object> map){
        ContentValues contentValues = new ContentValues();
        contentValues.put("id",(Integer)map.get("id"));
        contentValues.put("name",(String)map.get("name"));
        contentValues.put("mime",(String)map.get("mime"));
        contentValues.put("start_time",(Integer)map.get("start_time"));
        contentValues.put("end_time",(String)map.get("path"));
        contentValues.put("mime",(String)map.get("mime"));

        return contentValues;
    }


    public static ContentValues fromRecordToContentValues(RelatedRecord record){

        ContentValues contentValues = new ContentValues();

        contentValues.put("id",record.getId());
        contentValues.put("name",record.getName());
        contentValues.put("mime",record.getMime());
        contentValues.put("start_time",record.getStart_time());
        contentValues.put("end_time",record.getEnd_time());
        contentValues.put("path",record.getPath());

        return contentValues;
    }


    public static List<RelatedRecord> sortRecords(List<RelatedRecord> records){
        Comparator comp = new Comparator() {
            @Override
            public int compare(Object lhs, Object rhs) {
                RelatedRecord rec1 = (RelatedRecord) lhs;
                RelatedRecord rec2 = (RelatedRecord) rhs;
                if (rec1.getStart_time()> rec2.getStart_time())
                    return -1;
                else
                    return 1;
            }
        };

        Collections.sort(records, comp);
        return records;
    }


    public static void openRecord(Context context,String name ,String path , String mime){
        String number;
        int idx;
        Intent intent;
        PackageManager packageManager;
        switch(mime){
            case "application/pdf":
            case "application/msword":
            case "application/vnd.ms-excel":
            case "application/vnd.ms-powerpoint":
            case "text/plain":
                File f = new File(path);
                if(f.exists()){
                    Uri wpsUri = Uri.fromFile(f);
                    //  intent = new Intent(SearchMainActivity.this,WPSActivity.class);
                    intent = new Intent();

                    intent.setDataAndType(wpsUri, mime);

                    intent.setAction(Intent.ACTION_VIEW);
               /* intent.setAction(Intent.ACTION_VIEW);*/


                    Log.e("wps", "openRecord" + mime);
                    context.startActivity(intent);
                }

                break;
            case "text/html":
            /*intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(path);
            intent.setData(content_url);
            startActivity(intent);*/

                Uri content_url = Uri.parse(path);
                Intent it = new Intent(Intent.ACTION_VIEW, content_url);
                context.startActivity(it);

                break;
            case "audio/mpeg":
                //intent.setAction("com.android.music");
            /*packageManager = getPackageManager();
            intent =packageManager.getLaunchIntentForPackage("com.android.music");
            startActivity(intent);*/

            /*intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_APP_MUSIC);
            startActivity(intent);*/

                intent = new Intent("android.intent.action.MUSIC_PLAYER");
                context.startActivity(intent);
                break;
            case "application/app":
                packageManager = context.getPackageManager();
                intent =packageManager.getLaunchIntentForPackage(path);
                context.startActivity(intent);

                break;
            case "application/sms":
                idx = name.indexOf("/");
                number = name.substring(idx + 1);
                Uri uri = null;
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setType("vnd.android-dir/mms-sms");
                intent.setData(uri.parse("content://mms-sms/" + number));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                break;
            case "application/calendar":

                packageManager = context.getPackageManager();
                intent =packageManager.getLaunchIntentForPackage("com.android.calendar");
                context.startActivity(intent);
                break;

            case "application/email":

                packageManager = context.getPackageManager();
                intent =packageManager.getLaunchIntentForPackage("com.tencent.androidqqmail");
                context.startActivity(intent);

                break;
            case "application/call":
                intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:" + name.substring(name.indexOf("/") + 1)));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

                break;
        }

    }



}
