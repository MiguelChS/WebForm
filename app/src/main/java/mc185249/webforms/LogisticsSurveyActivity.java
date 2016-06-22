package mc185249.webforms;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;


import java.util.Date;

import app.AppController;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;
import models.EmailSender;
import models.WebFormsLogModel;

public class LogisticsSurveyActivity extends mc185249.webforms.WebFormsActivity {

  public static WebFormsLogModel logModel;
    @NotEmpty(messageId = R.string.validation_text)
    EditText editTextWO, editTextParte, editTextReparador,
            editTextRetorno,editTextComentario;
    LogisticsSurveyForm form;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logistics_survey);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        logModel = new WebFormsLogModel();
        email = new EmailSender(this);
        form = new LogisticsSurveyForm();
        editTextComentario = (EditText) findViewById(R.id.editTextComentario);
        editTextParte = (EditText) findViewById(R.id.editTextParte);
        editTextReparador = (EditText) findViewById(R.id.editTextReparador);
        editTextRetorno = (EditText) findViewById(R.id.editTextRetorno);
        editTextWO = (EditText) findViewById(R.id.WorkOrder);

        editTextComentario.setOnFocusChangeListener(this);
        editTextParte.setOnFocusChangeListener(this);
        editTextRetorno.setOnFocusChangeListener(this);
        editTextReparador.setOnFocusChangeListener(this);
        editTextWO.setOnFocusChangeListener(this);
        initializeFab();
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
                if (validate()){
                    String appVersion = null;
                    try{
                        appVersion =
                                AppController.getInstance().getAppVersion();
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }

                    if (AppController.getInstance().checkCredentials()){

                        logModel.setAppVersion(appVersion);
                        logModel.setTxtFecha(new Date().toString());
                        logModel.setTxtCsrCode(new WebFormsPreferencesManager(this)
                                .getCsrCode());
                        logModel.setTxtWO("W" + editTextWO.getText().toString());
                        logModel.setTxtParte(editTextParte.getText().toString());
                        logModel.setTxtReparador(editTextReparador.getText().toString());
                        logModel.setTxtRetorno(editTextRetorno.getText().toString());
                        logModel.setChkProFotos((email.get_hasAttachment()));
                        logModel.setTxtComentario(editTextComentario.getText().
                                toString());
                        logModel.setFormID("Logistics");

                        form.setWorkOrder("W" + editTextWO.getText().toString());
                        form.setComentario(editTextComentario.getText().toString());
                        form.setParte(editTextParte.getText().toString());
                        form.setRefReparador(editTextReparador.getText().toString());
                        form.setRetorno(editTextRetorno.getText().toString());

                        email.setSubject("Nuevo Formulario - Logistics");
                        email.setRecipients(getContacts(null));
                        email.bodyMaker(form);
                        email.setFrom(
                                new WebFormsPreferencesManager(this).getUserName()
                        );
                        email.setForm(logModel);
                        createEmail();
                    }

                }
                break;
        }

        return true;
    }
}
