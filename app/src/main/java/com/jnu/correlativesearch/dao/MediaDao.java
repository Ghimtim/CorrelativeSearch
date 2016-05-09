package com.jnu.correlativesearch.dao;

import com.jnu.correlativesearch.beans.MediaFile;

/**
 * Created by zhuang on 2016/3/21.
 */
public interface MediaDao {

    public MediaFile getMediaByName(String name);
}
