package com.example.zhuang.helloworld.database;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import com.jnu.correlativesearch.database.MyDBHelper;

/**
 * Created by zhuang on 2016/3/17.
 */
public class MyDBHelperTest extends AndroidTestCase {

    public void testOnCreate() throws Exception {

    }

    public void testGetInstance() throws Exception {
        Log.e("db", "testGetInstance: ");
        MyDBHelper myDBHelper = MyDBHelper.getInstance(this.getContext());
        SQLiteDatabase db = myDBHelper.getWritableDatabase();
        System.out.println("database:" + db);
        Log.e("db", "testGetInstance: ");
    }

    public void testOnUpgrade() throws Exception {

    }
}