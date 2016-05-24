package mc185249.webforms;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.mc185249.webforms.WebFormsProvider;

import models.WebFormsLogModel;

/**
 * Created by jn185090 on 5/18/2016.
 */
public class LogProvider  {

    SQLiteDatabase db;
    WebFormsProvider.DBHelper dbHelper;
    Context mContext;
    int appVersion;

    //region table fields
    private static String ID = "ID";
    public static String txtFecha = "txtFecha"
            ,txtCsrcode = "txtCsrcode"
            ,txtIDATM = "txtIDATM"
            ,selClient = "selClient"
            ,txtWO = "txtWO"
            ,txtSerie = "txtSerie"
            ,txtContacto = "txtContacto"
            ,txtParte = "txtParte"
            ,chkProElectrico = "chkProElectrico"
            ,chkVolNoRegulado = "chkVolNoRegulado"
            ,txtFN = "txtFN"
            ,txtFT = "txtFT"
            ,txtNT = "txtNT"
            ,chkNoUps = "chkNoUps"
            ,chkNoTierraFisica = "chkNoTierraFisica"
            ,chkNoEnergia = "chkNoEnergia"
            ,chkProSite="chkProSite"
            ,chkSuciedad = "chkSuciedad"
            ,chkGoteras = "chkGoteras"
            ,chkPlagas = "chkPlagas"
            ,chkExpSol = "chkExpSol"
            ,chkHumedad = "chkHumedad"
            ,chkMalaIluminacion = "chkMalaIluminacion"
            ,chkNoAA = "chkNoAA"
            ,chkProComms = "chkProComms"
            ,selComunicaciones = "selComunicaciones"
            ,chkProOperativo="chkProOperativo"
            ,chkSinInsumos = "chkSinInsumos"
            ,chkMalaCalidadBilletes = "chkMalaCalidadBilletes"
            ,chkSinBilletes = "chkSinBilletes"
            ,chkMalaCalidadInsumos = "chkMalaCalidadInsumos"
            ,chkErrorOperador = "chkErrorOperador"
            ,chkCargaIncPapel = "chkCargaIncPapel"
            ,chkCargaIncCaseteras = "chkCargaIncCaseteras"
            ,chkSupervisor = "chkSupervisor"
            ,chkErrorBalanceo = "chkErrorBalanceo"
            ,chkProVandalismo = "chkProVandalismo"
            ,chkProOtros = "chkProOtros"
            ,chkProFotos = "chkProFotos"
            ,txtComentario = "txtComentario"
            ,formID = "formID"
            ,AppVersion = "appVersion"
            ,emailID = "emailID";
    //endregion

    public LogProvider(Context context) {
        mContext = context;
        dbHelper = new WebFormsProvider.DBHelper(mContext);
        db = dbHelper.getWritableDatabase();
        try {
            appVersion = new Double(mContext.getPackageManager().getPackageInfo(mContext.getPackageName(),0).versionName).intValue();
        } catch (PackageManager.NameNotFoundException e) {
            appVersion = 0;
        }
    }


