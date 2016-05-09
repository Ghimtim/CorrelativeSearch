package com.jnu.correlativesearch.activities;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Intent;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.jnu.correlativesearch.R;
import com.jnu.correlativesearch.beans.RelatedRecord;
import com.jnu.correlativesearch.utils.Define;
import com.jnu.correlativesearch.utils.WPSUtils;

public class WPSActivity extends AppCompatActivity {

    private BroadcastReceiver receiver;

    private Button view;

    private Button edit;

    @Override
    protected void onCreate(Bundle savedInstanceState){

    super.onCreate(savedInstanceState);


    setContentView(R.layout.activity_wps);

    view=(Button)findViewById(R.id.wps_view);

    edit=(Button)findViewById(R.id.wps_edit);


    Intent intent = getIntent();

    Log.e("wps","onCreate: "+intent.getAction());

    Uri uri = intent.getData();


    if(uri!=null){

        if (openFile(uri) == true) {
            String filePath = uri.getPath();
            String fileName = uri.getLastPathSegment();
            String mime = intent.getType();
            if (mime == null) {
                mime = "";
            }
            Log.e("wps", "onCreate: " + uri.toString());
            Log.e("wps", "onCreate: " + uri.getPath());
            Log.e("wps", "onCreate: " + fileName);
            Log.e("wps", "onCreate: " + mime);
            RelatedRecord record = new RelatedRecord();
            long start_time = System.currentTimeMillis();
            record.setName(fileName);
            record.setPath(filePath);
            record.setStart_time(start_time);
            record.setMime(mime);
            WPSUtils.putRecord(record);
        }

    }

    finish();

}




    public boolean openFile(Uri uri){

        Intent intent = new Intent();

        Bundle bundle = new Bundle();

        bundle.putString(Define.OPEN_MODE, Define.READ_ONLY);

        bundle.putBoolean(Define.SEND_CLOSE_BROAD, true);

        bundle.putString(Define.THIRD_PACKAGE, "com.example.zhuang.helloworld");

        bundle.putBoolean(Define.CLEAR_TRACE, true);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setClassName("cn.wps.moffice_eng", "cn.wps.moffice.documentmanager.PreStartActivity2");


        intent.setData(uri);
        Log.e("wps", "openFile: " + uri.getLastPathSegment() );

        intent.putExtras(bundle);

        try
        {
            startActivity(intent);
        }
        catch (ActivityNotFoundException e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(receiver != null){
            unregisterReceiver(receiver);
        }

    }



    /*public boolean openFile(String path){

        Intent intent = new Intent();

        Bundle bundle = new Bundle();

        bundle.putString(Define.OPEN_MODE, Define.READ_ONLY);

        bundle.putBoolean(Define.SEND_CLOSE_BROAD, true);

        bundle.putString(Define.THIRD_PACKAGE, "com.example.zhuang.helloworld");

        bundle.putBoolean(Define.CLEAR_TRACE, true);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setClassName("cn.wps.moffice_eng", "cn.wps.moffice.documentmanager.PreStartActivity2");


        File f = new File(path);

        if(f == null || !f.exists()){
            return false;
        }

        Uri uri = Uri.fromFile(f);

        intent.setData(uri);


        intent.putExtras(bundle);

        try
        {
            startActivity(intent);
        }
        catch (ActivityNotFoundException e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }
*/

}

