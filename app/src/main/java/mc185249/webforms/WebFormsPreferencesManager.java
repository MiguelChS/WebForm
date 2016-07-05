package mc185249.webforms;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.BoringLayout;

import microsoft.exchange.webservices.data.core.exception.misc.ArgumentException;

/**
 * Created by jn185090 on 5/20/2016.
 */
public class WebFormsPreferencesManager {
    SharedPreferences pref;
    Context mContext;
    SharedPreferences.Editor editor;
    int PRIVATE_MODE = 0;
    public static  final String CSR_CODE = "CSRCODE";
    public static final String PASSWD = "PASSWD";
    public static final String ACCOUNT_NAME = "ACCOUNT_NAME";
    public static final String SYNCRONIZES_CLIENTES = "SYNCRONIZES_CLIENTES";
    public static final String SYNCRONIZES_CONTACTOS = "SYNCRONIZES_CONTACTOS";
    public static final String SYNCRONIZES_PARTES = "SYNCRONIZES_PARTES";
    public static final String IS_FIRST_TIME = "FIRST_TIME";

    public WebFormsPreferencesManager(Context mContext) {
        this.mContext = mContext;
        pref = PreferenceManager.getDefaultSharedPreferences(mContext);
        editor = pref.edit();
    }

    public String getUserName(){
        return pref.getString(ACCOUNT_NAME,null);
    }
    public Boolean getFirstTime(){
        return pref.getBoolean(IS_FIRST_TIME,true);
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

    /**
     * Inserta un objeto de tipo clave valor dentro del shared preferences de la
     * aplicacion.
     * @param key
     * clave
     * @param obj : solo admite valores String e Integer.
     * @exception IllegalArgumentException if obj is null
     */
    public void put(String key,Object obj ){

        if (obj == null){
            throw new IllegalArgumentException("obj no puede estar vacio.");

        }

        if (obj instanceof String){
            editor.putString(key,obj.toString());
            editor.commit();
        }
        if (obj instanceof Integer){
            editor.putInt(key,Integer.parseInt(obj.toString()));
            editor.commit();
        }

        if (obj instanceof Boolean){
            editor.putBoolean(key,Boolean.parseBoolean(obj.toString()));
            editor.commit();
        }


    }

    /**
     * devuelve el valor almacenado para SYNCRONIZES_CLIENTES en el shared Preferences
     */
    public boolean getSyncClientes(){
        return pref.getBoolean(SYNCRONIZES_CLIENTES,false);
    }
    public boolean getSyncContactos(){
        return pref.getBoolean(SYNCRONIZES_CONTACTOS,false);
    }
    public boolean getSyncPartes(){
        return pref.getBoolean(SYNCRONIZES_PARTES,false);
    }
}
