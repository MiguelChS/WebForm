package mc185249.webforms;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;


import app.AppController;
import models.EmailSender;

public class memoriaFiscalActivity extends WebFormsActivity implements WorkOrderFragment.OnFragmentInteractionListener {

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
        loadSpinner(spinnerCliente);
    }



    protected void buttonSave_Click() {
        if (AppController.getInstance().checkCredentials()
                && validate()){

            String workOrder = "W" + editTextWO.getText().toString();
            MemoriaFiscalForm form = new MemoriaFiscalForm();
            form.setWorkOrder(workOrder);
            form.setCustRef(String.valueOf(editTextCustRef.getText()));
            form.setSiteName(String.valueOf(editTextSiteName.getText()));
            form.setPuntoVenta(String.valueOf(editTextPuntoVenta.getText()));
            form.setCliente(String.valueOf(spinnerCliente.getSelectedItem()));
            form.setFirmwarePlaca(String.valueOf(editTextFirmwarePlacaFiscal.getText()));
            form.setSerieImpresor(String.valueOf(editTextFirmwarePlacaFiscal.getText()));

            email.setSubject("Nuevo Formulario - Memoria Fiscal");
            email.setRecipients(getContacts(form.getCliente()));
            email.bodyMaker(form);
            email.setFrom(new WebFormsPreferencesManager(this).getUserName());
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
