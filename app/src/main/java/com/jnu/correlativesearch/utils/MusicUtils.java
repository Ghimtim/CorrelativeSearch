package com.jnu.correlativesearch.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.jnu.correlativesearch.beans.MediaFile;
import com.jnu.correlativesearch.beans.RelatedRecord;
import com.jnu.correlativesearch.dao.MediaDao;
import com.jnu.correlativesearch.dao.MediaDaoImpl;
import com.jnu.correlativesearch.dao.RecordDao;
import com.jnu.correlativesearch.dao.RecordDaoImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhuang on 2016/3/21.
 */
public class MusicUtils {

    public static Map<String,RelatedRecord> map = new HashMap<>(5);


    private static String KUGOU = "com.kugou.android.music.metachanged";

    private static String MEDIACENTER = "com.android.mediacenter.metachanged";



    public static void notifyMetaChanged(Context context,Intent intent,long time){

        String action = intent.getAction();
        RelatedRecord record = null;
        String track = null;
        RecordDao recordDao = null;
        MediaFile mf = null;
        MediaDao mediaDao = null;
        if(intent != null){
            action = intent.getAction();
            track = intent.getStringExtra("track");
            mediaDao = new MediaDaoImpl(context);
            mf = mediaDao.getMediaByName(track);
            if(map.get(action) == null){
                record = new RelatedRecord();
                record.setStart_time(time);
                record.setName(track);
                record.setMime(mf.getMime());
                record.setPath(mf.getPath());
                map.put(KUGOU,record);

            }else{
                record = map.get(action);
                record.setEnd_time(time);
                recordDao = new RecordDaoImpl(context);
                recordDao.insertRecord(record);
                RelatedRecord record1 = recordDao.getRecordById(2);
                Log.e("record", record1.toString());
                record.setStart_time(time);
                record.setName(mf.getName());
                record.setMime(mf.getMime());
                record.setPath(mf.getPath());

            }
        }


    }

}



