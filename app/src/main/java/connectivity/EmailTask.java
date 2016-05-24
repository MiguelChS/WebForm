package connectivity;

import android.app.NotificationManager;
import android.databinding.tool.solver.ExecutionBranch;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;


import models.EmailSender;

/**
 * Created by mc185249 on 3/31/2016.
 */
public class EmailTask extends AsyncTask<EmailSender,Integer,Boolean> {

    public interface  AsyncResponse {
        void processFinish(Boolean output,Exception e);
    }

    private Exception exception = null;
    public AsyncResponse delegate = null;

    public EmailTask(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    @Override
    protected Boolean doInBackground(EmailSender... params) {
        EmailSender emailSender = params[0];
        try {
            emailSender.send();
        }
         catch (Exception e) {
             exception = e;
           return false;
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        this.delegate.processFinish(aBoolean,exception);
    }




    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }
}
