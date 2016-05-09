package com.jnu.correlativesearch.beans;

/**
 * Created by zhuang on 2016/3/17.
 */
public class RelatedRecord {

    private Integer id;
    private String name;
    private String mime;
    private long start_time;
    private long end_time;
    private String path;

    public static final class MIME{
        public static final String TEXT_PLAIN = "text/plain";
        public static final String AUDIO_MPEG = "audio/mpeg";
    }

    public long getEnd_time() {
        return end_time;
    }

    public Integer getId() {
        return id;
    }

    public long getStart_time() {
        return start_time;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public String getMime() {
        return mime;
    }

    public void setEnd_time(long end_time) {
        this.end_time = end_time;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setStart_time(long start_time) {
        this.start_time = start_time;
    }

    @Override
    public String toString() {
        return "RelatedRecord{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", mime='" + mime + '\'' +
                ", start_time=" + start_time +
                ", end_time=" + end_time +
                ", path='" + path + '\'' +
                '}';
    }
}
