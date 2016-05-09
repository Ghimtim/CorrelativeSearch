package com.jnu.correlativesearch.utils;

import android.content.Context;
import android.widget.TextView;

import com.jnu.correlativesearch.beans.RelatedRecord;
import com.jnu.correlativesearch.dao.RecordDao;
import com.jnu.correlativesearch.dao.RecordDaoImpl;

import java.util.Iterator;
import java.util.List;

/**
 * Created by zhuang on 2016/3/22.
 */
public class MyThead implements Runnable {
    private TextView tv;
    private Context context;

    public MyThead(Context context, TextView tv) {
        this.tv = tv;
        this.context = context;
    }

    @Override
    public void run() {
        RecordDao dao = new RecordDaoImpl(context);
        List<RelatedRecord> records = dao.getAllRecords();
        StringBuilder str = new StringBuilder("");
        Iterator i = records.iterator();

        while(i.hasNext()){
            str.append(i.next().toString() + "\n");

        }

        tv.setText(str.toString());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
