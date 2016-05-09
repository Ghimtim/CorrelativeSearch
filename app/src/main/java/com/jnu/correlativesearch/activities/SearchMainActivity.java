package com.jnu.correlativesearch.activities;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jnu.correlativesearch.R;
import com.jnu.correlativesearch.SmsObserver;
import com.jnu.correlativesearch.broadcast.CallReceiver;
import com.jnu.correlativesearch.broadcast.MusicReceiver;
import com.jnu.correlativesearch.broadcast.SmsReceiver;
import com.jnu.correlativesearch.broadcast.WPSReceiver;
import com.jnu.correlativesearch.beans.RelatedRecord;
import com.jnu.correlativesearch.dao.RecordDao;
import com.jnu.correlativesearch.dao.RecordDaoImpl;
import com.jnu.correlativesearch.services.BackgroundService;
import com.jnu.correlativesearch.services.TaskMonitor;
import com.jnu.correlativesearch.beans.Condition;
import com.jnu.correlativesearch.utils.RecordUtils;
import com.jnu.correlativesearch.utils.TimeTextUtils;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import com.jnu.correlativesearch.Adapter.RecyclerAdapter;
import com.jnu.correlativesearch.Listener.HidingScollListener;
import widget.ClearEditText;
import widget.ExpandableLayout;

public class SearchMainActivity extends AppCompatActivity implements  DialogInterface.OnClickListener,RecyclerAdapter.OnRecyclerViewItemClickListener,TextWatcher,DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener,AdapterView.OnItemSelectedListener,ServiceConnection{

    public static final String STARTDATEPICKER_TAG = "startdatepicker";
    public static final String STARTTIMEPICKER_TAG = "starttimepicker";
    public static final String ENDDATEPICKER_TAG = "enddatepicker";
    public static final String ENDTIMEPICKER_TAG = "endtimepicker";

    private BroadcastReceiver wpsReceiver;

    private BroadcastReceiver smsReceiver;

    private BroadcastReceiver callReceiver;

    private BroadcastReceiver musicReceiver;

    private SmsObserver smsObserver;

    private Intent taskMService;


    private  static RecordDaoImpl recordDaoImpl ;

    private static RelatedRecord record;

    public static Typeface getIconfont() {
        return iconfont;
    }

    private static Typeface iconfont;

    private    Toolbar mToolbar;
    private    RecyclerView mRecyclerview;
    private    ClearEditText mSearchView;
    private    LinearLayout mSearchContainer;

    private Condition condition;

    private static RecyclerAdapter recyclerAdapter;

    private    int year,month,day,hour,minute;

    private List<Condition> history = new ArrayList<>();

    private static String content = "";
    private static long startTime,endTime;
    private static String mime = "";

    private Intent backgroundService;

    private  static List<RelatedRecord> mListRelatedRecord = new ArrayList<RelatedRecord>();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
 /*           getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);*/

            backgroundService = new Intent(this, BackgroundService.class);

            startService(backgroundService);

            mToolbar = (Toolbar)findViewById(R.id.toolbar);
            setSupportActionBar(mToolbar);

            iconfont = Typeface.createFromAsset(getAssets(), "iconfont/iconfont.ttf");

            condition = new Condition();

            initDateAndTime();
            initDateAndTimePicker(savedInstanceState);
            initSpinner(this);

            mSearchContainer = (ExpandableLayout) findViewById(R.id.search_container);
            mSearchView = (ClearEditText) findViewById(R.id.search_view);
            mSearchView.addTextChangedListener(this);
            condition.setContent(mSearchView.getText().toString());
            mRecyclerview = (RecyclerView) findViewById(R.id.recycler_view);
            mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
            recordDaoImpl = new RecordDaoImpl(this);
            mListRelatedRecord = recordDaoImpl.queryFirstTime(condition.getEnd_time());
            recyclerAdapter = new RecyclerAdapter(this, mListRelatedRecord );
            recyclerAdapter.setOnRecyclerViewItemClickListener(this);
            mRecyclerview.setAdapter(recyclerAdapter);

