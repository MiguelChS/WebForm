package app;

import android.app.AlarmManager;
import android.app.Application;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Application;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.Calendar;

import eu.inmite.android.lib.validations.form.validators.ValidatorFactory;
import mc185249.webforms.AlarmReceiver;
import mc185249.webforms.R;
import mc185249.webforms.WebFormsPreferencesManager;

/**
 * Created by jn185090 on 5/20/2016.
 */
public class AppController extends Application {
    private static AppController mInstance;
    private static String TAG = "WebForms";
    private RequestQueue mRequestQueue;
    NotificationManagerCompat notificationManagerCompat;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;

    //region sync adapter
    public static final String AUTHORITY_CLIENTS = "mc185249.webforms.ClientsContentProvider";
    public static final String AUTHORITY_CONTACTS = "mc185249.webforms.ContactsProvider";
    public static final String AUTHORITY_INVENTARIO = "mc185249.webforms.InventarioProvider";
    public static final String ACCOUNT_TYPE = "com.webforms";
    public static String ACCOUNT;
    public static Account mAccount;
    public static final long SECONDS_PER_MINUTE = 60L;
    public static final long SYNC_INTERVAL_IN_MINUTES = 24L * 60L;
    public static final long SYNC_INTERVAL = SYNC_INTERVAL_IN_MINUTES * SECONDS_PER_MINUTE;
    //endregion
    public static synchronized AppController getInstance(){
        return mInstance;
    }

    static {
        ValidatorFactory.registerValidatorClasses();
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        notificationManagerCompat =
                NotificationManagerCompat.from(this);

    }

    /**
     * Inicia la sincronizacion
     *
     */
    public void initializeSync() {
        mInstance = this;
        notificationManagerCompat =
                NotificationManagerCompat.from(this);
        ACCOUNT = "dummy";
        mAccount = createSyncAccount(this);

       ContentResolver.setIsSyncable(mAccount, AUTHORITY_CLIENTS,1);
         ContentResolver.setSyncAutomatically(
                mAccount,
                AUTHORITY_CLIENTS,
                true
        );
        ContentResolver.addPeriodicSync(
                mAccount,
                AUTHORITY_CLIENTS,
                Bundle.EMPTY,
                SYNC_INTERVAL
        );

        ContentResolver.setIsSyncable(mAccount, AUTHORITY_CONTACTS,1);
         ContentResolver.setSyncAutomatically(
                mAccount,
                AUTHORITY_CONTACTS,
                true
        );
        ContentResolver.addPeriodicSync(
                mAccount,
                AUTHORITY_CONTACTS,
                Bundle.EMPTY,
                SYNC_INTERVAL
        );

        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_MANUAL,true
        );
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_EXPEDITED,true
        );
        ContentResolver.setIsSyncable(mAccount, AUTHORITY_INVENTARIO,1);
        ContentResolver.setSyncAutomatically(
                mAccount,
                AUTHORITY_INVENTARIO,
                true
        );
        ContentResolver.requestSync(
                AppController.mAccount,AppController.AUTHORITY_INVENTARIO,settingsBundle
        );


        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY,2);
        alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this,0,intent,0);


        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),
                1000 * 20 * 60, pendingIntent);
    }
    public static Account createSyncAccount(Context context) {

        Account newAccount = new Account(
                ACCOUNT,ACCOUNT_TYPE
        );
        AccountManager accountManager =
                (AccountManager)context.getSystemService(
                        ACCOUNT_SERVICE
                );

        if (accountManager.addAccountExplicitly(newAccount,null,null)){
            return newAccount;
        }else{
            return accountManager.getAccounts()[0];
        }

    }

    public RequestQueue getmRequestQueue(){
        if (mRequestQueue == null)
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request,String tag){
        request.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getmRequestQueue().add(request);
    }

    public <T> void addToRequestQueue(Request<T> request){
        request.setTag(TAG);
        getmRequestQueue().add(request);
    }

    public void cancelPendingRequests(Object tag){
        if (mRequestQueue != null)
            mRequestQueue.cancelAll(tag);
    }

    public String getAppVersion() throws PackageManager.NameNotFoundException {
        return this.getPackageManager().getPackageInfo(
                        this.getPackageName()
                        ,0
                ).versionName;

    }

    public boolean checkCredentials(){
        WebFormsPreferencesManager pref = new WebFormsPreferencesManager(this);
        return (pref.getUserName() != null && pref.getPasswd() != null);
    }

    /**
     * CREA UNA NOTIFICACION SIMPLE
     * @param title Titulo de la notificacion
     * @param content Contenido de la notificacion
     */
    public void notify(String title, String content){
        Notification notification = new NotificationCompat
                .Builder(this)
                .setSmallIcon(R.drawable.ncr)
                .setContentTitle(title)
                .setContentText(content)
                .build();

        notificationManagerCompat.notify(1, notification);
    }

    /**
     * Inicia sync de clientes y contactos on demand
     */
    public void onDemandSyncClientesContactos(){
        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_MANUAL,true
        );
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_EXPEDITED,true
        );
        ContentResolver.setIsSyncable(mAccount, AUTHORITY_INVENTARIO,1);
        ContentResolver.setSyncAutomatically(
                mAccount,
                AUTHORITY_INVENTARIO,
                true
        );
        ContentResolver.requestSync(
                AppController.mAccount,AppController.AUTHORITY_INVENTARIO,settingsBundle
        );
    }


}
