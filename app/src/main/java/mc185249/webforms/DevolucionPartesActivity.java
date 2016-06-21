package mc185249.webforms;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.Date;

import app.AppController;
import layout.DatePickerFragment;
import models.DevolucionPartes;
import models.EmailSender;

public class DevolucionPartesActivity extends WebFormsActivity  {
    DevolucionPartes devolucionPartesForm;


    EditText wareHouse;
    EditText localidad;
    EditText fecha;
    EditText guiaDHL;
    CheckBox gng, oca;
    LinearLayout GNWrapper, OCAWrapper;
    EditText guia;
    EditText empresa;
    EditText ordenRetiro;
    EditText parte;
    EditText descripcion;
    EditText cantidad;
    Spinner estado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devolucion_partes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        devolucionPartesForm = new DevolucionPartes();
        email = new EmailSender(this);

        wareHouse = (EditText)findViewById(R.id.warehouse);
        localidad = (EditText)findViewById(R.id.localidad);
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

                        email.setSubject("Nuevo Formulario - Devolucion Partes");
                        email.setRecipients(getContacts(null));
                        email.setFrom(new WebFormsPreferencesManager(this).getUserName());
                        email.bodyMaker(devolucionPartesForm);
                        saveEmail();
                    }
                }
                break;
        }
        return super.onMenuItemClick(item);
    }
}
