package models;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.org.apache.http.HttpResponse;
import android.org.apache.http.NameValuePair;
import android.org.apache.http.client.ClientProtocolException;
import android.org.apache.http.client.HttpClient;
import android.org.apache.http.client.entity.UrlEncodedFormEntity;
import android.org.apache.http.client.methods.HttpPost;
import android.org.apache.http.entity.BasicHttpEntity;
import android.org.apache.http.entity.StringEntity;
import android.org.apache.http.impl.client.DefaultHttpClient;
import android.org.apache.http.message.BasicNameValuePair;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.MediaStore.Files;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.PasswordAuthentication;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;


import javax.sql.DataSource;

import app.AppController;
import mc185249.webforms.Api;
import mc185249.webforms.EmailsProvider;
import mc185249.webforms.WebFormsPreferencesManager;


/**
 * Created by mc185249 on 3/29/2016.
 */

public class EmailSender extends Email {
    Context mContext;

    public EmailSender(Context context) {
        this.mContext = context;
    }

    public void send() throws JSONException {

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(Api.SERVER + Api.EXCHANGE);

        HashMap<String, Object> cadena_json = new HashMap<>();
        cadena_json.put("hasAttachment", hasAttachment);
        cadena_json.put("body", Base64.encodeToString(body.toString().getBytes(), Base64.NO_WRAP));
        cadena_json.put("recipients", recipients);
        cadena_json.put("sender", sender);
        cadena_json.put("files", files);
        cadena_json.put("form", form);
        cadena_json.put("subject", subject);
        Gson gson = new Gson();
        String json = gson.toJson(cadena_json);
        try {
            httpPost.setEntity(new StringEntity(
                    json
            ));
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            HttpResponse response = httpClient.execute(httpPost);
            int status_code = response.getStatusLine().getStatusCode();
            InputStream inputStream =
                    response.getEntity().getContent();
            if (inputStream != null) {
                String result =
                        convertInputStreamToString(inputStream);
                if (status_code == 200
                        && Integer.parseInt(result) > 0) {
                    ContentValues values = new ContentValues();
                    values.put(EmailsProvider.CURRENT_STATE, 2);
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    values.put(EmailsProvider.FECHA, dateFormat.format(new java.util.Date()));
                    int mRowsUpdated = mContext.getContentResolver().update(
                            EmailsProvider.CONTENT_URI, values, EmailsProvider.ID + " = ?",
                            new String[]{
                                    String.valueOf(getId())
                            }


                    );
                }
                Log.v("NCR", result);
            } else {
                ContentValues values = new ContentValues();
                values.put(EmailsProvider.CURRENT_STATE, 1);
                int mRowsUpdated = mContext.getContentResolver().update(
                        EmailsProvider.CONTENT_URI, values, EmailsProvider.ID + " = ?",
                        new String[]{
                                String.valueOf(getId())
                        }
                );
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    public void setPasswordAuthentication(String account, String passwd) {
        Sender sender = new Sender();
        sender.passwd = "";
        sender.emailAddress = account;
        sender.name = account.split("@")[0];
        sender.CSRCode = new WebFormsPreferencesManager(mContext).getCsrCode();
    }


}
