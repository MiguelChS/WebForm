package connectivity;

import android.app.NotificationManager;
import android.databinding.tool.solver.ExecutionBranch;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;


import org.json.JSONException;

import models.EmailSender;

/**
 * Created by mc185249 on 3/31/2016.
 */
public class EmailTask extends AsyncTask<EmailSender,Integer,Void> {

    private Exception exception = null;

    @Override
    protected Void doInBackground(EmailSender... params) {
        EmailSender emailSender = params[0];

        try {
            emailSender.send();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
     @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }
}
