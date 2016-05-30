package sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.databinding.tool.util.L;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.mc185249.webforms.ClientsContentProvider;
import com.example.mc185249.webforms.WebFormsProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import app.AppController;
import mc185249.webforms.WebFormsPreferencesManager;
import models.Cliente;

/**
 * Created by jn185090 on 5/27/2016.
 */
public class ClientsSyncAdapter extends AbstractThreadedSyncAdapter {

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
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                "http://sarbue8000:8080/api/clients",
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
                                        "Cast Error",e.getMessage(),
                                        "SYNC", new WebFormsPreferencesManager(mContext).getUserName(),
                                        "Sync Adapter"
                                );
                            }

                        }

                        Uri clientes = Uri.parse(ClientsContentProvider.URL);
                        Cursor cursor = mContentResolver.query(
                                clientes,
                                null,
                                null,
                                null,null
                        );

                        if (cursor != null){
                            while (cursor.moveToNext()){
                                localClients.add(Cliente.fromCursor(cursor));
                            }
                        }
                        cursor.close();
                        //comprueba que clientes no estan en la base local

                        ArrayList<Cliente> clientesToLocal = new ArrayList<>();
                        for (Cliente cliente:
                             remoteClientes) {
                            if (localClients == null
                                    || localClients.size() == 0)
                                clientesToLocal.add(cliente);
                                mContentResolver.delete(
                                        ClientsContentProvider.CONTENT_URI,
                                        null,
                                        null
                                );
                        }

                        if (clientesToLocal.size() > 0){
                            ContentValues contentValues;
                            for (Cliente cliente:
                                 clientesToLocal) {

                                contentValues = new ContentValues();
                                contentValues.put(ClientsContentProvider.NOMBRE,cliente.getNombre());
                                contentValues.put(ClientsContentProvider.NUMERO,cliente.getNumero());
                                contentValues.put(ClientsContentProvider.PAIS,cliente.getPais());

                                Uri uri = mContentResolver.insert(
                                        ClientsContentProvider.CONTENT_URI,
                                        contentValues
                                );

                                AppController.getInstance().notify(
                                        "Clientes Actualizados",
                                        clientesToLocal.size() + " actualizados",
                                        "SYNC",
                                        new WebFormsPreferencesManager(mContext).getUserName(),
                                        "NCR Clientes"
                                );
                            }

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        AppController.getInstance().notify(
                                "Request Error",error.getMessage(),
                                "SYNC", new WebFormsPreferencesManager(mContext).getUserName(),
                                "Sync Adapter"
                        );
                    }
                }
        );

        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
    }
}
