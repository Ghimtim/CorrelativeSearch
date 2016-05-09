package com.jnu.correlativesearch;

import android.app.KeyguardManager;
import android.content.Context;

/**
 * Created by zhuang on 2016/3/9.
 */
public class LockScreen {
    private Context context;

    public LockScreen(Context context) {
        this.context = context;
    }

    public  synchronized  void lockScreen(){
        KeyguardManager keyguardManager = (KeyguardManager)context.getSystemService(context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("LOCK_TAG");
        keyguardLock.reenableKeyguard();
    }

}
