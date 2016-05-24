package mc185249.webforms;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by jn185090 on 5/20/2016.
 */
public class WebFormsPreferencesManager {
    SharedPreferences pref;
    Context mContext;
    SharedPreferences.Editor editor;
    int PRIVATE_MODE = 0;
    private static  final String CSR_CODE = "CSRCODE";
    private static final String PASSWD = "PASSWD";
    private static final String ACCOUNT_NAME = "ACCOUNT_NAME";

    public WebFormsPreferencesManager(Context mContext) {
        this.mContext = mContext;
        pref = PreferenceManager.getDefaultSharedPreferences(mContext);
        editor = pref.edit();
    }

    public String getUserName(){
        return pref.getString(ACCOUNT_NAME,null);
    }

    public String getPasswd(){
        return pref.getString(PASSWD,null);
    }
    public String getCsrCode(){
        return pref.getString(CSR_CODE,null);
    }

    public void save(String user,String passwd, String csrCode){
        editor.putString(CSR_CODE,csrCode);
        editor.putString(ACCOUNT_NAME,user);
        editor.putString(PASSWD,passwd);
        editor.commit();
    }
}
