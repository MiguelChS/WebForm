package sync;

import android.accounts.Account;
import android.app.DownloadManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;

import app.AppController;
import mc185249.webforms.Api;
import mc185249.webforms.ContactsProvider;
import mc185249.webforms.InventarioProvider;
import mc185249.webforms.WebFormsPreferencesManager;
import microsoft.exchange.webservices.data.misc.IFunctions;
import models.Elemento;

/**
 * Created by jn185090 on 6/21/2016.
 */
public class SynInventarioPartes extends AbstractThreadedSyncAdapter {

    Context mContext;
    public static final String PARTES_SYNC = "PARTES_SYNC";
    Elemento elemento = null;

    public SynInventarioPartes(Context context){
        super(context,true,true);
        mContext = context;

    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.v("NCR","sincronizando inventario");
        String csrCode = new WebFormsPreferencesManager(mContext).getCsrCode();
        String pais = csrCode.substring(0,2);
        InputStreamVolleyRequest request = new InputStreamVolleyRequest(
                Request.Method.GET,
                (Api.SERVER_PARTES + pais),
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();

                    }
                },
                new Response.Listener<byte[]>() {
                    @Override
                    public void onResponse(byte[] response) {
                        try {
                            if (response != null){

                                String data = new String(response,"UTF-8");
                                Log.v("NCR",data);
                                String[] lines = data.split("\\n");
                                Log.v("NCR","Lines[0]: " + lines[0]);
                                Log.v("NCR","Partes para insertar: " + lines.length);
                                for (String line:
                                     lines) {

                                    String[] values = line.split(";");
                                    Log.v("NCR","line:" + line);
                                    if (values == null
                                            || values.length < 4)
                                        continue;
                                    Log.v("NCR","values:" + values);
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put(InventarioProvider.CLASE,values[0]);
                                    contentValues.put(InventarioProvider.CLASEMODELO,values[1]);
                                    contentValues.put(InventarioProvider.DESCRIPCION,values[3]);
                                    contentValues.put(InventarioProvider.PARTE,values[2]);

                                    Uri uri = mContext.getContentResolver().insert(
                                            InventarioProvider.CONTENT_URL,contentValues
                                    );
                                    Long.valueOf(uri.getLastPathSegment());

                                }
                                getContext().getContentResolver()
                                        .notifyChange(InventarioProvider.CONTENT_URL, null, false);
                            }
                        }catch (Exception e){
                            Log.e("NCR","Unable to download file",e);
                            e.printStackTrace();
                        }
                    }
                },
                null
        );

        AppController.getInstance().addToRequestQueue(request,"Inventario Partes");
    }
}
