package com.jnu.correlativesearch;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;

/**
 * Created by GoneAir on 2016/3/21.
 */
public class ContactGetter extends Activity {

    public String getContactName(ContentResolver resolver,String number) {
        if (TextUtils.isEmpty(number)) {
            return "";
        }

        Uri lookupUri = null;
        String[] projection = new String[] { ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.DISPLAY_NAME };
        Cursor cursor = null;

        try {
            lookupUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
            cursor =resolver.query(lookupUri, projection, null, null, null);
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                lookupUri = Uri.withAppendedPath(android.provider.Contacts.Phones.CONTENT_FILTER_URL,
                        Uri.encode(number));
                cursor = resolver.query(lookupUri, projection, null, null, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String ret = number;
        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            ret = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup.DISPLAY_NAME));
        }

        cursor.close();
        return ret;
    }

}
