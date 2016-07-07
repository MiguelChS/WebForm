package mc185249.webforms;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQuery;
import android.database.sqlite.SQLiteQueryBuilder;
import android.databinding.tool.expr.BracketExpr;
import android.net.Uri;
import android.text.TextUtils;

import java.util.HashMap;

public class ContactsProvider extends WebFormsProvider {

    public static final String PROVIDER_NAME = "mc185249.webforms.ContactsProvider";
    public static final String URL = "content://" + PROVIDER_NAME + "/contacts";
    public static final Uri CONTENT_URI = Uri.parse(URL);

    //contacts table
    public static final String ID = "id";
    public static final String DIRECIONES = "Direcciones";
    public static final String NOMBRES = "Nombres";
    public static final String PAIS = "Pais";
    public static final String NUMERO = "Numero";

    static final int CONTACTS = 1;
    static final int CONTACTS_ID = 2;

    private static HashMap<String,String> CONTACT_PROJECTION_MAP;
    public static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME,"contacts",CONTACTS);
        uriMatcher.addURI(PROVIDER_NAME,"contacts/#",CONTACTS_ID);
    }

    public ContactsProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)){
            case CONTACTS:
                count = db.delete(CONTACTS_TABLE_NAME,selection,selectionArgs);
                break;
            case CONTACTS_ID:
                String id = uri.getPathSegments().get(1);
                count = db.delete(CONTACTS_TABLE_NAME,
                        ID + " = " + id + (!TextUtils.isEmpty(selection) ?
                        " AND (" + selection + ')' : ""),
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowID = super.db.insert(CONTACTS_TABLE_NAME,"",values);
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
        return (db == null)? false : true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(CONTACTS_TABLE_NAME);
        switch (uriMatcher.match(uri)){
            case CONTACTS:
                qb.setProjectionMap(CONTACT_PROJECTION_MAP);
                break;
            case CONTACTS_ID:
                qb.appendWhere(ID + "= " + uri.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        if (sortOrder == null || sortOrder == ""){
            sortOrder = null;
        }

        Cursor cursor = qb.query(db,projection,selection,selectionArgs,
                null,null,sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(),uri);

        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)){
            case CONTACTS:
                count = db.update(CONTACTS_TABLE_NAME,values,selection,selectionArgs);
                break;
            case CONTACTS_ID:
                count = db.update(
                        CONTACTS_TABLE_NAME,
                        values,
                        ID + " = " + uri.getPathSegments().get(1) +
                                (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs
                );
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return count;
    }
}
