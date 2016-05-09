package com.jnu.correlativesearch.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyReceiver extends BroadcastReceiver {
    public MyReceiver() {
    }
    public static final String ACTION = "com.example.zhuang.helloworld.android.intent.myreceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("接收到广播");
    }
}
