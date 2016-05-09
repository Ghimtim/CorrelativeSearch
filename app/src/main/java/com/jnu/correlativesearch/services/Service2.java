package com.jnu.correlativesearch.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class Service2 extends Service {
    public Service2() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
