package app;

import android.app.Application;
import android.content.pm.PackageManager;
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
    public static synchronized AppController getInstance(){
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
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
}
