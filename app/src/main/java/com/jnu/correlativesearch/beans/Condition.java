package com.jnu.correlativesearch.beans;

/**
 * Created by zhuang on 2016/4/7.
 */
public class Condition {

    private long start_time;

    private long end_time;

    private String mime;

    private String content;

    @Override
    public String toString() {
        return "Condition{" +
                "start_time=" + start_time +
                ", end_time=" + end_time +
                ", mime='" + mime + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    public long getStart_time() {
        return start_time;
    }

    public void setStart_time(long start_time) {
        this.start_time = start_time;
    }

    public Condition() {
        mime = "";
        content = "";
    }

    public Condition(long start_time, long end_time, String content, String mime) {
        this.start_time = start_time;
        this.end_time = end_time;
        this.content = content;

        this.mime = mime;
    }

    public long getEnd_time() {

        return end_time;
    }

    public void setEnd_time(long end_time) {
        this.end_time = end_time;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
