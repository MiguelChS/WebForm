package mc185249.webforms;

import android.content.ContentResolver;
import android.content.Context;
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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.innodroid.expandablerecycler.ExpandableRecyclerAdapter;

import org.w3c.dom.Text;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import adapters.CheckListAdapter;
import adapters.ExpansibleListAdapter;
import adapters.ExpansibleListViewDataAdapter;
import app.AppController;
import eu.inmite.android.lib.validations.form.annotations.Custom;
import eu.inmite.android.lib.validations.form.annotations.MinLength;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;
import eu.inmite.android.lib.validations.form.annotations.RegExp;
import eu.inmite.android.lib.validations.form.iface.IValidator;
import eu.inmite.android.lib.validations.form.validators.ValidatorFactory;
import mc185249.webforms.WebFormsPreferencesManager;
import models.Cliente;
import models.EmailSender;
import models.EnvironmentalSiteForm;
import models.WebFormsLogModel;

public class EnvironmentalSiteActivity extends mc185249.webforms.WebFormsActivity
        implements WorkOrderFragment.OnFragmentInteractionListener,
        MenuItem.OnMenuItemClickListener {


    public static WebFormsLogModel logModel;

    @NotEmpty(messageId = R.string.validation_text)
    @MinLength(value = 9,messageId = R.string.min_length_workOrder)
    @RegExp(value = "[0-9]",messageId = R.string.min_length_workOrder)
    EditText WorkOrder;

    @NotEmpty(messageId = R.string.validation_text)
    EditText serie, idEquipo, editText_contacto,
            editText_comentario;
    Spinner client;
    @NotEmpty(messageId = R.string.validation_text)
    @RegExp(value = "[0-9]",messageId = R.string.campo_numerico)
    EditText editText_parte;
    EnvironmentalSiteForm form;
    /**
     * almaceno los textos de los checkbox seleccionados
     */
    ArrayList<String> problems = new ArrayList<>();

    //region problema_electrico_layout
    LinearLayout electrico_wrapper;
    CheckBox voltajeNoRegulado, noPoseeUPS,tierraFisica,energiaElectrica;
    @Custom(value = ProblemaElectricoValidator.class, messageId = R.string.tension)
    public EditText tensionFN, tensionFT, tensionNT;
    TextView header_electrico;
    //endregion

    //region problema-site_layout
    LinearLayout siteWrapper;
    CheckBox suciedad, goteras, plagas, expSol, humedad, malaIluminacion, sinAA;
    TextView headerComunicaciones;
    //endregion

    //region problema_comunicaciones_layout
    LinearLayout comunicaciones_wrapper;
    Spinner comunicaciones;
    TextView headerOperativo;
    //endregion

    //region problema_operativo_layout
    LinearLayout operativoWrapper;
    TextView headerSite;
    CheckBox sinInsumos, sinBilletes, malaCalidadBilletes, malaCalidadInsumos, errorOperador;
    //endregion
    CheckBox problemaVandalismo, otrosProblemas;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        email = new EmailSender(this);
        logModel = new WebFormsLogModel();

        setContentView(R.layout.activity_environmental_site);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.client = (Spinner) findViewById(R.id.cliente);
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

        electrico_wrapper = (LinearLayout) findViewById(R.id.electrico_wrapper);
        siteWrapper = (LinearLayout) findViewById(R.id.siteWrapper);
        comunicaciones_wrapper = (LinearLayout) findViewById(R.id.comunicaciones_wrapper);
        operativoWrapper = (LinearLayout) findViewById(R.id.operativoWrapper);

        /**
         * Creo una instancia de los textBox en cada fragment, y a cada uno le seteo un evento de click para abrir o cerrar la lista de checkbox
         *
         */
        this.header_electrico = (TextView) findViewById(R.id.header_electrico);
        this.headerComunicaciones = (TextView)findViewById(R.id.header_comunicaciones);
        this.headerSite = (TextView)findViewById(R.id.headerSite);
        this.headerOperativo = (TextView)findViewById(R.id.header_operativo);
        this.header_electrico.setOnClickListener(wrapper_listener);
        this.headerComunicaciones.setOnClickListener(wrapper_listener);
        this.headerSite.setOnClickListener(wrapper_listener);
        this.headerOperativo.setOnClickListener(wrapper_listener);

        /**
         * inicializo todas las vistas y controles dentro de las cehcklist
         */
        this.voltajeNoRegulado = (CheckBox)findViewById(R.id.voltajeNoRegulado);
        this.tensionFN = (EditText) findViewById(R.id.tensionFN);
        this.tensionFT = (EditText)findViewById(R.id.tensionFT);
        this.tensionNT = (EditText)findViewById(R.id.tensionNT);
        this.noPoseeUPS = (CheckBox)findViewById(R.id.noPoseeUPS);
        this.tierraFisica = (CheckBox)findViewById(R.id.tierraFisicaa);
        this.energiaElectrica = (CheckBox)findViewById(R.id.energiaElectrica);
        this.suciedad = (CheckBox)findViewById(R.id.suciedad);
        this.goteras = (CheckBox)findViewById(R.id.goteras);
        this.plagas = (CheckBox)findViewById(R.id.plagas);
        this.expSol = (CheckBox)findViewById(R.id.expSol);
        this.humedad = (CheckBox)findViewById(R.id.humedad);
        this.malaIluminacion = (CheckBox)findViewById(R.id.malaIluminacion);
        this.sinAA = (CheckBox)findViewById(R.id.SinAACalefaccion);
        this.comunicaciones = (Spinner)findViewById(R.id.comunicaciones);
        this.sinInsumos = (CheckBox)findViewById(R.id.sinInsumos);
        this.sinBilletes = (CheckBox)findViewById(R.id.sinBilletes);
        this.malaCalidadBilletes = (CheckBox)findViewById(R.id.malaCalidadBilletes);
        this.malaCalidadInsumos = (CheckBox)findViewById(R.id.malaCalidadInsumos);
        this.errorOperador = (CheckBox)findViewById(R.id.errorOperador);
        this.problemaVandalismo = (CheckBox)findViewById(R.id.problemaVandalismo);
        this.otrosProblemas = (CheckBox) findViewById(R.id.otrosProblemas);

        //region set eventos a checkbox
        voltajeNoRegulado.setOnCheckedChangeListener(checkBox_click);
        noPoseeUPS.setOnCheckedChangeListener(checkBox_click);
        tierraFisica.setOnCheckedChangeListener(checkBox_click);
        energiaElectrica.setOnCheckedChangeListener(checkBox_click);
        suciedad.setOnCheckedChangeListener(checkBox_click);
        goteras.setOnCheckedChangeListener(checkBox_click);
        plagas.setOnCheckedChangeListener(checkBox_click);
        expSol.setOnCheckedChangeListener(checkBox_click);
        humedad.setOnCheckedChangeListener(checkBox_click);
        malaIluminacion.setOnCheckedChangeListener(checkBox_click);
        sinAA.setOnCheckedChangeListener(checkBox_click);
        sinInsumos.setOnCheckedChangeListener(checkBox_click);
        sinBilletes.setOnCheckedChangeListener(checkBox_click);
        malaCalidadBilletes.setOnCheckedChangeListener(checkBox_click);
        malaCalidadInsumos.setOnCheckedChangeListener(checkBox_click);
        errorOperador.setOnCheckedChangeListener(checkBox_click);
        problemaVandalismo.setOnCheckedChangeListener(checkBox_click);
        otrosProblemas.setOnCheckedChangeListener(checkBox_click);
        //endregion
    }

    /**
     * Evento click para abrir o cerra las checklist
     */
    View.OnClickListener wrapper_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

           switch (v.getId()){
               case R.id.header_electrico:
                   toggleVisibility(electrico_wrapper);
                   break;
               case R.id.header_comunicaciones:
                   toggleVisibility(comunicaciones_wrapper);
                   break;
               case R.id.header_operativo:
                   toggleVisibility(operativoWrapper);
                   break;
               case R.id.headerSite:
                   toggleVisibility(siteWrapper);
                   break;
           }
        }

        public void toggleVisibility(View  v){
            if (v.getVisibility() == View.GONE){
                v.setVisibility(View.VISIBLE);
            }else{
                v.setVisibility(View.GONE);
            }
        }
    };

    CompoundButton.OnCheckedChangeListener checkBox_click = new CompoundButton.OnCheckedChangeListener() {

        private void toggle(){

        }
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked){
                problems.add(
                        buttonView.getText().toString()
                );
                switch (buttonView.getId()){
                    case R.id.voltajeNoRegulado:
                        logModel.setChkVolNoRegulado(1);
                        break;
                    case R.id.noPoseeUPS:
                        logModel.setChkNoUps(1);
                        break;
                    case R.id.tierraFisicaa:
                        logModel.setChkNoTierraFisica(1);
                        break;
                    case R.id.energiaElectrica:
                        logModel.setChkNoEnergia(1);
                        break;
                    case R.id.suciedad:
                        logModel.setChkSuciedad(1);
                        break;
                    case R.id.goteras:
                        logModel.setChkGoteras(1);
                        break;
                    case R.id.plagas:
                        logModel.setChkPlagas(1);
                        break;
                    case R.id.expSol:
                        logModel.setChkExpSol(1);
                        break;
                    case R.id.humedad:
                        logModel.setChkHumedad(1);
                        break;
                    case R.id.malaIluminacion:
                        logModel.setChkMalaIluminacion(1);
                        break;
                    case R.id.SinAACalefaccion:
                        logModel.setChkNoAA(1);
                        break;
                    case R.id.sinInsumos:
                        logModel.setChkSinInsumos(1);
                        break;
                    case R.id.sinBilletes:
                        logModel.setChkSinBilletes(1);
                        break;
                    case R.id.malaCalidadBilletes:
                        logModel.setChkMalaCalidadBilletes(1);
                        break;
                    case R.id.malaCalidadInsumos:
                        logModel.setChkMalaCalidadInsumos(1);
                        break;
                    case R.id.errorOperador:
                        logModel.setChkErrorOperador(1);
                        break;
                    case R.id.problemaVandalismo:
                        logModel.setChkProVandalismo(1);
                        break;
                    case R.id.otrosProblemas:
                        logModel.setChkProOtros(1);
                        break;
                }
            }else{
                int index =
                        problems.indexOf(buttonView.getText().toString());
                problems.remove(index);
                switch (buttonView.getId()){
                    case R.id.voltajeNoRegulado:
                        logModel.setChkVolNoRegulado(0);
                        break;
                    case R.id.noPoseeUPS:
                        logModel.setChkNoUps(0);
                        break;
                    case R.id.tierraFisicaa:
                        logModel.setChkNoTierraFisica(0);
                        break;
                    case R.id.energiaElectrica:
                        logModel.setChkNoEnergia(0);
                        break;
                    case R.id.suciedad:
                        logModel.setChkSuciedad(0);
                        break;
                    case R.id.goteras:
                        logModel.setChkGoteras(0);
                        break;
                    case R.id.plagas:
                        logModel.setChkPlagas(0);
                        break;
                    case R.id.expSol:
                        logModel.setChkExpSol(0);
                        break;
                    case R.id.humedad:
                        logModel.setChkHumedad(0);
                        break;
                    case R.id.malaIluminacion:
                        logModel.setChkMalaIluminacion(0);
                        break;
                    case R.id.SinAACalefaccion:
                        logModel.setChkNoAA(0);
                        break;
                    case R.id.sinInsumos:
                        logModel.setChkSinInsumos(0);
                        break;
                    case R.id.sinBilletes:
                        logModel.setChkSinBilletes(0);
                        break;
                    case R.id.malaCalidadBilletes:
                        logModel.setChkMalaCalidadBilletes(0);
                        break;
                    case R.id.malaCalidadInsumos:
                        logModel.setChkMalaCalidadInsumos(0);
                        break;
                    case R.id.errorOperador:
                        logModel.setChkErrorOperador(0);
                        break;
                    case R.id.problemaVandalismo:
                        logModel.setChkProVandalismo(0);
                        break;
                    case R.id.otrosProblemas:
                        logModel.setChkProOtros(0);
                        break;
                }

            }
        }
    };

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

                        String str_workOrder = "W" + WorkOrder.getText().toString();
                       logModel.setAppVersion(appVersion);
                        logModel.setTxtFecha(new Date().toString());
                        logModel.setTxtCsrCode(new WebFormsPreferencesManager(this).getCsrCode());
                        logModel.setSelCliente(client.getSelectedItem().toString());
                        logModel.setTxtWO(str_workOrder);
                        logModel.setTxtContacto(editText_contacto.getText().toString());
                        logModel.setTxtComentario(editText_comentario.getText().toString());

                        logModel.setTxtParte(editText_parte.getText().toString());
                        logModel.setTxtSerie(serie.getText().toString());
                        logModel.setFormID("Environmental Site");


                        form = new EnvironmentalSiteForm();
                        form.setCliente(client.getSelectedItem().toString());
                        form.setIdEquipo(idEquipo.getText().toString());
                        form.setSerie(serie.getText().toString());
                        form.setWorkOrder(str_workOrder);
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


    /**
     * Valida ,en caso de haberse checkeado voltajeNoRegulado, que los campos tensionFN, tensionFT,tensionNT no esten vacios
     * @see #tensionFN
     * @see #tensionFT
     * @see #tensionNT
     *
     */
    class ProblemaElectricoValidator implements IValidator<String>{
        Boolean problemaElectricoChecked = false;

        public Boolean getProblemaElectricoChecked() {
            return problemaElectricoChecked;
        }

        public void setProblemaElectricoChecked(Boolean problemaElectricoChecked) {
            this.problemaElectricoChecked = problemaElectricoChecked;
        }

        @Override
        public boolean validate(Annotation annotation, String input) {
            if (!problemaElectricoChecked){
                return true;
            }
            if (tensionFN != null && !tensionFN.getText().toString().isEmpty()
                    && tensionNT != null && !tensionNT.getText().toString().isEmpty()
                    && tensionFT != null && !tensionFT.getText().toString().isEmpty()){
                return true;
            }
            return false;
        }

        @Override
        public String getMessage(Context context, Annotation annotation, String input) {
            return "Los campos Tension NT, Tension FN y Tension FT no deben estar vacios.";
        }

        @Override
        public int getOrder(Annotation annotation) {
            return 0;
        }
    }
}
