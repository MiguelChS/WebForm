package mc185249.webforms;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.innodroid.expandablerecycler.ExpandableRecyclerAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import adapters.CheckListAdapter;
import adapters.ExpansibleListAdapter;
import adapters.ExpansibleListViewDataAdapter;
import app.AppController;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;
import mc185249.webforms.WebFormsPreferencesManager;
import models.Cliente;
import models.EmailSender;
import models.EnvironmentalSiteForm;
import models.WebFormsLogModel;

public class EnvironmentalSiteActivity extends mc185249.webforms.WebFormsActivity
        implements WorkOrderFragment.OnFragmentInteractionListener,
        MenuItem.OnMenuItemClickListener, ExpandableListView.OnChildClickListener {

    ExpandableListAdapter listAdapter;
    ExpandableListView expandableListView;
    ExpansibleListViewDataAdapter[] expansibleListViewDataAdapter;
    public static WebFormsLogModel logModel;

    @NotEmpty(messageId = R.string.validation_text)
    EditText WorkOrder, serie, idEquipo, editText_contacto,
            editText_comentario;
    Spinner client;
    @NotEmpty(messageId = R.string.validation_text)
    EditText editText_parte;
    EnvironmentalSiteForm form;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        email = new EmailSender(this);
        logModel = new WebFormsLogModel();

        setContentView(R.layout.activity_environmental_site);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.client = (Spinner) findViewById(R.id.cliente);
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        expandableListView.setDividerHeight(2);
        expandableListView.setGroupIndicator(null);
        expandableListView.setClickable(true);

        expansibleListViewDataAdapter = new ExpansibleListViewDataAdapter[]
                {
                       new ExpansibleListViewDataAdapter("Problema electrico",
                                  new String[]{
                                          "Voltaje no regulado","No posee UPS",
                                          "No posee tierra fisica",
                                          "Sin energia electrica"
                                  }),
                       new ExpansibleListViewDataAdapter("Problema de Site",new String[]
                                  {
                                          "Suciedad","Goteras",
                                          "Plagas",
                                          "Exposicion directa al sol",
                                          "Humedad",
                                          "Mala iluminacion",
                                          "Sin AA/Calefaccion"
                                  }),

                        new ExpansibleListViewDataAdapter("Problema de comunicaciones",
                                new String[]{
                                        "Causa"
                                }),
                        new ExpansibleListViewDataAdapter("Problema operativo",
                                new String[]{
                                        "Sin insumos","Sin billetes",
                                        "Mala calidad de billetes",
                                        "Mala calidad de insumos",
                                        "Error de operador"
                                }),
                        new ExpansibleListViewDataAdapter("Problema de vandalismo",
                                new String[]{
                                        "Problema de vandalismo"
                                }),
                        new ExpansibleListViewDataAdapter("Otros problemas",
                                new String[]{
                                        "Otros problemas"
                                })
                };

        listAdapter = new ExpansibleListAdapter(this,expansibleListViewDataAdapter);
        expandableListView.setAdapter(listAdapter);
        expandableListView.setOnChildClickListener(this);

        final ArrayList<CheckBox> itemsChecked = new ArrayList<>();
        this.WorkOrder = (EditText) findViewById(R.id.WorkOrder);
        this.idEquipo = (EditText) findViewById(R.id.idEquipo);
        this.serie = (EditText) findViewById(R.id.serie);
        this.editText_comentario = (EditText) findViewById(R.id.editText_comentario);
        editText_contacto = (EditText) findViewById(R.id.editText_contacto);
        editText_parte = (EditText)findViewById(R.id.editText_parte);

        idEquipo.setOnFocusChangeListener(this);
        serie.setOnFocusChangeListener(this);
        WorkOrder.setOnFocusChangeListener(this);
        super.initializeFab();
        loadSpinner(client);

    }



    @Override
    public void onFragmentInteraction(Uri uri) {

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
                if (validate() && ExpansibleListAdapter.selectedGroup.size() > 0
                        && ExpansibleListAdapter.selectedChild.size() > 0) {


                    String appVersion = null;
                    try {
                         appVersion =
                                AppController.getInstance().getAppVersion();
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                    if (AppController.getInstance().checkCredentials()){

                       logModel.setAppVersion(appVersion);
                        logModel.setTxtFecha(new Date().toString());
                        logModel.setTxtCsrCode(new WebFormsPreferencesManager(this).getCsrCode());
                        logModel.setSelCliente(client.getSelectedItem().toString());
                        logModel.setTxtWO(WorkOrder.getText().toString());
                        logModel.setTxtContacto(editText_contacto.getText().toString());
                        logModel.setTxtComentario(editText_comentario.getText().toString());
                        logModel.setTxtParte(editText_parte.getText().toString());
                        logModel.setTxtSerie(serie.getText().toString());
                        logModel.setFormID("Environmental Site");


                        form = new EnvironmentalSiteForm();
                        form.setCliente(client.getSelectedItem().toString());
                        form.setIdEquipo(idEquipo.getText().toString());
                        form.setSerie(serie.getText().toString());
                        form.setWorkOrder(WorkOrder.getText().toString());
                        form.setComentario(editText_comentario.getText().toString());
                        form.setParte(editText_parte.getText().toString());
                        form.setContacto(editText_contacto.getText().toString());
                        email.setSubject("Nuevo Formulario - Environmental Site");
                        email.setRecipients(getContacts());
                        email.setFrom(new WebFormsPreferencesManager(this).getUserName());
                        email.bodyMaker(form);
                        email.setForm(logModel);
                        createEmail();
                    }else{
                        Intent intent = new Intent(this, Stepper.class);
                        startActivity(intent);
                    }

                }
                break;
        }

        return true;
    }


    @Override
    public void fragmentCallback(java.util.Date date) {

    }

     @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

        Toast.makeText(EnvironmentalSiteActivity.this,"Clicked on child"
                ,Toast.LENGTH_SHORT).show();
        return true;
    }
}
