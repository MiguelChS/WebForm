package mc185249.webforms;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;

import app.AppController;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;
import models.EmailSender;
import models.TecladoEncryptorModel;

public class TecladoEncryptorActivity extends WebFormsActivity
        implements MenuItem.OnMenuItemClickListener{

    TecladoEncryptorModel form;

    @NotEmpty(messageId = R.string.validation_text)
    EditText workOrder, equipo,serie,comentario;
    Spinner cliente;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teclado_encryptor2);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        workOrder = (EditText)findViewById(R.id.WorkOrder);
        equipo = (EditText) findViewById(R.id.editText_equipo);
        serie = (EditText) findViewById(R.id.editText_serie);
        comentario = (EditText) findViewById(R.id.editText_comentario);
        cliente = (Spinner) findViewById(R.id.spinnerCliente);

        loadSpinner(cliente);
        workOrder.setOnFocusChangeListener(this);
        equipo.setOnFocusChangeListener(this);
        serie.setOnClickListener(this);
        comentario.setOnFocusChangeListener(this);

        email = new EmailSender(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_send:
                boolean validCredentials = AppController.getInstance().checkCredentials();
                if (validCredentials
                        && validate()){
                    String work = "W" + workOrder.getText().toString();
                    form = new TecladoEncryptorModel(
                            work,
                            cliente.getSelectedItem().toString(),
                            serie.getText().toString(),
                            equipo.getText().toString(),
                            "",
                            comentario.getText().toString()
                    );

                    email.setCSRCode(new WebFormsPreferencesManager(getApplicationContext()).getCsrCode());
                    email.setFrom(new WebFormsPreferencesManager(this).getUserName());
                    email.setSubject("Nuevo Formulario - Teclado Encryptor");
                    email.setRecipients(getContacts(form.getCliente()));
                    email.bodyMaker(form);
                    createEmail();
                }else if (!validCredentials){
                    Intent intent = new Intent(this,Stepper.class);
                    startActivity(intent);
                }
                break;
        }
        return true;
    }
}
