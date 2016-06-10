package mc185249.webforms;

import android.content.ContentProvider;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mc185249 on 4/5/2016.
 */
public abstract class WebFormsProvider extends ContentProvider {
    SQLiteDatabase db;
    public static final String DATABASE_NAME = "WebForms";
    public static final String EMAIL_TABLE_NAME = "Emails";
    public static final String ATTACHAMENT_FILES_TABLE_NAME = "AttachementFiles";
    public static final String CLIENT_TABLE_NAME = "Clients";
    public static final String LOG_TABLE_NAME = "Log";
    public static final String CONTACTS_TABLE_NAME = "Contactos";
    public static final int DATABASE_VERSION = 11;

     public static class DBHelper extends SQLiteOpenHelper {

        //querys
        private static final String CREATE_TABLE_EMAILS = "CREATE TABLE Emails " +
                "(id integer primary key autoincrement, current_state integer DEFAULT 0, activity text, fecha date," +
                " subject text, body text, recipient text, sender text );";
        private static final String CREATE_TABLE_ATTACHEMENTFILES = "CREATE TABLE " +
                "AttachementFiles (id integer primary key autoincrement, mimeType text," +
                " blob BLOB, name text, email_id integer" +
                " ,FOREIGN KEY(email_id) REFERENCES Emails(id));";
         private static final String CREATE_TABLE_CLIENT = "CREATE TABLE Clients" +
                 "(id integer primary key autoincrement, pais text, nombre text," +
                 "numero text);";
         private static final String CREATE_TABLE_CONTACTS = "CREATE TABLE " +
                 CONTACTS_TABLE_NAME + " (id integer primary key autoincrement," +
                 "Direcciones text, Nombres text, Pais text, Numero text);";

         private static final String CREATE_TABLE_LOG = "CREATE TABLE Log" +
                 "(ID INTEGER PRIMARY KEY autoincrement NOT NULL," +
                 "emailID INTEGER NOT NULL, " +
                 "appVersion text NOT NULL," +
                 "txtFecha TEXT NOT NULL," +
                 "txtCsrcode TEXT NOT NULL," +
                 "txtIDATM TEXT," +
                 "selClient TEXT," +
                 "txtWO TEXT," +
                 "txtSerie TEXT," +
                 "txtContacto TEXT," +
                 "txtParte TEXT," +
                 "chkProElectrico INTEGER DEFAULT 0 ," +
                 "chkVolNoRegulado INTEGER DEFAULT 0," +
                 "txtFN INTEGER DEFAULT 0," +
                 "txtFT INTEGER DEFAULT 0," +
                 "txtNT INTEGER DEFAULT 0," +
                 "chkNoUps INTEGER DEFAULT 0," +
                 "chkNoTierraFisica INTEGER DEFAULT 0," +
                 "chkNoEnergia INTEGER DEFAULT 0," +
                 "chkProSite INTEGER DEFAULT 0," +
                 "chkSuciedad INTEGER DEFAULT 0," +
                 "chkGoteras INTEGER DEFAULT 0," +
                 "chkPlagas INTEGER DEFAULT 0," +
                 "chkExpSol INTEGER DEFAULT 0," +
                 "chkHumedad INTEGER DEFAULT 0," +
                 "chkMalaIluminacion INTEGER DEFAULT 0," +
                 "chkNoAA INTEGER DEFAULT 0," +
                 "chkProComms INTEGER DEFAULT 0," +
                 "selComunicaciones INTEGER DEFAULT 0," +
                 "chkProOperativo INTEGER DEFAULT 0," +
                 "chkSinInsumos INTEGER DEFAULT 0," +
                 "chkMalaCalidadBilletes INTEGER DEFAULT 0," +
                 "chkSinBilletes INTEGER DEFAULT 0," +
                 "chkMalaCalidadInsumos INTEGER DEFAULT 0," +
                 "chkErrorOperador INTEGER DEFAULT 0," +
                 "chkCargaIncPapel INTEGER DEFAULT 0," +
                 "chkCargaIncCaseteras INTEGER DEFAULT 0," +
                 "chkSupervisor INTEGER DEFAULT 0," +
                 "chkErrorBalanceo INTEGER DEFAULT 0," +
                 "chkProVandalismo INTEGER DEFAULT 0," +
                 "chkProOtros INTEGER DEFAULT 0," +
                 "chkProFotos INTEGER DEFAULT 0," +
                 "txtComentario TEXT," +
                 "formID TEXT);" ;
        private static final String DROP_TABLE_EMAILS = "DROP TABLE IF EXISTS Emails;";
        private static final String DROP_TABLE_ATTACHEMENTFILES = "DROP TABLE IF EXISTS AttachementFiles;";
        private static final String DROP_TABLE_CLIENT = "DROP TABLE IF EXISTS Clients;";
         private static final String DROP_TABLE_LOG = "DROP TABLE IF EXISTS Log";
        private static final String DROP_TABLE_CONTACTS = "DROP TABLE IF EXISTS " +
                CONTACTS_TABLE_NAME + ";";

        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE_EMAILS);
            db.execSQL(CREATE_TABLE_ATTACHEMENTFILES);
            db.execSQL(CREATE_TABLE_CLIENT);
            db.execSQL(CREATE_TABLE_LOG);
            db.execSQL(CREATE_TABLE_CONTACTS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DROP_TABLE_EMAILS);
            db.execSQL(DROP_TABLE_ATTACHEMENTFILES);
            db.execSQL(DROP_TABLE_CLIENT);
            db.execSQL(DROP_TABLE_LOG);
            db.execSQL(DROP_TABLE_CONTACTS);
            onCreate(db);
        }
    }

}
