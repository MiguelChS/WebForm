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
import models.VisitaTecnicaForm;

public class VisitaTecnica extends WebFormsActivity
        implements MenuItem.OnMenuItemClickListener {

    @NotEmpty(messageId = R.string.validation_text)
    EditText workOrder, serie,equipo;
    Spinner cliente;
    VisitaTecnicaForm form;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visita_tecnica);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        email = new EmailSender(this);
        workOrder = (EditText)findViewById(R.id.editText_wordOrder);
        serie = (EditText)findViewById(R.id.editText_serie);
        equipo = (EditText)findViewById(R.id.editText_equipo);
        cliente = (Spinner) findViewById(R.id.spinnerCliente);

        workOrder.setOnFocusChangeListener(this);
        serie.setOnFocusChangeListener(this);
        equipo.setOnFocusChangeListener(this);
        loadSpinner(cliente);
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

                    form = new VisitaTecnicaForm(
                            workOrder.getText().toString(),
                            cliente.getSelectedItem().toString(),
                            serie.getText().toString(),
                            equipo.getText().toString()
                    );
                    email.setSubject("Nuevo Formulario - Visita Tecnica");
                    email.setRecipients(getContacts());
                    email.setFrom(
                            new WebFormsPreferencesManager(this).getUserName()
                    );
                    email.bodyMaker(form);
                    createEmail();
                }else if(!validCredentials){

                    Intent intent = new Intent(this,Stepper.class);
                    startActivity(intent);
            }
                break;
        }

        return true;
    }
}
