package sync;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.util.Map;

/**
 * Created by jn185090 on 6/21/2016.
 */
public class InputStreamVolleyRequest extends Request<byte[]> {

    private final Response.Listener<byte[]> mListener;
    private Map<String,String> mParams;

    public Map<String,String>responseHeader;

    public InputStreamVolleyRequest(int method, String url, Response.ErrorListener listener, Response.Listener<byte[]> mListener, Map<String, String> mParams) {
        super(method, url, listener);
        setShouldCache(false);
        this.mListener = mListener;
        this.mParams = mParams;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return getmParams();
    }

    @Override
    protected Response<byte[]> parseNetworkResponse(NetworkResponse response) {
        responseHeader = response.headers;
        return Response.success(response.data, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(byte[] response) {
        mListener.onResponse(response);
    }

    public Map<String, String> getmParams() {
        return mParams;
    }

    public void setmParams(Map<String, String> mParams) {
        this.mParams = mParams;
    }
}
