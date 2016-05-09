package com.jnu.correlativesearch.utils;

import android.content.Context;
import android.util.Log;

import com.jnu.correlativesearch.beans.RelatedRecord;
import com.jnu.correlativesearch.dao.RecordDao;
import com.jnu.correlativesearch.dao.RecordDaoImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhuang on 2016/4/1.
 */
public class WPSUtils {


    public static Map<String,RelatedRecord> map = new HashMap<>(3);


    public static void putRecord(RelatedRecord record){
        String path = record.getPath();
        if(path != null){
            if(map.get(path) == null){
                map.put(path,record);
            }
        }
    }


    public static boolean addRecord(Context context , String path){

        RelatedRecord record = map.remove(path);

        if(record != null){

            RecordDao dao = new RecordDaoImpl(context);

            dao.insertRecord(record);

            Log.e("wps", "addRecord: " + "success" );

            return true;

        }
        Log.e("wps", "addRecord: " + "fail" );
        return false;

    }


    public static RelatedRecord getRecord(String path){
        return map.get(path);
    }

}
