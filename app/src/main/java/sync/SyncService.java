package sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by jn185090 on 5/27/2016.
 */
public class SyncService extends Service {
    public static ClientsSyncAdapter syncAdapter = null;
    public static final Object syncAdapterLock = new Object();


    @Override
    public void onCreate() {
        synchronized (syncAdapterLock){

            if (syncAdapter == null){
                syncAdapter = new ClientsSyncAdapter(
                        getApplicationContext(),
                        true
                );
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return syncAdapter.getSyncAdapterBinder();
    }
}
