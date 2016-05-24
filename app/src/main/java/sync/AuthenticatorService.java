package sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AuthenticatorService extends Service {

    private Authenticator mAuthenticator;
    public AuthenticatorService() {
    }

    @Override
    public void onCreate() {
        mAuthenticator = new Authenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel recipients the service.
        return mAuthenticator.getIBinder();
    }
}
