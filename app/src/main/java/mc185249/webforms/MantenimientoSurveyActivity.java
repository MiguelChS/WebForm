package com.example.mc185249.webforms;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Date;

import eu.inmite.android.lib.validations.form.annotations.NotEmpty;
import layout.DatePickerFragment;
import models.EmailSender;

public class MantenimientoSurveyActivity
        extends com.example.mc185249.webforms.WebFormsActivity
        implements
            com.example.mc185249.webforms.WorkOrderFragment.OnFragmentInteractionListener {

    @NotEmpty
    EditText editTextDate, editTextComentario,editTextWO, editTextserie,editTextidEquipo;
    Spinner spinnerCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mantenimiento_survey);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


         email = new EmailSender(this);
        editTextDate = (EditText) findViewById(R.id.editTextDate);
        editTextComentario = (EditText) findViewById(R.id.editTextComentario);
        editTextWO = (EditText) findViewById(R.id.WorkOrder);
        editTextserie = (EditText) findViewById(R.id.serie);
        editTextidEquipo = (EditText) findViewById(R.id.idEquipo);

        editTextComentario.setOnFocusChangeListener(this);
        editTextidEquipo.setOnFocusChangeListener(this);
        editTextserie.setOnFocusChangeListener(this);

        editTextDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    DatePickerFragment dialog = new DatePickerFragment();
                    dialog.show(getSupportFragmentManager(), new String("datePicker"));
                }

            }
        });
        editTextWO.setOnFocusChangeListener(this);
        spinnerCliente = (Spinner)findViewById(R.id.spinnerCliente);
        initializeFab();
    }

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
                String account = getCredential().get(String.valueOf(R.string.accountName));
                String passwd = getCredential().get(String.valueOf(R.string.passwd));

                if (account != null && passwd != null && validate()){
                    com.example.mc185249.webforms.MantenimientoSurveyForm form = new com.example.mc185249.webforms.MantenimientoSurveyForm();
                    form.setWorkOrder(String.valueOf(editTextWO.getText()));
                    form.setSerie(String.valueOf(editTextserie.getText()));
                    form.setComentario(String.valueOf(editTextComentario.getText()));
                    form.setEquipo(String.valueOf(editTextidEquipo.getText()));
                    form.setCliente(String.valueOf(spinnerCliente.getSelectedItem()));
                    form.setFecha(String.valueOf(editTextDate.getText()));

                    email.setSubject("NCR");
                    email.setRecipients(new String[]{"joaquinnicolas96@hotmail.com"});
                    email.bodyMaker(form);
                    email.setFrom(account);
                    createEmail();
                }
                break;
        }

        return true;
    }


    public void fragmentCallback(Date date) {
        editTextDate.setText(date.toString());
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
