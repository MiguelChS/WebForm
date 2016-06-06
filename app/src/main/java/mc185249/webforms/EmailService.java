package mc185249.webforms;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.*;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Calendar;
import java.util.Date;


import connectivity.EmailTask;
import mc185249.webforms.AttachementProvider;
import mc185249.webforms.EmailsProvider;
import mc185249.webforms.LogProvider;
import mc185249.webforms.WebFormsPreferencesManager;
import models.Email;
import models.EmailSender;
import models.Sender;
import models.WebFormsLogModel;

/**
 * Created by mc185249 on 4/5/2016.
 */
public class EmailService extends Service {

    int CustomEmailID = 0;
    String activity = "";
    File f = null;

    @Override
    public void onCreate() {

    }

    @Nullable
    @Override

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                readAndSendEmails();
            }
        }).start();


        return START_NOT_STICKY;
    }



    private void readAndSendEmails(){
        Log.v("NCR","service running..");
        String URL = "content://mc185249.webforms.EmailsProvider/emails";
        Uri emails = Uri.parse(URL);
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(emails,null,null,null,null);

        if (cursor != null
                && cursor.moveToFirst()){
            do{
                WebFormsPreferencesManager preferencesManager =
                        new WebFormsPreferencesManager(this);
                String account =
                    preferencesManager.getUserName();
                String passwd =
                        preferencesManager.getPasswd();
                if ((account != null && passwd != null)
                        && !(account.isEmpty())
                        && !(passwd.isEmpty())){
                    EmailSender emailSender = null;
                    emailSender = new EmailSender(this);

                    activity = cursor.getString(cursor.getColumnIndex(EmailsProvider.ACTIVITY));
                    emailSender.setSubject(cursor.getString(cursor.getColumnIndex(mc185249.webforms.EmailsProvider.SUBJECT)));
                    emailSender.setBody(cursor.getString(cursor.getColumnIndex(mc185249.webforms.EmailsProvider.BODY)));
                    emailSender.setRecipients(new String[]{cursor.getString(cursor.getColumnIndex(mc185249.webforms.EmailsProvider.RECIPIENT))});
                    emailSender.setFrom(cursor.getString(cursor.getColumnIndex(mc185249.webforms.EmailsProvider.FROM)));
                    emailSender.setPasswordAuthentication(account.trim(), passwd.trim());
                    WebFormsPreferencesManager pref =
                            new WebFormsPreferencesManager(this);
                    emailSender.setSender(
                            new Sender(
                                    pref.getPasswd(),
                                    (pref.getUserName()),
                                    pref.getUserName(),
                                    pref.getCsrCode()
                            )
                    );

                    CustomEmailID = cursor.
                            getInt(cursor.getColumnIndex(EmailsProvider.ID));

                    //region log
                    LogProvider logProvider =
                            new LogProvider(this);
                    WebFormsLogModel[] logs = logProvider.query(null,new String[]{
                            String.valueOf(CustomEmailID)
                    },(LogProvider.emailID + " = ?"));

                    if (logs.length > 0)
                        emailSender.setForm(logs[0]);

                    //endregion
                    //region adjuntos
                    Uri attachment_files = Uri.parse(AttachementProvider.URL);
                    ContentResolver contentResolver1 = getContentResolver();
                    Cursor mCursor = contentResolver1.query(
                            attachment_files,null, mc185249.webforms.AttachementProvider.EMAIL_ID + " = ?",
                            new String[]{
                                    String.valueOf(cursor.getInt(cursor.getColumnIndex(mc185249.webforms.EmailsProvider.ID)))
                            },null
                    );

                    if(mCursor.moveToFirst()){
                        do{
                            byte[] encodedFile = mCursor
                                    .getBlob
                                            (mCursor
                                                    .getColumnIndex(
                                                            AttachementProvider.BLOB));


                            emailSender
                                    .Attach(new models
                                            .File(
                                                ("WebFormsIMG_" + Calendar.getInstance()
                                                        .get(Calendar.SECOND)+ "_"+
                                                    new WebFormsPreferencesManager(this)
                                                            .getCsrCode())
                                                ,Base64.encodeToString(encodedFile,0)
                                                , BitmapFactory
                                                    .decodeByteArray(
                                                           encodedFile,0,encodedFile.length
                                                    )
                                                ));
                            new EmailTask().execute(emailSender);

                        }while(mCursor.moveToNext());
                    }//endregion
                    else{
                        new EmailTask().execute(emailSender);
                    }

                }


            }while (cursor.moveToNext());
        }
    }

    private boolean isDeviceOnline(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null){
            return true;
        }

        return false;
    }
}



