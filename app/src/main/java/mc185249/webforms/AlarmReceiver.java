package mc185249.webforms;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import app.AppController;

/**
 * Created by joaquin on 04/07/16.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_MANUAL,true
        );
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_EXPEDITED,true
        );

        ContentResolver.requestSync(
                AppController.mAccount,AppController.AUTHORITY_INVENTARIO,settingsBundle
        );
    }
}