   /* DataBaseHub dbh=new DataBaseHub(this);
    SQLiteDatabase db = dbh.getReadableDatabase();
    Cursor cursor = db.query("Table_name",null,null,null,null,
            null, null, null, null, null);
    if (cursor != null)
            cursor.moveToFirst();
    //Now you can read record from cursor easily*/
    public WebFormsLogModel[] query(String[] columns, String[] selectionArgs,String selection){
        Cursor cursor = db.query(WebFormsProvider.LOG_TABLE_NAME,columns,selection,selectionArgs,null,null,null);
        WebFormsLogModel[] logModels = new WebFormsLogModel[cursor.getCount()];
        WebFormsLogModel logModel;
        int count = 0;
        while (cursor.moveToNext()){
            logModel = new WebFormsLogModel(
                    String.valueOf(appVersion),
                    cursor.getString(cursor.getColumnIndex(LogProvider.txtFecha)),
                    cursor.getString(cursor.getColumnIndex(LogProvider.txtCsrcode)),
                    cursor.getString(cursor.getColumnIndex(LogProvider.txtIDATM)),
                    cursor.getString(cursor.getColumnIndex(LogProvider.selClient)),
                    cursor.getString(cursor.getColumnIndex(LogProvider.txtWO)),
                    cursor.getString(cursor.getColumnIndex(LogProvider.txtSerie)),
                    cursor.getString(cursor.getColumnIndex(LogProvider.txtContacto)),
                    cursor.getString(cursor.getColumnIndex(LogProvider.txtParte)),
                    cursor.getString(cursor.getColumnIndex(LogProvider.txtComentario)),
                    (cursor.getInt(cursor.getColumnIndex(LogProvider.chkProElectrico)) >= 1 ? 1 : 0),
                    (cursor.getInt(cursor.getColumnIndex(LogProvider.chkVolNoRegulado)) >= 1 ? 1:0),
                    (cursor.getInt(cursor.getColumnIndex(LogProvider.txtFN)) >= 1 ? 1:0),
                    cursor.getInt(cursor.getColumnIndex(LogProvider.txtFT)),
                    cursor.getInt(cursor.getColumnIndex(LogProvider.txtNT)),
                    cursor.getInt(cursor.getColumnIndex(LogProvider.chkNoUps)),
                    cursor.getInt(cursor.getColumnIndex(LogProvider.chkNoTierraFisica)),
                    cursor.getInt(cursor.getColumnIndex(LogProvider.chkNoEnergia)),
                    cursor.getInt(cursor.getColumnIndex(LogProvider.chkProSite)),
                    cursor.getInt(cursor.getColumnIndex(LogProvider.chkSuciedad)),
                    cursor.getInt(cursor.getColumnIndex(LogProvider.chkGoteras)),
                    cursor.getInt(cursor.getColumnIndex(LogProvider.chkPlagas)),
                    cursor.getInt(cursor.getColumnIndex(LogProvider.chkExpSol)),
                    cursor.getInt(cursor.getColumnIndex(LogProvider.chkHumedad)),
                    cursor.getInt(cursor.getColumnIndex(LogProvider.chkMalaIluminacion)),
                    cursor.getInt(cursor.getColumnIndex(LogProvider.chkNoAA)),
                    cursor.getInt(cursor.getColumnIndex(LogProvider.chkProComms)),
                    cursor.getInt(cursor.getColumnIndex(LogProvider.selComunicaciones)),
                    cursor.getInt(cursor.getColumnIndex(LogProvider.chkProOperativo)),
                    cursor.getInt(cursor.getColumnIndex(LogProvider.chkSinInsumos)),
                    cursor.getInt(cursor.getColumnIndex(LogProvider.chkSinBilletes)),
                    cursor.getInt(cursor.getColumnIndex(LogProvider.chkMalaCalidadBilletes)),
                    cursor.getInt(cursor.getColumnIndex(LogProvider.chkMalaCalidadInsumos)),
                    cursor.getInt(cursor.getColumnIndex(LogProvider.chkErrorOperador)),
                    cursor.getInt(cursor.getColumnIndex(LogProvider.chkCargaIncPapel)),
                    cursor.getInt(cursor.getColumnIndex(LogProvider.chkCargaIncCaseteras)),
                    cursor.getInt(cursor.getColumnIndex(LogProvider.chkSupervisor)),
                    cursor.getInt(cursor.getColumnIndex(LogProvider.chkErrorBalanceo)),
                    cursor.getInt(cursor.getColumnIndex(LogProvider.chkProVandalismo)),
                    cursor.getInt(cursor.getColumnIndex(LogProvider.chkProOtros)),
                    cursor.getInt(cursor.getColumnIndex(LogProvider.chkProFotos))
                    );
            logModel.setID(cursor.getInt(cursor.getColumnIndex(LogProvider.ID)));
            logModels[count++] = logModel;
        }

        return logModels;
    }

