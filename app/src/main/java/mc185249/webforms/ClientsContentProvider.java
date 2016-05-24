package com.example.mc185249.webforms;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.HashMap;

public class ClientsContentProvider extends WebFormsProvider {

    static  final String PROVIDER_NAME = "com.example.mc185249.webforms.ClientsContentProvider";
    static  final String URL = "content://" + PROVIDER_NAME + "/clients";
    static  final Uri CONTENT_URI = Uri.parse(URL);

    //clients table
    static final String ID = "id";
    static final String PAIS = "pais";
    static final String NOMBRE = "nombre";
    static final String NUMERO = "numero";

    static final int CLIENT = 1;
    static final int CLIENT_ID = 2;

    private static HashMap<String, String> CLIENTS_PROJECTION_MAP;
    static final UriMatcher uriMatcher;
    static {
      uriMatcher= new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME,"clients",CLIENT);
        uriMatcher.addURI(PROVIDER_NAME,"clients/#",CLIENT_ID);
    };

    public ClientsContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)){
            case CLIENT:
                count = db.delete(CLIENT_TABLE_NAME,selection,selectionArgs);
                break;

            case CLIENT_ID:
                String id = uri.getPathSegments().get(1);
                count = db.delete(CLIENT_TABLE_NAME,ID + " = " + id + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""),selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this recipients handle requests for the MIME type of the data
        // at the given URI.
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
       long rowID = super.db.insert(CLIENT_TABLE_NAME,"",values);
        if (rowID > 0){
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI,rowID);
            getContext().getContentResolver().notifyChange(_uri,null);
            return _uri;
        }

        return null;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DBHelper dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
        return (db == null)? false:true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(CLIENT_TABLE_NAME);

        switch (uriMatcher.match(uri)){
            case CLIENT:
                qb.setProjectionMap(CLIENTS_PROJECTION_MAP);
                break;
            case CLIENT_ID:
                qb.appendWhere(ID + "=" + uri.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        if (sortOrder == null || sortOrder == ""){
            sortOrder = null;
        }

        Cursor cursor = qb.query(db,projection,selection,selectionArgs,null,null,null);
        cursor.setNotificationUri(getContext().getContentResolver(),uri);

        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)){
            case CLIENT:
                count = db.update(CLIENT_TABLE_NAME,values,selection,selectionArgs);
                break;

            case CLIENT_ID:
                count = db.update(CLIENT_TABLE_NAME,values,ID + " = " + uri.getPathSegments().get(1) +
                        (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return count;
    }
}
