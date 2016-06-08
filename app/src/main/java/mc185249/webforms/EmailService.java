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
import java.util.ArrayList;
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
        ArrayList<EmailSender> emailSenders = Email.readEmails(this);
        if (emailSenders != null &&
                !emailSenders.isEmpty()){
            for (EmailSender emailSender:
                 emailSenders) {
                new EmailTask().execute(emailSender);
            }
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



