package connectivity;

import android.os.AsyncTask;
import android.util.Log;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by mc185249 on 3/23/2016.
 */
public class CsvTask extends AsyncTask<String, Void, String> {

    String url = null;
    boolean hasCon = false;


    public CsvTask(boolean hasCon){
        this.hasCon = hasCon;
    }

    @Override
    protected String doInBackground(String... params) {
        Log.v("CSV","tiene conexion: " + hasCon);
        if(hasCon){
            this.url = params[0];
            return  downloadUrl();
        }else{
            return readFromLocalResource();
        }

    }

    private String readFromLocalResource() {

        return null;
    }

    private String readIt(InputStream stream, int len) {
        Reader reader = null;
        try {
            reader = new InputStreamReader(stream, "UTF-8");
            char[] buffer = new char[len];
            reader.read(buffer);
            Log.v("CSV","Sin problemas");
            return new String(buffer);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {

    }

    private String downloadUrl(){
        InputStream is = null;
        int len = 500;
        try{
            URL Url = new URL(this.url);
            Log.v("CSV",url);
            HttpURLConnection conn = (HttpURLConnection) Url.openConnection();
            //conn.setReadTimeout(90000);
            //conn.setConnectTimeout(90000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            int response = conn.getResponseCode();
            Log.v("CSV","response code: " + response);
            is = conn.getInputStream();

            //  CONVERT INPUTSTREAM INTO A STRING

            String contentAsString = readIt(is, len);
            return contentAsString;

        } catch (MalformedURLException e) {
            Log.e("CSV",e.getMessage());
        } catch (ProtocolException e) {
            Log.e("CSV",e.getMessage());
        } catch (IOException e) {
            Log.e("CSV",e.getMessage());
        }finally{
            if (is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
