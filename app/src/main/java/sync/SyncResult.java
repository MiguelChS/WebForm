package sync;

/**
 * Created by joaquin on 06/07/16.
 */
public class SyncResult {

    public static int STATE_CLIENTES;
    public static int STATE_CONTACTOS;
    public static final int ACTIVE = 0, SUCCESS = 1, ERROR = 2;

    public interface Listener{
        void onSuccess();
        void onError();
    }
}
