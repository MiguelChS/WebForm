package sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.database.Cursor;
import android.databinding.tool.util.L;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import app.AppController;
import mc185249.webforms.Api;
import mc185249.webforms.ClientsContentProvider;
import mc185249.webforms.ScrollingActivity;
import mc185249.webforms.WebFormsPreferencesManager;
import models.Cliente;

/**
 * Created by jn185090 on 5/27/2016.
 */
public class ClientsSyncAdapter extends AbstractThreadedSyncAdapter {

    public static final String CLIENT_SYNC_FINISHED = "CLIENT_SYNC_FINISHED";
    ContentResolver mContentResolver;
    ClientsContentProvider clientsContentProvider;
    Context mContext;
    public ClientsSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContext = context;
        mContentResolver = context.getContentResolver();
        clientsContentProvider = new ClientsContentProvider();
    }

/**/

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, final SyncResult syncResult) {
        doSync();
    }

    public void doSync(){
        Log.v("NCR","sincronizando clientes...");
        StringBuilder stringBuilder = new StringBuilder((Api.SERVER + Api.CLIENTS + "/?CSR=@"));
        int index = stringBuilder.indexOf("@");
        stringBuilder.replace(index,(index + 1),new WebFormsPreferencesManager(mContext).getCsrCode());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                stringBuilder.toString(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ArrayList<Cliente> localClients = new ArrayList<>();
                        ArrayList<Cliente> remoteClientes = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++){
                            try {
                                JSONObject json = response.getJSONObject(i);
                                remoteClientes.add(
                                        new Cliente(
                                                json.getString("pais"),
                                                json.getString("nombre"),
                                                json.getString("numero")
                                        )
                                );
                            } catch (JSONException e) {
                                AppController.getInstance().notify(
                                        "Parser error - Sync Clientes",
                                        e.getMessage()
                                );
                            }

                        }


                        if (remoteClientes.size() > 0){
                            mContentResolver.delete(
                                    ClientsContentProvider.CONTENT_URI,
                                    null,
                                    null
                            );
                            ContentValues contentValues;
                            for (Cliente cliente:
                                    remoteClientes) {
                                contentValues = new ContentValues();
                                contentValues.put(ClientsContentProvider.NOMBRE,cliente.getNombre());
                                contentValues.put(ClientsContentProvider.NUMERO,cliente.getNumero());
                                contentValues.put(ClientsContentProvider.PAIS,cliente.getPais());

                                Uri uri = mContentResolver.insert(
                                        ClientsContentProvider.CONTENT_URI,
                                        contentValues
                                );


                            }
                        }

                        Log.v("NCR","Clientes sincronizados");


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e("NCR","sync clientes",error);
                        AppController.getInstance().notify(
                                "Error Sincronizar Clientes",
                                "No se pudo sincronizar los clientes"
                        );
                    }
                }
        );

        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
    }
}
