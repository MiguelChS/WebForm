package com.example.mc185249.webforms;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;

import models.EmailSender;

public class memoriaFiscalActivity extends com.example.mc185249.webforms.WebFormsActivity implements com.example.mc185249.webforms.WorkOrderFragment.OnFragmentInteractionListener {

    EditText editTextWO,editTextCustRef, editTextSiteName,
        editTextPuntoVenta, editTextFirmwarePlacaFiscal, editTextSerie;
    Spinner spinnerCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memoria_fiscal);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        email = new EmailSender(this);
        editTextWO = (EditText) findViewById(R.id.WorkOrder);
        editTextWO.setOnFocusChangeListener(this);
        editTextCustRef = (EditText) findViewById(R.id.editTextCustRef);
        editTextCustRef.setOnFocusChangeListener(this);
        editTextSiteName = (EditText) findViewById(R.id.editTextSiteName);
        editTextSiteName.setOnFocusChangeListener(this);
        editTextPuntoVenta = (EditText) findViewById(R.id.editTextPuntoVenta);
        editTextPuntoVenta.setOnFocusChangeListener(this);
        editTextFirmwarePlacaFiscal = (EditText) findViewById(R.id.editTextPlacaFiscal);
        editTextFirmwarePlacaFiscal.setOnFocusChangeListener(this);
        editTextSerie = (EditText) findViewById(R.id.editTextSerieImpresora);
        editTextSerie.setOnFocusChangeListener(this);
        spinnerCliente = (Spinner) findViewById(R.id.spinnerCliente);
        initializeFab();
    }



    protected void buttonSave_Click() {
        String account = getCredential().get(String.valueOf(R.string.accountName));
        String passwd = getCredential().get(String.valueOf(R.string.passwd));

        if (account != null && passwd != null && validate()){

            com.example.mc185249.webforms.MemoriaFiscalForm form = new com.example.mc185249.webforms.MemoriaFiscalForm();
            form.setWorkOrder(String.valueOf(editTextWO.getText()));
            form.setCustRef(String.valueOf(editTextCustRef.getText()));
            form.setSiteName(String.valueOf(editTextSiteName.getText()));
            form.setPuntoVenta(String.valueOf(editTextPuntoVenta.getText()));
            form.setCliente(String.valueOf(spinnerCliente.getSelectedItem()));
            form.setFirmwarePlaca(String.valueOf(editTextFirmwarePlacaFiscal.getText()));
            form.setSerieImpresor(String.valueOf(editTextFirmwarePlacaFiscal.getText()));

            email.setSubject("NCR");
            email.setRecipients(new String[]{"joaquinnicolas96@hotmail.com"});
            email.bodyMaker(form);
            email.setFrom("JNN");
            createEmail();
        }
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
                buttonSave_Click();
                break;
        }
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
