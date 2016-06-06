package sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

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
        super(context, autoInitialize);
        initialize(context);
    }

    public ContactsSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
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
                                        "Cast Error",e.getMessage(),
                                        "SYNC", new WebFormsPreferencesManager(mContext).getUserName(),
                                        "Contacts Sync Adapter"
                                );
                            }
                        }

                        Uri contactos = Uri.parse(ContactsProvider.URL);
                        Cursor cursor = mResolver.query(
                                contactos,
                                null,
                                null,
                                null,null
                        );
                        if (cursor != null){
                            while (cursor.moveToNext()){
                                localContacts.add(
                                        Contacto.fromCursor(cursor)
                                );
                            }
                        }

                        cursor.close();
                        ArrayList<Contacto> contactosToLocal = new ArrayList<>();
                        for (Contacto contacto:
                             remoteContacts) {
                            if (localContacts == null
                                    || localContacts.size() == 0){
                                contactosToLocal.add(contacto);
                                mResolver.delete(
                                        ContactsProvider.CONTENT_URI,
                                        null,
                                        null
                                );
                            }
                        }

                        if (contactosToLocal.size() > 0){
                            ContentValues contentValues;
                            for (Contacto contacto:
                                 contactosToLocal) {
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

                                AppController.getInstance().notify(
                                        "Contactos Actualizados",
                                        contactosToLocal.size() + " actualizados",
                                        "SYNC CONTACTOS",
                                        new WebFormsPreferencesManager(mContext).getUserName(),
                                        "NCR Contactos"
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
                                "SYNC CONTACTOS", new WebFormsPreferencesManager(mContext).getUserName(),
                                "Sync Adapter - Contactos"
                        );
                    }
                }
        );

        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
    }
}
