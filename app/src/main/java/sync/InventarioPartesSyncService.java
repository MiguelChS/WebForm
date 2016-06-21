package sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by jn185090 on 6/21/2016.
 */
public class InventarioPartesSyncService extends Service {

    private static SynInventarioPartes synInventarioPartes = null;
    private static final Object syncAdapterLock = new Object();

    @Override
    public void onCreate() {
        synchronized (syncAdapterLock) {
            if (synInventarioPartes == null) {
                synInventarioPartes = new SynInventarioPartes(getApplicationContext());
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return synInventarioPartes.getSyncAdapterBinder();
    }
}
