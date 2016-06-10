package mc185249.webforms;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import app.AppController;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;
import models.CambioPidPadForm;
import models.EmailSender;

public class cambioPidPad extends WebFormsActivity
        implements MenuItem.OnMenuItemClickListener {

    @NotEmpty(messageId = R.string.validation_text)
    EditText workOrder,custRef,siteName,nroPos
            ,serieSaliente, serieEntrante;

    CambioPidPadForm form;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambio_pid_pad);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        form = new CambioPidPadForm();
        email = new EmailSender(this);
        workOrder = (EditText) findViewById(R.id.editText_WO);
        custRef = (EditText) findViewById(R.id.editText_custRef);
        siteName = (EditText)findViewById(R.id.editText_siteName);
        nroPos = (EditText)findViewById(R.id.editText_nroPos);
        serieSaliente = (EditText)findViewById(R.id.editText_serieSaliente);
        serieEntrante = (EditText)findViewById(R.id.editText_serieEntrante);

        workOrder.setOnFocusChangeListener(this);
        custRef.setOnFocusChangeListener(this);
        siteName.setOnFocusChangeListener(this);
        nroPos.setOnFocusChangeListener(this);
        serieSaliente.setOnFocusChangeListener(this);
        serieEntrante.setOnFocusChangeListener(this);

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

                /**
                 * @see AppController#checkCredentials()  true si el usuario se logeo, false caso contrario.
                 */
                boolean validCredentials = AppController.getInstance().checkCredentials();

                /**
                 * Si el usuario esta logeado y los campos son validos
                 */
                if (validCredentials
                        && validate()){

                    form.setWorkOrder(workOrder.getText().toString());
                    form.setCustRef(custRef.getText().toString());
                    form.setSiteName(siteName.getText().toString());
                    form.setNroPos(nroPos.getText().toString());
                    form.setSerieSaliente(serieSaliente.getText().toString());
                    form.setSerieEntrante(serieEntrante.getText().toString());
                    
                    email.setSubject("Nuevo Formulario - Cambio Pid Pad");
                    email.setRecipients(getContacts());
                    email.setFrom(
                            new WebFormsPreferencesManager(this).getUserName()
                    );
                    email.bodyMaker(form);
                    saveEmail();
                }else if (!validCredentials){
                    Intent intent = new Intent(this, Stepper.class);
                    startActivity(intent);
                }
                break;
        }
        return true;
    }
}
