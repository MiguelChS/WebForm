package mc185249.webforms;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResolvingResultCallbacks;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import java.util.Date;

import adapters.PlaceAutocompleteAdapter;
import app.AppController;
import eu.inmite.android.lib.validations.form.FormValidator;
import eu.inmite.android.lib.validations.form.annotations.MinLength;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;
import eu.inmite.android.lib.validations.form.annotations.RegExp;
import eu.inmite.android.lib.validations.form.callback.SimpleErrorPopupCallback;
import layout.DatePickerFragment;
import models.DevolucionPartes;
import models.EmailSender;

public class DevolucionPartesActivity extends WebFormsActivity implements GoogleApiClient.OnConnectionFailedListener {
    DevolucionPartes devolucionPartesForm;
    private GoogleApiClient mGoogleApiClient;
    private PlaceAutocompleteAdapter mAdapter;


    @NotEmpty(messageId = R.string.validation_text)
    @MinLength(value = 8, messageId = R.string.minLength8)
    EditText wareHouse;
    @NotEmpty(messageId = R.string.validation_text)
    AutoCompleteTextView localidad;
    @NotEmpty(messageId = R.string.validation_text)
    EditText fecha;
    EditText guiaDHL;
    CheckBox gng, oca;
    LinearLayout GNWrapper, OCAWrapper;
    EditText guia;
    EditText empresa;
    EditText ordenRetiro;
    @NotEmpty(messageId = R.string.validation_text)
    @MinLength(value = 10,messageId = R.string.minLength10)
    @RegExp(value = "[0-9]", messageId = R.string.campo_numerico)
    EditText parte;
    @NotEmpty(messageId = R.string.validation_text)
    EditText descripcion;
    @NotEmpty(messageId = R.string.validation_text)
    @RegExp(value = "[0-9]",messageId = R.string.campo_numerico)
    EditText cantidad;
    Spinner estado;
    private AdapterView.OnItemClickListener mAutocompleteClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devolucion_partes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,0,this)
                .addApi(Places.GEO_DATA_API)
                .build();
        devolucionPartesForm = new DevolucionPartes();
        email = new EmailSender(this);

        wareHouse = (EditText)findViewById(R.id.warehouse);
        localidad = (AutoCompleteTextView) findViewById(R.id.localidad);
        fecha = (EditText)findViewById(R.id.fecha);
        guiaDHL = (EditText)findViewById(R.id.guiaDHL);
        gng = (CheckBox) findViewById(R.id.gng);
        oca = (CheckBox)findViewById(R.id.oca);
        GNWrapper = (LinearLayout)findViewById(R.id.GNWrapper);
        OCAWrapper = (LinearLayout)findViewById(R.id.OCAWrapper);
        guia = (EditText)findViewById(R.id.guia);
        empresa = (EditText)findViewById(R.id.empresa);
        ordenRetiro = (EditText)findViewById(R.id.ordenRetiro);
        parte = (EditText)findViewById(R.id.parte);
        descripcion = (EditText)findViewById(R.id.descripcion);
        cantidad = (EditText)findViewById(R.id.cantidad);
        estado = (Spinner)findViewById(R.id.estado);

        oca.setOnCheckedChangeListener(oncheckedListener);
        gng.setOnCheckedChangeListener(oncheckedListener);
        fecha.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    DatePickerFragment dialog = new DatePickerFragment();
                    dialog.show(getSupportFragmentManager(), new String("datePicker"));

                }
            }
        });
        cantidad.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND){
                    send();
                    handled = true;
                }

                return handled;
            }
        });

        wareHouse.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                FormValidator.validate(DevolucionPartesActivity.this,new SimpleErrorPopupCallback(getApplicationContext()));
            }
        });


        localidad.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final AutocompletePrediction item = mAdapter.getItem(position);
                final String placeId = item.getPlaceId();
                final CharSequence primaryText = item.getPrimaryText(null);

                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                        .getPlaceById(mGoogleApiClient,placeId);
                placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            }
        });
        mAdapter = new PlaceAutocompleteAdapter(this,mGoogleApiClient,null);
        localidad.setAdapter(mAdapter);
    }

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResolvingResultCallbacks<PlaceBuffer>(DevolucionPartesActivity.this,0) {
        @Override
        public void onSuccess(@NonNull PlaceBuffer places) {
            if (!places.getStatus().isSuccess()){
                places.release();
                return;
            }
           // final Place place = places.get(0);
            places.release();
        }

        @Override
        public void onUnresolvableFailure(@NonNull Status status) {

        }
    };

    @Override
    protected void onStart(){
        super.onStart();
        mGoogleApiClient.connect();
    }
    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    public void fragmentCallback(Date date){
        fecha.setText(date.toString());
        devolucionPartesForm.setFecha(date);
    }

    CompoundButton.OnCheckedChangeListener oncheckedListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (buttonView.getId() == gng.getId()){
                if (isChecked){
                    GNWrapper.setVisibility(View.VISIBLE);
                }else{
                    GNWrapper.setVisibility(View.GONE);
                }

            }
            if (buttonView.getId() == oca.getId()){
                if (isChecked){
                    OCAWrapper.setVisibility(View.VISIBLE);
                }else{
                    OCAWrapper.setVisibility(View.GONE);
                }

            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu);
        return true;
        }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){

            case R.id.action_send:
                send();
                break;
        }
        return super.onMenuItemClick(item);
    }


    private void send() {
        if (validate()){
            if (AppController.getInstance().checkCredentials()){
                devolucionPartesForm.setCantidad(cantidad.getText().toString());
                devolucionPartesForm.setWareHouse(wareHouse.getText().toString());
                devolucionPartesForm.setLocalidad(localidad.getText().toString());
                devolucionPartesForm.setGuiaDHL(guiaDHL.getText().toString());
                devolucionPartesForm.setGuia(guia.getText().toString());
                devolucionPartesForm.setEmpresa(empresa.getText().toString());
                devolucionPartesForm.setOrdenRetiro(ordenRetiro.getText().toString());
                devolucionPartesForm.setParte(parte.getText().toString());
                devolucionPartesForm.setDescripcion(descripcion.getText().toString());
                devolucionPartesForm.setCantidad(cantidad.getText().toString());
                devolucionPartesForm.setEstado(estado.getSelectedItem().toString());

                email.setCSRCode(new WebFormsPreferencesManager(getApplicationContext()).getCsrCode());
                email.setSubject("Nuevo Formulario - Devolucion Partes");
                email.setRecipients(getContacts(null));
                email.setFrom(new WebFormsPreferencesManager(this).getUserName());
                email.bodyMaker(devolucionPartesForm);
                saveEmail();
            }
        }
    }



    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
