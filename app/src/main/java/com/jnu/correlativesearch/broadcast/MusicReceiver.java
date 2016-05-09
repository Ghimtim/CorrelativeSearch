package com.jnu.correlativesearch.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.jnu.correlativesearch.utils.MusicUtils;

import java.util.Calendar;

public class MusicReceiver extends BroadcastReceiver {
    public MusicReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.e("Music", "packageName:" + context.getPackageName());
        Log.e("Music", "intent:" + intent.toString());
        //String cmd = intent.getStringExtra("command");
        Log.e("Music", "action" + action);
        Bundle bundle = intent.getExtras();

        for(String str : bundle.keySet()){
            Log.e("bundle",str );
        }


        String artist = intent.getStringExtra("artist");
        String album = intent.getStringExtra("album");
        String track = intent.getStringExtra("track");
        String title = intent.getStringExtra("title");
        String displayName = intent.getStringExtra("display");
        Log.e("Music","artist:" + artist+"album:"+album+"track:"+track + "title:" + title + "display:" + displayName);
        Calendar calendar = Calendar.getInstance();
        long time = calendar.getTimeInMillis();

        MusicUtils.notifyMetaChanged(context, intent, time);
    }
}
