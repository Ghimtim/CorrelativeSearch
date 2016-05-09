package com.jnu.correlativesearch.beans;

/**
 * Created by zhuang on 2016/3/21.
 */
public class MediaFile {


    private String name;

    private String displayName;

    private String path;

    public void setMime(String mime) {
        this.mime = mime;
    }

    public String getMime() {

        return mime;
    }

    private String mime;

    public void setName(String name) {
        this.name = name;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {

        return displayName;
    }

    public String getPath() {
        return path;
    }

    public String getName() {

        return name;
    }

    @Override
    public String toString() {
        return "MediaFile{" +
                "name='" + name + '\'' +
                ", displayName='" + displayName + '\'' +
                ", path='" + path + '\'' +
                ", mime='" + mime + '\'' +
                '}';
    }
}
