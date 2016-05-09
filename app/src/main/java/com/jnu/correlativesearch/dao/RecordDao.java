package com.jnu.correlativesearch.dao;

import com.jnu.correlativesearch.beans.RelatedRecord;

import java.util.List;

/**
 * Created by zhuang on 2016/3/17.
 */
public interface RecordDao {

    public List<RelatedRecord> queryRecords(String content , Long startTime, Long endTime , String  mime);

    public List<RelatedRecord> queryFirstTime(long time);

    public RelatedRecord getRecordById(Integer id);

    public List<RelatedRecord> getRecordsByContent(String content);

    public long insertRecord(RelatedRecord record);

    public List<RelatedRecord> getAllRecords();
}