            mRecyclerview.addOnScrollListener(new HidingScollListener() {
                @Override
                public void onHide() {
                    hideViews();
                }

                @Override
                public void onShow() {
                    showViews();
                }
            });



        }

    private void hideViews(){
        mSearchContainer.animate().translationY(-mSearchContainer.getHeight()+mToolbar.getHeight()).setInterpolator(new LinearInterpolator());

    }

    private void showViews() {
        mSearchContainer.animate().translationY(0).setInterpolator(new LinearInterpolator());

    }


  @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == event.KEYCODE_BACK && event.getRepeatCount() == 0){
            Log.e("condition", "onKeyDown: ");
            Condition con = null;
            if(history.size() > 0 && (condition = history.remove(history.size()-1)) != null){
                Log.e("condition", "onClick: " + condition.toString());

               /* setStartTime(condition.getStart_time());
                setEndTime(condition.getEnd_time());
                setMime(condition.getMime());*/


                setStartDateAndTime(condition.getStart_time());
                setEndDateAndTime(condition.getStart_time());
                setSpinner(condition.getMime());
                notify(condition);
                return false;
            }else{
               /* Dialog dialog = new AlertDialog.Builder(this).setIcon(R.drawable.btn_cleartext).setTitle("是否确定退出？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                moveTaskToBack(true);
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();;
                    }
                }).show();*/

                moveTaskToBack(true);
                return true;
            }



        }

        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (taskMService != null) {
            unbindService(this);
        }

        if (smsReceiver != null) {
            unregisterReceiver(smsReceiver);
        }

        if (wpsReceiver != null) {
            unregisterReceiver(wpsReceiver);
        }

        if (musicReceiver != null) {
            unregisterReceiver(musicReceiver);
        }
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month_0, int day) {
       if( datePickerDialog.getTag() == STARTDATEPICKER_TAG) {
           TextView tv_data1 = (TextView) findViewById(R.id.start_data);
           int month = month_0 + 1;
           tv_data1.setText(year + "-" + month + "-" + day);
           getPickDateAndTime(true, year, month, day, 0, 0);
           Toast.makeText(SearchMainActivity.this, "start date:" + year + "-" + month + "-" + day, Toast.LENGTH_SHORT).show();
       }else if( datePickerDialog.getTag() == ENDDATEPICKER_TAG) {
           TextView tv_data2 = (TextView) findViewById(R.id.end_data);
           int month = month_0 + 1;
           tv_data2.setText(year + "-" + month + "-" + day);
           getPickDateAndTime(true, year, month, day, 0, 0);
           Toast.makeText(SearchMainActivity.this, "end date:" + year + "-" + month + "-" + day, Toast.LENGTH_SHORT).show();
       }
    }


    public void onTimeSet(TimePickerDialog timePickerDialog,RadialPickerLayout view, int hourOfDay, int minute) {
            if(timePickerDialog.getTag() == STARTTIMEPICKER_TAG) {
                TextView tv_time1 = (TextView) findViewById(R.id.start_time);
                tv_time1.setText(hourOfDay + ":" + minute);
                getPickDateAndTime(false,0,0,0,hourOfDay,minute);
                Date d = new Date((int)(SearchMainActivity.this.year-1900),(int)(SearchMainActivity.this.month-1),SearchMainActivity.this.day,SearchMainActivity.this.hour,SearchMainActivity.this.minute);
                Long dLong = d.getTime();
                String date = ""+SearchMainActivity.this.year+SearchMainActivity.this.month+SearchMainActivity.this.day+SearchMainActivity.this.hour+SearchMainActivity.this.minute+String.valueOf(dLong);
                Log.e("LJT", "start time" + date);
                condition.setStart_time(dLong);
                //startTime = dLong;
                condition.setContent("");
                Log.e("LJB",startTime+content);
                Date test = new Date();
                test.setTime(startTime);
                Log.e("LJB", test.toString());
                notify(condition);
//                Toast.makeText(SearchMainActivity.this,String.valueOf(dLong),Toast.LENGTH_SHORT).show();
//                Toast.makeText(SearchMainActivity.this, "start time:" + hourOfDay + "-" + minute, Toast.LENGTH_SHORT).show();
            }else if(timePickerDialog.getTag() == ENDTIMEPICKER_TAG) {
                TextView tv_time2 = (TextView) findViewById(R.id.end_time);
                tv_time2.setText(hourOfDay + ":" + minute);
                getPickDateAndTime(false,0,0,0,hourOfDay,minute);
                Date d = new Date((int)(SearchMainActivity.this.year-1900),(int)(SearchMainActivity.this.month-1),SearchMainActivity.this.day,SearchMainActivity.this.hour,SearchMainActivity.this.minute);
                Long dLong = d.getTime();
                String date = ""+SearchMainActivity.this.year+SearchMainActivity.this.month+SearchMainActivity.this.day+SearchMainActivity.this.hour+SearchMainActivity.this.minute+String.valueOf(dLong);
                Log.e("LJT", "end time" + date);
                condition.setEnd_time(dLong);
                notify(condition);
//                Toast.makeText(SearchMainActivity.this, "end time:" + hourOfDay + "-" + minute, Toast.LENGTH_SHORT).show();
            }
    }
    public void initDateAndTime(){
        TextView tv_Sdate = (TextView)findViewById(R.id.start_data);
        TextView tv_Stime = (TextView)findViewById(R.id.start_time);
        TextView tv_Edata = (TextView)findViewById(R.id.end_data);
        TextView tv_Etime = (TextView)findViewById(R.id.end_time);
        Calendar calendar = Calendar.getInstance();
//        获取应用初次安装时间作为默认开始时间
        Date date = new Date();
        long firstInstallTime = 0;
        PackageManager packageManager = this.getPackageManager();
        try{
            PackageInfo packageInfo = packageManager.getPackageInfo(this.getPackageName(),0);
            firstInstallTime = packageInfo.firstInstallTime;
            condition.setStart_time(firstInstallTime);
        }catch (Exception e){
            e.printStackTrace();
        }
/*        date.setTime(firstInstallTime);
        int year = date.getYear()+1900;
        int month1 = date.getMonth()+1;
        String sData = year+"-"+month1+"-"+date.getDate();
        String sTime = date.getHours()+":"+date.getMinutes();*/
        String sData = TimeTextUtils.millisToStringDate(firstInstallTime, "yyyy-MM-dd");
        String sTime = TimeTextUtils.millisToStringDate(firstInstallTime,"HH:mm");
        tv_Sdate.setText(sData);
        tv_Stime.setText(sTime);
        /*int month2 = calendar.get(Calendar.MONTH)+1;
        String eData = calendar.get(Calendar.YEAR)+"-"+month2+"-"+calendar.get(Calendar.DAY_OF_MONTH);
        String eTime = calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE)*/;

        String eData = TimeTextUtils.millisToStringDate(calendar.getTimeInMillis(),"yyyy-MM-dd");
        String eTime = TimeTextUtils.millisToStringDate(calendar.getTimeInMillis(), "HH:mm");
        this.year = calendar.get(Calendar.YEAR);
        this.month = calendar.get(Calendar.MONTH);
        this.day = calendar.get(Calendar.DAY_OF_MONTH);
        this.hour = calendar.get(Calendar.HOUR_OF_DAY);
        this.minute = calendar.get(Calendar.MINUTE);
        tv_Edata.setText(eData);
        tv_Etime.setText(eTime);
        condition.setEnd_time(calendar.getTimeInMillis());
    }
    public void initDateAndTimePicker(Bundle savedInstanceState){
        final Calendar calendar = Calendar.getInstance();
        final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), true);
        final TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false, false);

        findViewById(R.id.start_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //时间选择器
                timePickerDialog.setVibrate(true);
                timePickerDialog.setCloseOnSingleTapMinute(false);
                timePickerDialog.show(getSupportFragmentManager(), STARTTIMEPICKER_TAG);
                //日期选择器
                datePickerDialog.setVibrate(true);
                datePickerDialog.setYearRange(1985, 2028);
                datePickerDialog.setCloseOnSingleTapDay(false);
                datePickerDialog.show(getSupportFragmentManager(), STARTDATEPICKER_TAG);

            }
        });
        findViewById(R.id.end_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //时间选择器
                timePickerDialog.setVibrate(true);
                timePickerDialog.setCloseOnSingleTapMinute(false);
                timePickerDialog.show(getSupportFragmentManager(), ENDTIMEPICKER_TAG);
                //日期选择器
                datePickerDialog.setVibrate(true);
                datePickerDialog.setYearRange(1985, 2028);
                datePickerDialog.setCloseOnSingleTapDay(false);
                datePickerDialog.show(getSupportFragmentManager(), ENDDATEPICKER_TAG);
            }
        });
        if (savedInstanceState != null) {
            DatePickerDialog sdpd = (DatePickerDialog) getSupportFragmentManager().findFragmentByTag(STARTDATEPICKER_TAG);
            if (sdpd != null) {
                sdpd.setOnDateSetListener(this);
            }

            TimePickerDialog stpd = (TimePickerDialog) getSupportFragmentManager().findFragmentByTag(STARTTIMEPICKER_TAG);
            if (stpd != null) {
                stpd.setOnTimeSetListener(this);
            }
            DatePickerDialog edpd = (DatePickerDialog) getSupportFragmentManager().findFragmentByTag(ENDDATEPICKER_TAG);
            if (edpd != null) {
                edpd.setOnDateSetListener(this);
            }

            TimePickerDialog etpd = (TimePickerDialog) getSupportFragmentManager().findFragmentByTag(ENDTIMEPICKER_TAG);
            if (etpd != null) {
                etpd.setOnTimeSetListener(this);
            }
        }
    }
    public void getPickDateAndTime(boolean Tag,int year,int month,int day,int hour,int minute){
        if(Tag){
            this.year = year;
            this.month = month;
            this.day = day;
        }else {
            this.hour = hour;
            this.minute = minute;
        }
    }
    public void initSpinner(Context context){
        final Spinner spinner = (Spinner)findViewById(R.id.type_spinner);
        String mItemType[] = getResources().getStringArray(R.array.type_name);
        ArrayAdapter<String> SpinnerAdpater = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,mItemType);
        spinner.setAdapter(SpinnerAdpater);
        spinner.setOnItemSelectedListener(this);
    }
    public void setSpinner(String mime){
        final Spinner spinner = (Spinner)findViewById(R.id.type_spinner);
        String mItemType[] = getResources().getStringArray(R.array.type_name);
        int k=0;
        for(int i = 0;i<mItemType.length;i++){
            if(mime.equals(mItemType[i])){
                k =i;
                break;
            }
            spinner.setSelection(k,true);
        }
    }



    public void setStartDateAndTime(long milliseconds){
        /*Date date = new Date();
        int year, month,day, hour, minute;
        date.setTime(milliseconds);
        year = date.getYear()+1900;
        month = date.getMonth()+1;
        day =  date.getDate();*/
        TextView tv_data = (TextView)findViewById(R.id.start_data);
//        Log.e("LLL",year + "-" + month + "-" + day);
        tv_data.setText(TimeTextUtils.millisToStringDate(milliseconds,"yyyy-MM-dd"));
        TextView tv_time = (TextView)findViewById(R.id.start_time);
        /*hour = date.getHours();
        minute = date.getMinutes();*/
//        Log.e("LLL", hour + ":" + minute);
        tv_time.setText(TimeTextUtils.millisToStringDate(milliseconds,"HH:mm"));
    }

    public void setEndDateAndTime(long milliseconds){
        /*Date date = new Date();
        int year, month,day, hour, minute;
        date.setTime(milliseconds);
        year = date.getYear()+1900;
        month = date.getMonth()+1;
        day =  date.getDay()+3;*/
        TextView tv_data = (TextView)findViewById(R.id.end_data);
//        Log.e("LLL",year + "-" + month + "-" + day);
        tv_data.setText(TimeTextUtils.millisToStringDate(milliseconds,"yyyy-MM-dd"));
        TextView tv_time = (TextView)findViewById(R.id.end_time);
       /* hour = date.getHours();
        minute = date.getMinutes();
        Log.e("LLL", hour + ":" + minute);*/
        tv_time.setText(TimeTextUtils.millisToStringDate(milliseconds,"HH:mm"));
    }



    public void notify(Condition con){
        List<RelatedRecord> list= recordDaoImpl.queryRecords(con.getContent(),con.getStart_time(),con.getEnd_time(),con.getMime());
        Log.e("LJT","*******"+content+" "+new Date(startTime).toString()+" "+new Date(endTime).toString()+" "+mime);
        mListRelatedRecord.clear();
        mListRelatedRecord.addAll(list);
        recyclerAdapter.notifyDataSetChanged();
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(mSearchView.isFocus()){
//           getText().toString().length() == 0
            if(TextUtils.isEmpty(s)){ //如果为空，删除图标隐藏
                mSearchView.setCompoundDrawables(mSearchView.getLeftImg(),null,null,null);
            }else {
                if(null == mSearchView.getRightImg()){
                    mSearchView.setRight(mSearchView.getCompoundDrawables()[2]);
                }
                mSearchView.setCompoundDrawables(mSearchView.getLeftImg(),null,mSearchView.getRightImg(),null);
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        condition.setContent(mSearchView.getText().toString());
        notify(condition);
        Log.e("LJT", mSearchView.getText().toString());
    }



    @Override
    public void onItemClick(View view) {
       int tag = (int)view.getTag();
        record = mListRelatedRecord.get(tag);
        Log.e("LJTTTTT", "Itemclick");
        Dialog dialog = new AlertDialog.Builder(this).setIcon(R.drawable.search_image)
                .setTitle("选择操作").setMessage(record.getName()+"\n路径："+record.getPath()+"\n打开时间："+ TimeTextUtils.millisToStringDate(record.getStart_time(),"yyyy-MM-dd HH:mm")+"\n关闭时间："+TimeTextUtils.millisToStringDate(record.getEnd_time(),"yyyy-MM-dd HH:mm"))
                .setPositiveButton("查找", this).setNeutralButton("打开", this).setNegativeButton("取消", this).create();
        dialog.show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which){
            //查找 -1
            case Dialog.BUTTON_POSITIVE:
                Log.e("condition", "onClick: " + mime );
                Condition con = new Condition(condition.getStart_time(),condition.getEnd_time(),condition.getContent(),condition.getMime());
                Log.e("condition", "onClick: " + condition.toString());
                history.add(con);
                condition.setStart_time(record.getStart_time());
                condition.setEnd_time(record.getEnd_time());
                setStartDateAndTime(record.getStart_time());
                setEndDateAndTime(record.getEnd_time());

                condition.setMime("");
                condition.setContent("");
                notify(condition);
                dialog.dismiss();
                break;
            //打开 -2
            case Dialog.BUTTON_NEUTRAL:
                RecordUtils.openRecord(this,record.getName(),record.getPath(),record.getMime());
                break;
            //取消 -3
            case Dialog.BUTTON_NEGATIVE:
                dialog.dismiss();
                break;
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String type = adapterView.getSelectedItem().toString();
        Log.e("LJT",type);
       // mime = type;
        condition.setMime(type);
        notify(condition);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        String type = adapterView.getSelectedItem().toString();
        Log.e("LJT",type);
        //mime = "";
        condition.setMime("");
        notify(condition);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {

    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }


}







/*    public void initActionbar() {
        // 自定义标题栏
        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setDisplayShowCustomEnabled(true);
        LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mTitleView = mInflater.inflate(R.layout.custom_action_bar_layout,
                null);
        getActionBar().setCustomView(
                mTitleView,
                new ActionBar.LayoutParams(LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT));
        searchView = (SearchView) mTitleView.findViewById(R.id.search_view);
    }*/



  /*  @Override
    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            // Clear the text filter.
            listView.clearTextFilter();
        } else {
            // Sets the initial value for the text filter.
            // Sets the initial value for the text filter.
            listView.setFilterText(newText.toString());
        }
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        // TODO Auto-generated method stub
        return false;
    }
}

*/