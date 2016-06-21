package mc185249.webforms;


import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.gc.materialdesign.widgets.ProgressDialog;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import app.AppController;
import eu.inmite.android.lib.validations.form.FormValidator;
import eu.inmite.android.lib.validations.form.callback.SimpleErrorPopupCallback;
import layout.DatePickerFragment;
import mc185249.webforms.EmailsProvider;
import mc185249.webforms.LogProvider;
import mc185249.webforms.WebFormsPreferencesManager;
import models.EmailSender;
import models.Storage;
import models.WebFormsLogModel;

/**
 * Created by mc185249 on 4/11/2016.
 */
public class WebFormsActivity extends AppCompatActivity
        implements DatePickerFragment.FragmentListener,
        View.OnFocusChangeListener, View.OnClickListener, MenuItem.OnMenuItemClickListener {

    EmailSender email = null;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_ATTACH = 2;
    File photo = null;
    ProgressDialog dialog = null;
    com.getbase.floatingactionbutton.FloatingActionButton fabCamera, fabAttach;
    FloatingActionsMenu fabMenu;
    private Bundle savedInstance = null;


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        for (models.File f : email.getFiles())
        {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            f.getBitmap().compress(Bitmap.CompressFormat.PNG,100,stream);
            byte[] bytes = stream.toByteArray();
            outState.putByteArray(f.getName(),bytes);
        }
    }

    protected void onCreate(Bundle savedInstanceState){
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        email = new EmailSender(this);
        this.savedInstance = savedInstanceState;

        super.onCreate(savedInstanceState);

    }

    public void initializeFab()
    {
        this.fabMenu = (FloatingActionsMenu) findViewById(R.id.fabMenu);
        this.fabCamera = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.camerafab);
        this.fabAttach = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fab_attach);
        this.fabCamera.setOnClickListener(this);
        this.fabAttach.setOnClickListener(this);

        if (savedInstance != null){
            for (models.File key:
                 email.getFiles()) {

                attachPhotoAndDisplayOnFAB(
                        ((models.File) savedInstance.get(key.getName())).
                                getBitmap()
                );
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            if (extras != null){
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                attachPhotoAndDisplayOnFAB(imageBitmap);
            }
        }

        if (REQUEST_IMAGE_ATTACH == requestCode && resultCode == RESULT_OK){
            Uri uri = data.getData();
            String path = uri.getPath();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
                attachPhotoAndDisplayOnFAB(bitmap);

            } catch (IOException e) {
                displayAlertDialog(e);
            } catch (Exception e) {
                displayAlertDialog(e);
            }
        }
    }

    private void displayAlertDialog(Exception e) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(e.getMessage()).setTitle("Error");
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void attachPhotoAndDisplayOnFAB(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream =
                new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
        byte[] b = byteArrayOutputStream.toByteArray();
        String base64Image = Base64.encodeToString(b,Base64.DEFAULT);

        models.File model_file = new models.File(
                ("WebForms_" + new WebFormsPreferencesManager(this).getCsrCode() +
                        new Date().toString()),
                base64Image,
                bitmap
        );

        com.getbase.floatingactionbutton.FloatingActionButton fab = new com.getbase.floatingactionbutton.FloatingActionButton(this);
        fab.setSize(com.getbase.floatingactionbutton.FloatingActionButton.SIZE_MINI);
        Bitmap roundedPhoto = null;
        roundedPhoto = Storage.roundedCroppedImage(bitmap);
        fab.setPadding(-1,-1,-1,-1);
        fab.setScaleType(ImageView.ScaleType.CENTER_CROP);
        fab.setImageBitmap(roundedPhoto);
        fab.setTag(model_file);

        email
                .Attach(
                        model_file
                );

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email.removeAttachFile((File) v.getTag());
                fabMenu.removeButton((com.getbase.floatingactionbutton.FloatingActionButton) v);
            }
        });

        fabMenu.addButton(fab);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openCamera();
                }
                break;

            case 300:
                boolean writeAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (writeAccepted)
                    openCamera();
                 break;
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {


        if (v.getId() == this.fabCamera.getId()){


            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)){

                }else{
                    ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            1);
                }
            }else{
                openCamera();
            }


        }

        if(v.getId() == this.fabAttach.getId()){
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            try{
                startActivityForResult(
                        intent,REQUEST_IMAGE_ATTACH
                );
            }catch (ActivityNotFoundException e){
                displayAlertDialog(e);
            }
        }


    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photo = null;
            try {
                photo = new Storage().createImageFile();
                Bundle bundle = new Bundle();

                onSaveInstanceState(bundle);
            } catch (Exception e) {
                displayAlertDialog(e);
            }
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus){
            final boolean isValid = FormValidator.validate(this, new SimpleErrorPopupCallback(this));
        }
    }

    /**
     * Se fija que los EditText sean validos, es decir, no esten vacios y cumplan con lo solicitado.
     * @return
     * <ul>
     *     <li>true -> si los campos no estan vacios</li>
     *     <li>false -> hay campos vacios o invalidos</li>
     * </ul>
     */
    protected boolean validate(){
        final boolean isValid = FormValidator.validate(this,new SimpleErrorPopupCallback(this));
        return isValid;
    }

    public HashMap<String, String> getCredential(){
        WebFormsPreferencesManager sharedPreferences =
                new WebFormsPreferencesManager(this);
        String account = sharedPreferences.
                getUserName();
        String passwd = sharedPreferences.
                getPasswd();
        HashMap<String, String> credentials = new HashMap<>();
        credentials.put(String.valueOf(R.string.accountName),account);
        credentials.put(String.valueOf(R.string.passwd),passwd);

        return credentials;
    }

    /**
     * @deprecated usar saveEmail
     */
    public void createEmail(){

        saveEmail();
    }

    private void saveLog(WebFormsLogModel log) {
        LogProvider logProvider = new LogProvider(this);
        long count = logProvider.insert(log);
        Log.v("NCR","log insertado = " + count);
        dialog.dismiss();
        Toast.makeText(this,"operacion exitosa",Toast.LENGTH_SHORT).show();
    }

    /**
     * Guarda el email dentro de la base de datos
     */
    public void saveEmail()
    {
        showProgressDialog();
        for (String rec : email.getRecipients().split(",")){
            ContentValues values = new ContentValues();
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            values.put(EmailsProvider.FECHA, dateFormat.format(new java.util.Date()));
            values.put(EmailsProvider.ACTIVITY,this.getClass().getSimpleName());
            values.put(EmailsProvider.SUBJECT,email.getSubject());
            values.put(EmailsProvider.BODY, email.getBody());
            values.put(EmailsProvider.RECIPIENT,rec);
            values.put(EmailsProvider.FROM,email.getFrom());

            Uri uri = getContentResolver().insert(
                    EmailsProvider.CONTENT_URI,values
            );

            long id = Long.valueOf(uri.getLastPathSegment());

            for (models.File f : email.getFiles()){
                values = new ContentValues();
                values.put(AttachementProvider.MIME_TYPE,"image/png");
                values.put(AttachementProvider.BLOB,f.getBlob());
                values.put(AttachementProvider.EMAIL_ID,id);

                Uri uri1 = getContentResolver().insert(
                        AttachementProvider.CONTENT_URI,values
                );
            }

            if (email.getForm() != null){
                WebFormsLogModel log = email.getForm();
                log.setEmailID((int) id);
                email.setForm(log);
                saveLog(email.getForm());
            }else{
                dialog.dismiss();
                Toast.makeText(this,"operacion exitosa",Toast.LENGTH_SHORT).show();
            }


        }

    }

    public void showProgressDialog(){
            dialog = new ProgressDialog(this,"Procesando...");
            dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.action_send);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.setVisible(false);
        MenuItem settingItem = menu.findItem(R.id.setting);
        settingItem.setVisible(false);
        if (menuItem != null){
            menuItem.setOnMenuItemClickListener(this);
        }


        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return true;
    }


    @Override
    public void fragmentCallback(Date date) {

    }

    /**
     * Carga los clientes de la base de datos dentro de un Spinner
     * @param spinner
     */
    protected void loadSpinner(Spinner spinner){
        ContentResolver mContentResolver = getContentResolver();
        ArrayList<String> localClients = new ArrayList<>();
        Uri clientes = Uri.parse(ClientsContentProvider.URL);
        Cursor cursor = mContentResolver.query(
                clientes,
                null,
                null,
                null,null
        );

        if (cursor != null){
            while (cursor.moveToNext()){
                localClients.add(
                        cursor.getString(
                                cursor.getColumnIndex(
                                        ClientsContentProvider
                                                .NOMBRE
                                )
                        )
                );
            }
        }
        cursor.close();
        ArrayAdapter<String> dataAdapter =
                new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,localClients);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    /**
     *
     * Devuelve un array con email de contactos.
     * @return
     *      <ul>
     *          <li>String[] con email de contactos</li>
     *          <li>null si no hay contactos</li>
     *      </ul>
     */
    protected String[] getContacts(@Nullable String cliente){
        ContentResolver mContentResolver = getContentResolver();
        String csrCode = new WebFormsPreferencesManager(this).getCsrCode();
        String[] contacts = null;
        Uri contacrsUri = Uri.parse(ContactsProvider.URL);
        String[] selectArgs;
        String select;
        if (cliente == null){
            select = ContactsProvider.NUMERO  + " = ? ";
            selectArgs = new String[]{
              csrCode
            };
        }else{
            select = ContactsProvider.NUMERO + "= ?  OR " + ContactsProvider.NUMERO +" = ?";
            selectArgs = new String[]{
                    csrCode,cliente
            };
        }
        Cursor cursor = mContentResolver.query(
                contacrsUri,
                null,
                select,
                selectArgs,
                null
        );
        if (cursor != null && cursor.getCount() > 0){
            contacts = new String[cursor.getCount()];
            int x = 0;
            while(cursor.moveToNext()){
                contacts[x++] = (
                        cursor.getString(
                                cursor.getColumnIndex(
                                        ContactsProvider.DIRECIONES
                                )
                        )
                );
            }
        }
        cursor.close();
        return contacts;

    }

    /**
     * BUSCA CLIENTES EN SQLITE
     * @return true si existen clientes, caso contrario false
     */
    public boolean checkClientes(){
        Cursor mCursor = getContentResolver().query(
                ClientsContentProvider.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        if (null == mCursor){
            return false;
        }
        else if(mCursor.getCount() < 1){
            return false;
        }
        return true;
    }
}


