package com.jnu.correlativesearch.dao;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import com.jnu.correlativesearch.beans.MediaFile;

import java.util.List;

/**
 * Created by zhuang on 2016/3/21.
 */
public class MediaDaoImpl implements MediaDao{

    private Context context;

    @Override
    public MediaFile getMediaByName(String name) {
        ContentResolver resolver = context.getContentResolver();

        Cursor cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.MIME_TYPE}, "title like ?", new String[]{"%" + name + "%"}, null);



        MediaFile mediaFile = new MediaFile();

        String title;

        String displayName;
        String data;
        String mime;
        if(cursor != null && cursor.moveToNext()){

            title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
            displayName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
            data = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
            mime = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE));
           Log.e("Music", "getMediaByName: " + title );
            mediaFile.setName(title);
            mediaFile.setDisplayName(displayName);
            mediaFile.setPath(data);
            mediaFile.setMime(mime);
        }
        return mediaFile;
    }

    public MediaDaoImpl(Context context) {
        this.context = context;
    }

    public void getAllMedia(){
        ContentResolver resolver = context.getContentResolver();

        Cursor cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.MIME_TYPE},null, null, null);

        String title;

        String displayName;
        String data;
        String mime;
        if(cursor != null && cursor.moveToNext()){

            title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
            displayName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
            data = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
            mime = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE));
            Log.e("Music", "getMediaByName: " +  "title" + title + "displayname" + displayName + "data" + data + "mime" + mime);
        }
    }
}
