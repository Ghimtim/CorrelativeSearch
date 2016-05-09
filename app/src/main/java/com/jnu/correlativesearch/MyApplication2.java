package com.jnu.correlativesearch;

import android.app.Application;
import android.content.Context;

import com.jnu.correlativesearch.broadcast.Receiver1;
import com.jnu.correlativesearch.broadcast.Receiver2;
import com.jnu.correlativesearch.services.BackgroundService;
import com.jnu.correlativesearch.services.Service2;
import com.marswin89.marsdaemon.DaemonClient;
import com.marswin89.marsdaemon.DaemonConfigurations;

/**
 * Implementation 2<br/>
 * if you have to extends other Application, use this method.<br/>
 *
 * Created by Mars on 12/24/15.
 */
public class MyApplication2 extends Application {

    private DaemonClient mDaemonClient;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        mDaemonClient = new DaemonClient(createDaemonConfigurations());
        mDaemonClient.onAttachBaseContext(base);
    }



    private DaemonConfigurations createDaemonConfigurations(){
        DaemonConfigurations.DaemonConfiguration configuration1 = new DaemonConfigurations.DaemonConfiguration(
                "com.marswin89.marsdaemon.demo:process1",
                BackgroundService.class.getCanonicalName(),
                Receiver1.class.getCanonicalName());
        DaemonConfigurations.DaemonConfiguration configuration2 = new DaemonConfigurations.DaemonConfiguration(
                "com.marswin89.marsdaemon.demo:process2",
                Service2.class.getCanonicalName(),
                Receiver2.class.getCanonicalName());
        DaemonConfigurations.DaemonListener listener = new MyDaemonListener();
        //return new DaemonConfigurations(configuration1, configuration2);//listener can be null
        return new DaemonConfigurations(configuration1, configuration2, listener);
    }


    class MyDaemonListener implements DaemonConfigurations.DaemonListener{
        @Override
        public void onPersistentStart(Context context) {
        }

        @Override
        public void onDaemonAssistantStart(Context context) {
        }

        @Override
        public void onWatchDaemonDaed() {
        }
    }
}
