package com.jnu.correlativesearch.broadcast;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.jnu.correlativesearch.ContactGetter;
import com.jnu.correlativesearch.beans.RelatedRecord;
import com.jnu.correlativesearch.dao.RecordDao;
import com.jnu.correlativesearch.dao.RecordDaoImpl;

import java.util.Date;

/**
 * Created by GoneAir on 2016/3/24.
 */
public class CallReceiver extends BroadcastReceiver {

    private static RelatedRecord record = new RelatedRecord();
    private static boolean incomingFlag = false;
    private static boolean tag = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        ContactGetter cg = new ContactGetter();
        Date startTime;
        long startTimeStamp;
        Date endTime;
        long endTimeStamp;

        if(intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)){
            incomingFlag = false;
            String name = cg.getContactName(context.getContentResolver(),intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER));
            startTime = new Date();
            startTimeStamp = startTime.getTime();
            record.setName(name + "/" + intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER));
            record.setMime("application/call");
            record.setStart_time(startTimeStamp);
            record.setPath("拨打");
            tag = true;
            Log.e("CallReceiver", "Calling" + incomingFlag);

        }else{
            TelephonyManager tm = (TelephonyManager)context.getSystemService(Service.TELEPHONY_SERVICE);
            switch(tm.getCallState()){
                case TelephonyManager.CALL_STATE_RINGING:
                    incomingFlag = true;
                    String inComingNumber = intent.getStringExtra("incoming_number");
                    String name = cg.getContactName(context.getContentResolver(), inComingNumber);
                    startTime = new Date();
                    startTimeStamp = startTime.getTime();
                    record.setMime("application/call");
                    record.setName(name + "/" + inComingNumber);
                    record.setStart_time(startTimeStamp);
                    record.setPath("接收");
                    tag = true;
                    Log.e("CallReceiver", "Ringing" + incomingFlag + inComingNumber);
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.e("CallReceiver","OFFHOOK" + incomingFlag);
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.e("CallReceiver","IDLE" + incomingFlag);
                    endTime = new Date();
                    endTimeStamp = endTime.getTime();
                    record.setEnd_time(endTimeStamp);
                    if(tag == true){
                        RecordDao rdi = new RecordDaoImpl(context);
                        rdi.insertRecord(record);
                        tag = false;
                    }
                    break;

            }
        }
    }
}