    /*ContentValues cv=new ContentValues();
cv.put(DataBaseHub.Eid,101);
cv.put(DataBaseHub.Ename,"Tofeeq");
cv.put(DataBaseHub.Eadd,"142,Ananad Delhi");
long i=db.insert(DataBaseHub.Emp, null, cv);
Log.i("Row ID=",String.valueOf(i));*/
    public long insert(WebFormsLogModel webFormsLogModel){
        if (webFormsLogModel == null){
            return 0;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(LogProvider.AppVersion,webFormsLogModel.getAppVersion());
        contentValues.put(LogProvider.txtFecha,webFormsLogModel.getTxtFecha());
        contentValues.put(LogProvider.txtIDATM,webFormsLogModel.getTxtIDATM());
        contentValues.put(LogProvider.txtCsrcode,webFormsLogModel.getTxtCsrCode());
        contentValues.put(LogProvider.selClient,webFormsLogModel.getSelCliente());
        contentValues.put(LogProvider.txtWO,webFormsLogModel.getTxtWO());
        contentValues.put(LogProvider.txtSerie,webFormsLogModel.getTxtSerie());
        contentValues.put(LogProvider.txtContacto,webFormsLogModel.getTxtContacto());
        contentValues.put(LogProvider.txtParte,webFormsLogModel.getTxtParte());
        contentValues.put(LogProvider.chkProElectrico,webFormsLogModel.getChkProElectrico());
        contentValues.put(LogProvider.chkVolNoRegulado,webFormsLogModel.getChkVolNoRegulado());
        contentValues.put(LogProvider.txtFN,webFormsLogModel.getTxtFN());
        contentValues.put(LogProvider.txtFT,webFormsLogModel.getTxtFT());
        contentValues.put(LogProvider.txtNT,webFormsLogModel.getTxtNT());
        contentValues.put(LogProvider.chkNoUps,webFormsLogModel.getChkNoUps());
        contentValues.put(LogProvider.chkNoTierraFisica,webFormsLogModel.getChkNoTierraFisica());
        contentValues.put(LogProvider.chkNoEnergia,webFormsLogModel.getChkNoEnergia());
        contentValues.put(LogProvider.chkProSite,webFormsLogModel.getChkProSite());
        contentValues.put(LogProvider.chkSuciedad,webFormsLogModel.getChkSuciedad());
        contentValues.put(LogProvider.chkGoteras,webFormsLogModel.getChkGoteras());
        contentValues.put(LogProvider.chkPlagas,webFormsLogModel.getChkPlagas());
        contentValues.put(LogProvider.chkExpSol,webFormsLogModel.getChkExpSol());
        contentValues.put(LogProvider.chkHumedad,webFormsLogModel.getChkHumedad());
        contentValues.put(LogProvider.chkMalaIluminacion,webFormsLogModel.getChkMalaIluminacion());
        contentValues.put(LogProvider.chkNoAA,webFormsLogModel.getChkNoAA());
        contentValues.put(LogProvider.chkProComms,webFormsLogModel.getChkProComms());
        contentValues.put(LogProvider.selComunicaciones,webFormsLogModel.getSelComunicaciones());
        contentValues.put(LogProvider.chkProOperativo,webFormsLogModel.getChkProOperativo());
        contentValues.put(LogProvider.chkSinInsumos,webFormsLogModel.getChkSinInsumos());
        contentValues.put(LogProvider.chkMalaCalidadBilletes,webFormsLogModel.getChkMalaCalidadBilletes());
        contentValues.put(LogProvider.chkSinBilletes,webFormsLogModel.getChkSinBilletes());
        contentValues.put(LogProvider.chkMalaCalidadInsumos,webFormsLogModel.getChkMalaCalidadInsumos());
        contentValues.put(LogProvider.chkErrorOperador,webFormsLogModel.getChkErrorOperador());
        contentValues.put(LogProvider.chkCargaIncPapel,webFormsLogModel.getChkCargaIncPapel());
        contentValues.put(LogProvider.chkCargaIncCaseteras,webFormsLogModel.getChkCargaIncCaseteras());
        contentValues.put(LogProvider.chkSupervisor,webFormsLogModel.getChkSupervisor());
        contentValues.put(LogProvider.chkErrorBalanceo,webFormsLogModel.getChkErrorBalanceo());
        contentValues.put(LogProvider.chkProVandalismo,webFormsLogModel.getChkProVandalismo());
        contentValues.put(LogProvider.chkProOtros,webFormsLogModel.getChkProOtros());
        contentValues.put(LogProvider.chkProFotos,webFormsLogModel.getChkProFotos());
        contentValues.put(LogProvider.txtComentario,webFormsLogModel.getTxtComentario());
        contentValues.put(LogProvider.formID,webFormsLogModel.getFormID());
        contentValues.put(LogProvider.emailID,webFormsLogModel.getEmailID());

        return db.insert(WebFormsProvider.LOG_TABLE_NAME,null,contentValues);
    }

    /*DataBaseHub dbh=new DataBaseHub(this);
SQLiteDatabase db=dbh.getWritableDatabase();
i=db.delete(DataBaseHub.Emp, DataBaseHub.Eid+"=?",new String[]{"101"});
Log.i("Number of Row=",String.valueOf(i)););*/
    public int delete(int id){
        return db.delete(WebFormsProvider.LOG_TABLE_NAME,("WHERE " + ID + " = ?"),new String[]{
                String.valueOf(id)
        });
    }
}
