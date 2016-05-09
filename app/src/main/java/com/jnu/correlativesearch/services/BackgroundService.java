package com.jnu.correlativesearch.services;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import com.jnu.correlativesearch.BrowserHisGetter;
import com.jnu.correlativesearch.SmsObserver;
import com.jnu.correlativesearch.beans.RelatedRecord;
import com.jnu.correlativesearch.broadcast.CallReceiver;
import com.jnu.correlativesearch.broadcast.MusicReceiver;
import com.jnu.correlativesearch.broadcast.SmsReceiver;
import com.jnu.correlativesearch.broadcast.WPSReceiver;
import com.jnu.correlativesearch.dao.RecordDao;
import com.jnu.correlativesearch.dao.RecordDaoImpl;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class BackgroundService extends Service implements Runnable{

   // private Intent serviceIntent;
    private BroadcastReceiver wpsReceiver;

    private BroadcastReceiver smsReceiver;

    private BroadcastReceiver callReceiver;

    private BroadcastReceiver musicReceiver;

    private SmsObserver smsObserver;

    private static Thread thread = null;

    private volatile static boolean exits = false;

    //private Intent taskMService;


    private static final String TAG = "background";

    public BackgroundService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "onbind ......");

        return new Binder();
    }


    @Override
    public void run() {

        ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> infoList;
        RecordDao rdi = new RecordDaoImpl(this);
        HashMap<String,RelatedRecord> oldMap = new HashMap<String,RelatedRecord>();
        HashMap<String,RelatedRecord> newMap = new HashMap<String,RelatedRecord>();

        while(!exits){
            newMap.clear();
            infoList  = am.getRunningTasks(10);
            Date Time = new Date();
            long TimeStamp = Time.getTime();
            for (ActivityManager.RunningTaskInfo info : infoList){
                RelatedRecord record = new RelatedRecord();
                String pkgName = info.topActivity.getPackageName();
                record.setName(getAppName(pkgName));
                record.setPath(pkgName);
                record.setMime("application/app");
                if(oldMap.containsKey(pkgName)) {
                    record.setStart_time(oldMap.get(pkgName).getStart_time());
                }
                else {
                    record.setStart_time(TimeStamp);
                }
                newMap.put(pkgName,record);
            }

            Set set = oldMap.entrySet();
            Iterator iterator = set.iterator();
            HashMap.Entry<String,RelatedRecord> entry;
            while(iterator.hasNext()){
                entry = (HashMap.Entry<String,RelatedRecord>) iterator.next();
                if(!newMap.containsKey(entry.getKey()) && !entry.getKey().equals("android") && !entry.getKey().equals("com.jnu.correlativesearch") && !entry.getKey().equals("com.android.systemui")){
                    RelatedRecord rec = entry.getValue();
                    rec.setEnd_time(TimeStamp);
                    Log.e("TaskMonitor", rec.getName());
                    Log.e("TaskMonitor", rec.getPath());
                    Log.e("TaskMonitor", rec.getStart_time() + "");
                    Log.e("TaskMonitor", "EndTime" + TimeStamp);
                    rdi.insertRecord(rec);
                    if(rec.getPath().equals("com.android.browser")){
                        BrowserHisGetter getter = new BrowserHisGetter();
                        Context context = getApplicationContext();
                        getter.getBrowserHis(rec.getStart_time(),rec.getEnd_time(),context);
                    }
                }
            }

            oldMap = (HashMap<String,RelatedRecord>)newMap.clone();
            delay(1000);
        }
    }

    public String getAppName(String pkgName){
        String appName = "";
        PackageManager pm = this.getApplicationContext().getPackageManager();
        List<ApplicationInfo> appInfoList = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);

        for(ApplicationInfo info : appInfoList){
            if(pkgName.equals(info.processName)){
                appName = info.loadLabel(pm).toString();
                break;
            }
        }

        return appName;
    }

    private void delay(int sec) {
        try{
            Thread.currentThread();
            Thread.sleep(sec);
        }catch(InterruptedException e){
            System.err.print(e);
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
      //  acquireWakeLock();
        if(smsReceiver == null) {
            smsReceiver = new SmsReceiver();
            IntentFilter iF = new IntentFilter();
            iF.addAction("android.provider.Telephony.SMS_RECEIVED");
            iF.addAction("android.provider.Telephony.SMS_DELIVER");
            registerReceiver(smsReceiver, iF);
            Log.e("background", "onCreate: ");
        }

        if(callReceiver == null){
            callReceiver = new CallReceiver();
            IntentFilter callIF = new IntentFilter();
            callIF.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
            callIF.addAction("android.intent.action.PHONE_STATE");
            registerReceiver(callReceiver, callIF);
        }


        if(smsObserver == null) {
            smsObserver = new SmsObserver(new android.os.Handler(), this);

            getContentResolver().registerContentObserver(Uri.parse("content://sms"), true, smsObserver);
        }

        if (wpsReceiver == null) {

            wpsReceiver = new WPSReceiver();

            IntentFilter wpsIF = new IntentFilter();

            wpsIF.addAction("cn.wps.moffice.file.close");
            wpsIF.addAction("com.kingsoft.writer.back.key.down");
            wpsIF.addAction("com.kingsoft.writer.home.key.down");
            wpsIF.addAction("cn.wps.moffice.file.save");

            registerReceiver(wpsReceiver, wpsIF);
        }



        if(musicReceiver == null){
            musicReceiver = new MusicReceiver();
            IntentFilter musicIF = new IntentFilter();
            musicIF.addAction("com.android.music.metachanged");


            musicIF.addAction("com.kugou.android.music.metachanged");
            // iF.addAction("com.kugou.android.music.playstatechanged");
    /*    iF.addAction("com.netease.cloudmusic.service.PlayService");
        iF.addAction("com.netease.music.action.STAR_MUSIC");
        iF.addAction("com.netease.cloudmusic.metachanged");*/

            musicIF.addAction("com.android.mediacenter.metachanged");

            registerReceiver(musicReceiver, musicIF);
        }

    }


    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        if(thread == null) {
            thread = new Thread(this);
            thread.start();
        }
        return START_STICKY;
    }



    @Override
    public void onDestroy() {
        //super.onDestroy();
        Log.e("background", "onDestroy: ");
        //stopSelf();
        exits = true;
        thread = null;
        if(smsReceiver != null){
            unregisterReceiver(smsReceiver);
        }

        if (wpsReceiver != null) {
            unregisterReceiver(wpsReceiver);
        }

        if (musicReceiver != null) {
            unregisterReceiver(musicReceiver);
        }
        System.out.println("service destroy ......");

    }



}
