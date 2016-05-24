package com.example.mc185249.webforms;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.example.mc185249.webforms.WebFormsProvider;

import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by mc185249 on 4/4/2016.
 */
public class EmailsProvider extends WebFormsProvider {

    static final String PROVIDER_NAME = "com.example.mc185249.webforms.EmailsProvider";
    static final String URL = "content://" + PROVIDER_NAME + "/emails";
    static final Uri CONTENT_URI = Uri.parse(URL);

    //email table
    static final String ID = "id";
    static final String FECHA = "fecha";
    static final String ACTIVITY = "activity";
    static final String SUBJECT = "subject";
    static final String BODY = "body";
    static final String RECIPIENT = "recipient";
    static final String FROM = "sender";

    static final int EMAIL = 1;
    static final int EMAIL_ID = 2;

    private static HashMap<String, String> EMAILS_PROJECTION_MAP;

    static final UriMatcher uriMatcher;
    static {
      uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "emails", EMAIL);
        uriMatcher.addURI(PROVIDER_NAME, "emails/#", EMAIL_ID);
    };




    @Override
    public boolean onCreate() {
        Context context = getContext();
        DBHelper dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
        return (db == null)? false:true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(EMAIL_TABLE_NAME);

        switch (uriMatcher.match(uri)){
            case EMAIL:
                qb.setProjectionMap(EMAILS_PROJECTION_MAP);
                break;

            case EMAIL_ID:
                qb.appendWhere(ID + "=" + uri.getPathSegments().get(1));
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        if (sortOrder == null || sortOrder == "" )
        {
            sortOrder = null ;
        }
        Cursor cursor = qb.query(db, projection, selection, selectionArgs, null,null,null,sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        long rowID = super.db.insert(WebFormsProvider.EMAIL_TABLE_NAME, "", values);
        if (rowID > 0){
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;

        switch (uriMatcher.match(uri)){
            case EMAIL:
                count = db.delete(EMAIL_TABLE_NAME, selection, selectionArgs);
                break;

            case EMAIL_ID:
                String id = uri.getPathSegments().get(1);
                count = db.delete(EMAIL_TABLE_NAME, ID + " = " + id +  (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count = 0;

        switch (uriMatcher.match(uri)){
            case EMAIL:
                count = db.update(EMAIL_TABLE_NAME, values, selection, selectionArgs);
                break;

            case EMAIL_ID:
                count = db.update(EMAIL_TABLE_NAME, values, ID + " = " + uri.getPathSegments().get(1) +
                        (!TextUtils.isEmpty(selection) ? " AND (" +selection + ')' : ""), selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }




}
