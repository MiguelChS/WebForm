package mc185249.webforms;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.HashMap;

/**
 * Created by mc185249 on 4/5/2016.
 */
public class AttachementProvider extends WebFormsProvider {

    public static final String PROVIDER_NAME = "mc185249.webforms.AttachementProvider";
    public static final String URL = "content://" + PROVIDER_NAME + "/attachement";
    public static final Uri CONTENT_URI = Uri.parse(URL);

    public static final int ATTACHEMENT = 1;
    public static final int ATTACHEMENT_ID = 2;

    private static HashMap<String,String> ATTACHEMENT_PROJECTION_MAP;
    static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME,"attachement",ATTACHEMENT);
        uriMatcher.addURI(PROVIDER_NAME, "attachement/#", ATTACHEMENT_ID);
    }

     public static String ID = "id";
    public static String MIME_TYPE = "mimeType";
    public static String BLOB = "blob";
    public static String EMAIL_ID = "email_id";


    @Override
    public boolean onCreate() {
        DBHelper dbHelper = new DBHelper(getContext());
        db = dbHelper.getWritableDatabase();
        return (db == null ? false : true);
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(ATTACHAMENT_FILES_TABLE_NAME);

        switch (uriMatcher.match(uri)){
            case ATTACHEMENT:
                qb.setProjectionMap(ATTACHEMENT_PROJECTION_MAP);
                break;

            case ATTACHEMENT_ID:
                qb.appendWhere(ID + "=" + uri.getPathSegments().get(1));
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        if (sortOrder == null || sortOrder == ""){
            sortOrder = null;
        }
        Cursor cursor = qb.query(db, projection, selection, selectionArgs, null,null,sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
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
        long rowID = super.db.insert(ATTACHAMENT_FILES_TABLE_NAME, "", values);
        if (rowID > 0){
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI,rowID);
            getContext().getContentResolver().notifyChange(_uri,null);
            return _uri;
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count;

        switch (uriMatcher.match(uri)){
            case ATTACHEMENT:
                count = db.delete(ATTACHAMENT_FILES_TABLE_NAME,selection,selectionArgs);
                break;

            case ATTACHEMENT_ID:
                String id = uri.getPathSegments().get(1);
                count = db.delete(ATTACHAMENT_FILES_TABLE_NAME, ID + " = " + id + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""),selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri,null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)){
            case ATTACHEMENT:
                count = db.update(ATTACHAMENT_FILES_TABLE_NAME,values,selection,selectionArgs);
                break;

            case ATTACHEMENT_ID:
                count = db.update(ATTACHAMENT_FILES_TABLE_NAME, values, ID + " = " + uri.getPathSegments().get(1) +
                        (!TextUtils.isEmpty(selection) ? " AND (" +selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI "+ uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return count;
    }
}
