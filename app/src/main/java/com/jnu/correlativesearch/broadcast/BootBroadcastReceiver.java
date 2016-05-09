package com.jnu.correlativesearch.broadcast;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class BootBroadcastReceiver extends BroadcastReceiver {
    private static final String ACTION = "android.intent.action.BOOT_COMPLETED";
    private Intent serviceIntent;
    public BootBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"boot",Toast.LENGTH_LONG);
        Log.e("boot", "onReceive: " );
        if(intent.getAction().equals(ACTION)){
            Log.e("boot", "onReceive: " + intent.getAction().toString());
            serviceIntent = new Intent();
            serviceIntent.setComponent(new ComponentName("com.jnu.bgservice","com.jnu.bgservice.BackgroundService"));
            context.startService(serviceIntent);
        }


    }
}
