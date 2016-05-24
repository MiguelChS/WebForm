package com.example.mc185249.webforms;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import eu.inmite.android.lib.validations.form.annotations.NotEmpty;
import models.EmailSender;

public class LogisticsSurveyActivity extends com.example.mc185249.webforms.WebFormsActivity {

    @NotEmpty(messageId = R.string.validation_text)
    EditText editTextWO, editTextParte, editTextReparador, editTextRetorno,editTextComentario;
    com.example.mc185249.webforms.LogisticsSurveyForm form;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logistics_survey);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        email = new EmailSender(this);
        form = new com.example.mc185249.webforms.LogisticsSurveyForm();
        editTextComentario = (EditText) findViewById(R.id.editTextComentario);
        editTextParte = (EditText) findViewById(R.id.editTextParte);
        editTextReparador = (EditText) findViewById(R.id.editTextReparador);
        editTextRetorno = (EditText) findViewById(R.id.editTextRetorno);
        editTextWO = (EditText) findViewById(R.id.editText_WO);

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
                    String account = getCredential().get(String.valueOf(R.string.accountName));
                    String passwd = getCredential().get(String.valueOf(R.string.passwd));

                    if (account != null && passwd != null){
                        form.setWorkOrder(editTextWO.getText().toString());
                        form.setComentario(editTextComentario.getText().toString());
                        form.setParte(editTextParte.getText().toString());
                        form.setRefReparador(editTextReparador.getText().toString());
                        form.setRetorno(editTextRetorno.getText().toString());

                        email.setSubject("NCR");
                        email.setRecipients(new String[]{"joaquinnicolas96@hotmail.com"});
                        email.bodyMaker(form);
                        email.setFrom(account);

                        createEmail();
                    }

                }
                break;
        }

        return true;
    }
}
