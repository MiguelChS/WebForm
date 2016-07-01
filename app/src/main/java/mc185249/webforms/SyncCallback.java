package mc185249.webforms;

import android.support.v7.app.AppCompatActivity;

import java.io.Serializable;

/**
 * Created by joaquin on 29/06/16.
 */
public  class SyncCallback<T>  implements Serializable  {
    private Class<T> data_class;

    public interface Listener<T>{
        public void onSuccess(T response);
    }

    public interface ErrorListener{
        public void onErrorListener(CustomException excepton);
    }

    public static class CustomException extends Exception{
        String detailMessage;
        Throwable throwable;
        public CustomException(String detailMessage) {
            super(detailMessage);
            this.detailMessage = detailMessage;
        }

        public CustomException(String detailMessage, Throwable throwable) {
            super(detailMessage, throwable);
            this.detailMessage = detailMessage;
            this.throwable = throwable;
        }

        public CustomException(Throwable throwable) {
            super(throwable);
            this.throwable = throwable;
        }
    }
}
