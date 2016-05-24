package models;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import com.example.mc185249.webforms.R;
import com.example.mc185249.webforms.WebFormsActivity;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.PasswordAuthentication;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;



import javax.sql.DataSource;

import app.AppController;
import mc185249.webforms.Api;
import mc185249.webforms.WebFormsPreferencesManager;


/**
 * Created by mc185249 on 3/29/2016.
 */

public class EmailSender extends Email  {
    Context mContext;

     public EmailSender(Context context) {
        this.mContext = context;
    }

    public void send() throws JSONException {

        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("hasAttachment",hasAttachment);
        jsonObject.accumulate("body",body);
        jsonObject.accumulate("recipients",recipients);
        jsonObject.accumulate("sender",sender);
        jsonObject.accumulate("files",files);
        jsonObject.accumulate("form",form);

        JsonObjectRequest Request = new
                JsonObjectRequest(
                com.android.volley.Request.Method.POST,
                (Api.SERVER + Api.EXCHANGE),
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        return;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        AlertDialog.Builder alertDialogBuilder =
                                new AlertDialog.Builder(mContext,R.style.alertDialog);
                        alertDialogBuilder.create();
                        alertDialogBuilder.setMessage(error.getMessage());
                        alertDialogBuilder.setTitle("Error");
                        alertDialogBuilder.show();
                    }
                }
        ){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params =
                                new HashMap<>();
                        params.put()
                    }
                };
        AppController.getInstance().addToRequestQueue(Request);
    }


    public void setPasswordAuthentication(String account, String passwd)
    {
        Sender sender = new Sender();
        sender.passwd = passwd;
        sender.emailAddress = account;
        sender.name = account.split("@")[0];
        sender.CSRCode = new WebFormsPreferencesManager(mContext).getCsrCode();
    }


}
