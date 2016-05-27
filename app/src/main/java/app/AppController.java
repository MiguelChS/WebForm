package app;

import android.app.Application;
import android.app.Notification;
import android.content.pm.PackageManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import mc185249.webforms.WebFormsPreferencesManager;

/**
 * Created by jn185090 on 5/20/2016.
 */
public class AppController extends Application {
    private static AppController mInstance;
    private static String TAG = "WebForms";
    private RequestQueue mRequestQueue;
    NotificationManagerCompat notificationManagerCompat;
    public static synchronized AppController getInstance(){
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        notificationManagerCompat =
                NotificationManagerCompat.from(this);
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

    public void notify(String title, String content,String group,String summaryText,String bigContentTitle){
        Notification notification = new NotificationCompat
                .Builder(this)
                .setContentTitle(title)
                .setContentText(content)
                .setStyle(new NotificationCompat.InboxStyle()
                        .setBigContentTitle(bigContentTitle)
                        .setSummaryText(summaryText))
                .setSmallIcon(android.support.design.R.drawable.notification_template_icon_bg)
                .setGroup(group)
                .setGroupSummary(true)
                .build();

        notificationManagerCompat.notify(1, notification);
    }
}
