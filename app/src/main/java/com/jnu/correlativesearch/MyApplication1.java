package com.jnu.correlativesearch;

import android.content.Context;

import com.jnu.correlativesearch.services.BackgroundService;
import com.jnu.correlativesearch.broadcast.Receiver1;
import com.jnu.correlativesearch.broadcast.Receiver2;
import com.jnu.correlativesearch.services.Service2;
import com.marswin89.marsdaemon.DaemonApplication;
import com.marswin89.marsdaemon.DaemonConfigurations;

/**
 * Created by zhuang on 2016/5/7.
 */
public class MyApplication1 extends DaemonApplication {

    /**
     * you can override this method instead of {@link android.app.Application attachBaseContext}
     * @param base
     */
    @Override
    public void attachBaseContextByDaemon(Context base) {
        super.attachBaseContextByDaemon(base);
    }


    /**
     * give the configuration to lib in this callback
     * @return
     */
    @Override
    protected DaemonConfigurations getDaemonConfigurations() {
        DaemonConfigurations.DaemonConfiguration configuration1 = new DaemonConfigurations.DaemonConfiguration(
                "com.jnu.correlativesearch:process1",
                BackgroundService.class.getCanonicalName(),
                Receiver1.class.getCanonicalName());

        DaemonConfigurations.DaemonConfiguration configuration2 = new DaemonConfigurations.DaemonConfiguration(
                "com.jnu.correlativesearch:process2",
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