package mc185249.webforms;

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

public class InventarioProvider extends WebFormsProvider {

    public static final String PROVIDER_NAME = "mc185249.webforms.InventarioProvider";
    public static final String URL = "content://" + PROVIDER_NAME + "/inventario";
    public static final Uri CONTENT_URL = Uri.parse(URL);


    //Inventario table
    public static final String ID = "id";
    public static final String CLASE = "clase";
    public static final String CLASEMODELO = "clase_modelo";
    public static final String PARTE = "parte";
    public static final String DESCRIPCION = "descripcion";

    static final int INVENTARIO = 1;
    static final int INVENTARIO_ID = 2;

    private static HashMap<String,String> INVENTARIO_PROJECTION_MAP;
    public static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME,"inventario",INVENTARIO);
        uriMatcher.addURI(PROVIDER_NAME,"inventario/#",INVENTARIO_ID);
    }

    public InventarioProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)){
            case INVENTARIO:
                count = db.delete(INVENTARIO_TABLE_NAME,selection,selectionArgs);
                break;
            case INVENTARIO_ID:
                String id = uri.getPathSegments().get(1);
                count = db.delete(INVENTARIO_TABLE_NAME,
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
        long rowID = super.db.insert(INVENTARIO_TABLE_NAME,"",values);
        if (rowID > 0){
            Uri _uri = ContentUris.withAppendedId(CONTENT_URL,rowID);
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
        return (db == null) ? false : true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(INVENTARIO_TABLE_NAME);
        switch (uriMatcher.match(uri)){
            case INVENTARIO:
                qb.setProjectionMap(INVENTARIO_PROJECTION_MAP);
                break;
            case INVENTARIO_ID:
                qb.appendWhere(ID + " = " + uri.getPathSegments().get(1));
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
            case INVENTARIO:
                count = db.update(INVENTARIO_TABLE_NAME,values,selection,selectionArgs);
                break;
            case INVENTARIO_ID:
                count = db.update(
                        INVENTARIO_TABLE_NAME,
                        values,
                        ID + " = " + uri.getPathSegments().get(1) +
                                (!TextUtils.isEmpty(selection) ?
                                  " AND (" +selection + ')' : ""),
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
