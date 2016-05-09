package com.example.zhuang.helloworld.dao;

import android.test.AndroidTestCase;
import android.util.Log;

import com.jnu.correlativesearch.beans.MediaFile;
import com.jnu.correlativesearch.dao.MediaDao;
import com.jnu.correlativesearch.dao.MediaDaoImpl;

/**
 * Created by zhuang on 2016/3/22.
 */
public class MediaDaoTest extends AndroidTestCase {

    public void testGetMediaByName() throws Exception {

        MediaDaoImpl mediaDao = new MediaDaoImpl(this.getContext());
        mediaDao.getAllMedia();




    }
}