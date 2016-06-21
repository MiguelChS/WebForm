package sync;

import android.accounts.Account;
import android.app.DownloadManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.io.FileOutputStream;

import app.AppController;
import mc185249.webforms.Api;
import mc185249.webforms.WebFormsPreferencesManager;

/**
 * Created by jn185090 on 6/21/2016.
 */
public class SynInventarioPartes extends AbstractThreadedSyncAdapter {

    Context mContext;

    public SynInventarioPartes(Context context){
        super(context,true,true);
        mContext = context;

    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
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
                                FileOutputStream outputStream;
                                String name = "InventarioPartes.csv";

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
