package com.jnu.correlativesearch.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.telephony.gsm.SmsManager;
import android.util.Log;

import com.jnu.correlativesearch.ContactGetter;
import com.jnu.correlativesearch.beans.RelatedRecord;
import com.jnu.correlativesearch.dao.RecordDao;
import com.jnu.correlativesearch.dao.RecordDaoImpl;

import java.util.Date;
/**
 * Created by GoneAir on 2016/3/21.
 */
public class SmsReceiver extends BroadcastReceiver {
    public static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";
    public static final String SMS_DELIVER_ACTION = "android.provider.Telephony.SMS_DELIVER";

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i("SmsReceiver", "Received Sms");
        SmsMessage msg = null;
        RelatedRecord record = new RelatedRecord();
        String action = intent.getAction();
        String msgBody = "";
        String number = "";
        String contact = "";
        long time = 0;
        RecordDao rdi = new RecordDaoImpl(context);
        if(SMS_RECEIVED_ACTION.equals(action) || SMS_DELIVER_ACTION.equals(action)){
            Bundle bundle = intent.getExtras();
            if(bundle != null){
                Object[] pdus = (Object[])bundle.get("pdus");
                for(Object obj : pdus){
                    msg = SmsMessage.createFromPdu((byte[]) obj);
                    Date date = new Date(msg.getTimestampMillis());
                    time = date.getTime();
                    msgBody = msgBody + msg.getMessageBody();
                    number = msg.getOriginatingAddress();
                    ContactGetter cg = new ContactGetter();
                    contact = cg.getContactName(context.getContentResolver(),number);
                    //msg.getIndexOnIcc()
                    Log.i("SmsReceiver",String.valueOf(msg.getStatusOnIcc()) + SmsManager.STATUS_ON_SIM_UNREAD);
                }
                Log.i("SmsReceiver", msgBody + "---" + number + "---" + contact + "---" + msgBody.length());
                record.setEnd_time(time);
                record.setMime("application/sms");
                record.setStart_time(time);
                record.setName(contact+"/"+number);
                record.setPath(msgBody);

                Log.i("SmsReceiver", "Insert into database");

                rdi.insertRecord(record);

                Log.i("SmsReceiver","End Insert");
            }
        }
    }


}
