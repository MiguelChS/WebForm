package sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by jn185090 on 5/29/2016.
 */
public class ContactsSyncService extends Service {

    private static ContactsSyncAdapter syncAdapter = null;
    public static final Object syncAdapterLock = new Object();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return syncAdapter.getSyncAdapterBinder();
    }

    @Override
    public void onCreate() {
        synchronized (syncAdapterLock){
            if (syncAdapter == null){
                syncAdapter = new ContactsSyncAdapter(
                        getApplicationContext(),
                        true
                );
            }
        }
    }
}
