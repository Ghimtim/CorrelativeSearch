package com.jnu.correlativesearch.services;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.jnu.correlativesearch.BrowserHisGetter;
import com.jnu.correlativesearch.beans.RelatedRecord;
import com.jnu.correlativesearch.dao.RecordDao;
import com.jnu.correlativesearch.dao.RecordDaoImpl;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by GoneAir on 2016/3/25.
 */
public class TaskMonitor extends Service implements Runnable {

    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        Thread thread  = new Thread(this);
        thread.start();
        return super.onStartCommand(intent,flags,startId);
    }





    @Override
    public void run() {

        ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> infoList;
        RecordDao rdi = new RecordDaoImpl(this);
        HashMap<String,RelatedRecord> oldMap = new HashMap<String,RelatedRecord>();
        HashMap<String,RelatedRecord> newMap = new HashMap<String,RelatedRecord>();

        while(true){
            newMap.clear();
            infoList  = am.getRunningTasks(10);
            Date Time = new Date();
            long TimeStamp = Time.getTime();
            for (ActivityManager.RunningTaskInfo info : infoList){
                RelatedRecord record = new RelatedRecord();
                String pkgName = info.topActivity.getPackageName();
                record.setName(getAppName(pkgName));
                Log.i("TaskMonitor", getAppName(pkgName));
                record.setPath(pkgName);
                Log.i("TaskMonitor",pkgName );
                record.setMime("application/app");
                if(oldMap.containsKey(pkgName)) {
                    record.setStart_time(oldMap.get(pkgName).getStart_time());
                    Log.i("TaskMonitor", "StartTime1" + TimeStamp );
                }
                else {
                    record.setStart_time(TimeStamp);
                    Log.i("TaskMonitor", "StartTime2" + TimeStamp );
                }
                newMap.put(pkgName,record);
            }

            Set set = oldMap.entrySet();
            Iterator iterator = set.iterator();
            HashMap.Entry<String,RelatedRecord> entry;
            while(iterator.hasNext()){
                entry = (HashMap.Entry<String,RelatedRecord>) iterator.next();
                if(!newMap.containsKey(entry.getKey()) && !entry.getKey().equals("android") && !entry.getKey().equals("com.jnu.associativesearch") && !entry.getKey().equals("com.android.systemui")){
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
    public IBinder onBind(Intent intent) {

        Thread thread  = new Thread(this);
        thread.start();

        return new Binder();
    }
}
