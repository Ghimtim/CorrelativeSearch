package com.example.zhuang.helloworld.dao;

import android.test.AndroidTestCase;
import android.util.Log;

import com.jnu.correlativesearch.beans.RelatedRecord;
import com.jnu.correlativesearch.dao.RecordDao;
import com.jnu.correlativesearch.dao.RecordDaoImpl;

import java.util.Iterator;
import java.util.List;

/**
 * Created by zhuang on 2016/3/17.
 */
public class RecordDaoImplTest extends AndroidTestCase {

    public void testGetRecordById() throws Exception {
        RecordDao dao = new RecordDaoImpl(this.getContext());
        RelatedRecord record = dao.getRecordById(3);
        Log.e("record", "testGetRecordById: " + record.toString() );
    }

    public void testGetRecordsByContent() throws Exception {
        RecordDao dao = new RecordDaoImpl(this.getContext());

        RelatedRecord record = dao.getRecordById(1);

        Log.e("record", record.toString() );
    }

    public void testInsertRecord() throws Exception {
        System.out.print(this.getContext());
        RecordDao dao = new RecordDaoImpl(this.getContext());
        if(this.getContext() == null){
            System.out.println("database is null hahahahhahaahahahahh");
        }
        RelatedRecord record = new RelatedRecord();
        record.setName("ddddd");
        record.setMime("text/html");
        record.setPath("ccccc");
        record.setStart_time(1234);
        record.setEnd_time(5678);
        long i = dao.insertRecord(record);
        Log.e("record", "long i: " +  i);


      /*  record = dao.getRecordById(2);

        System.out.println(record);*/



        List<RelatedRecord> records = dao.getRecordsByContent("d");
        if(records == null){
            System.out.print("record2 is null hahahahahah");
        }
        System.out.print("record2 is null hahahahahah");
        Iterator iterator = records.iterator();
        while(iterator.hasNext()){
            System.out.println(((RelatedRecord)iterator.next()).toString());
        }
        System.out.print("record2 is null hahahahahah");
        Log.e("record", "testGetRecordById: " + records.toString());
    }




}