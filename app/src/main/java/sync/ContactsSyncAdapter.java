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
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.AppController;
import mc185249.webforms.Api;
import mc185249.webforms.ContactsProvider;
import mc185249.webforms.ScrollingActivity;
import mc185249.webforms.WebFormsPreferencesManager;
import models.Contacto;

/**
 * Created by jn185090 on 5/27/2016.
 */
public class ContactsSyncAdapter extends AbstractThreadedSyncAdapter {

    ContentResolver mResolver;
    Context mContext;
    ContactsProvider contactsProvider;


    public ContactsSyncAdapter(Context context, boolean autoInitialize) {
        super(context, true);
        initialize(context);
    }

    public ContactsSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, true);
        initialize(context);
    }

    private void initialize(Context context){
        mContext = context;
        mResolver = context.getContentResolver();
        contactsProvider = new ContactsProvider();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {

        StringBuilder stringBuilder = new StringBuilder((Api.SERVER + Api.CONTACTS +
                "/?CSR=@" ));
        int index = stringBuilder.indexOf("@");
        stringBuilder.replace(
                index,
                (index + 1),
                new WebFormsPreferencesManager(mContext).getCsrCode()
        );
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                stringBuilder.toString(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ArrayList<Contacto> localContacts = new ArrayList<>();
                        ArrayList<Contacto> remoteContacts = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++){
                            try {
                                JSONObject json = response.getJSONObject(i);
                                remoteContacts.add(
                                        new Contacto(
                                              json.getString("direcciones"),
                                              json.getString("nombres"),
                                              json.getString("pais"),
                                              json.getString("numero"))
                                );
                            } catch (JSONException e) {
                                AppController.getInstance().notify(
                                        "Error Sync Contactos",
                                        e.getMessage()
                                );
                            }
                        }
                        if (remoteContacts.size() > 0){
                            mResolver.delete(
                                    ContactsProvider.CONTENT_URI,
                                    null,
                                    null);
                            ContentValues contentValues;
                            for (Contacto contacto:
                                    remoteContacts) {

                                contentValues = new ContentValues();
                                contentValues.put(ContactsProvider.DIRECIONES,
                                        contacto.getDirecciones());
                                contentValues.put(ContactsProvider.NOMBRES,
                                        contacto.getNombre());
                                contentValues.put(ContactsProvider.PAIS,
                                        contacto.getPais());
                                contentValues.put(ContactsProvider.NUMERO,
                                        contacto.getNumero());

                                Uri uri = mResolver.insert(
                                        ContactsProvider.CONTENT_URI,
                                        contentValues
                                );
                            }
                        }
                        Log.v("NCR","Contactos Sincronizados");
                        sync.SyncResult.STATE_CONTACTOS = sync.SyncResult.SUCCESS;
                        ScrollingActivity.mObserver.onSuccess();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        sync.SyncResult.STATE_CONTACTOS = sync.SyncResult.ERROR;
                        Log.e("NCR","error sync contactos",error);
                        AppController.getInstance().notify(
                                "Error Sync Contactos",
                                "No se pudo sincronizar contactos"
                        );
                    }
                }
        );

        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
    }
}
