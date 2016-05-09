package com.jnu.correlativesearch.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.jnu.correlativesearch.beans.RelatedRecord;
import com.jnu.correlativesearch.utils.WPSUtils;

public class WPSReceiver extends BroadcastReceiver {

    public static int count = 0;

    public WPSReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
       // Log.e("wps", "close" );
        count++;
        long end_time = System.currentTimeMillis();
        Log.e("wps", "close:" + count + intent.getAction());
        Bundle bundle = intent.getExtras();

        for(String str : bundle.keySet()){
            Log.e("wps", "onReceive: " + str + bundle.getString(str));
        }
        String action = intent.getAction();
        if("cn.wps.moffice.file.close".equals(action)){

            String filePath = bundle.getString("CloseFile");

            RelatedRecord record = WPSUtils.getRecord(filePath);

            if(record != null){
                record.setEnd_time(end_time);
                WPSUtils.addRecord(context,filePath);
            }

        }
    }
}
