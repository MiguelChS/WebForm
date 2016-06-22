package models;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;

import com.google.api.client.util.DateTime;

import connectivity.EmailTask;
import mc185249.webforms.AttachementProvider;
import mc185249.webforms.EmailsProvider;
import mc185249.webforms.IWebForm;
import mc185249.webforms.LogProvider;
import mc185249.webforms.LogisticsSurveyForm;
import mc185249.webforms.MantenimientoSurveyForm;
import mc185249.webforms.MemoriaFiscalForm;
import mc185249.webforms.VisitaTecnica;
import mc185249.webforms.WebFormsPreferencesManager;
import microsoft.exchange.webservices.data.core.exception.misc.ArgumentException;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;


/**
 * Created by mc185249 on 3/31/2016.
 */
public class  Email {


    protected String recipients;
    protected String from = "";
    protected String body = "";
    protected Sender sender;
    protected String subject = "";
    protected boolean hasAttachment = false;
    protected ArrayList<models.File> files = new ArrayList<>();
    protected WebFormsLogModel form = null;
    protected String CSRCode = "";
    protected String activity;
    protected Date fecha;
    protected int id;

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }


    /**
     * 0 = guardado localmente,
     * 1 = guardado localmente pero no fue posible enviar al servidor,
     * 2 = sincronizado con el servidor. Pendiente de envio.
     * 3 = email enviado
     */
    protected int currentState;

    public ArrayList<models.File> getFiles() {
        return files;
    }
    public int countAttachment(){
        return (files == null) ? 0 : files.size();
    }

    public int get_hasAttachment(){
        return (hasAttachment) ? 1 : 0;
    }

    public void removeAttachFile(File file){
        this.files.remove(file);
    }

    public Email(String body, String subject, String[] to) {
        this.body = body;
        this.subject = subject;
        setRecipients(to);

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCurrentState() {
        return currentState;
    }

    /**
     *
     * @param currentState
     * 0 = guardado localmente,
     * 1 = guardado localmente pero no fue posible enviar al servidor,
     * 2 = sincronizado con el servidor. Pendiente de envio.
     * 3 = email enviado
     */
    public void setCurrentState(int currentState) {
        if (currentState > 3 || currentState < 0){
            throw new ArgumentException("currentState unicamente puede ser 0,1,2,3");
        }
        this.currentState = currentState;
    }

    public void setCSRCode(String csrCode){
        this.CSRCode = csrCode;
    }
    public String getCSRCode(){
        return this.CSRCode;
    }

   public void Attach(models.File file) throws ArrayIndexOutOfBoundsException{
       if(this.files.size() == 4){
           throw new ArrayIndexOutOfBoundsException("Max fotos adjuntadas!");
       }

       this.files.add(file);
       this.hasAttachment = true;
   }
    public Email(){
        
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public  String getFrom() {
        return from;
    }

    public  void setFrom(String from) {
        if (from == null || from.isEmpty()){
            this.from = "s/d";
            return;
        }
       this.from = from;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getRecipients() {
        return recipients;
    }

    public void setRecipients(String[] recipients) {
        if (recipients == null) return;
        StringBuilder str = new StringBuilder();
        int count = 0;
        if (recipients.length == 1){
            this.recipients = recipients[0];
            return;
        }
        for (String r:
             recipients) {

            if (count != 0)
                str.append(',');

            str.append(r);
            count++;
        }
        this.recipients = str.toString();
    }

    public void setSender(Sender sender){
        this.sender = sender;
    }
    public Sender getSender(){
        return sender;
    }
    public void setForm(WebFormsLogModel form1){
        this.form = form1;
    }
    public WebFormsLogModel getForm(){
        return form;
    }


    /**
     * Construye el cuerpo del email.
     * @param body
     * @see IWebForm
     */
    public void bodyMaker(IWebForm body){
        //region body

        String b =
                "<html>"+
                "<head>\n" +
                        "<link href=\"https://fonts.googleapis.com/css?family=Open+Sans\"" +
                        " rel=\"stylesheet\" type=\"text/css\">"+
                        "<style type=\"text/css\">\n" +
                        "*{font-family: \"Open Sans\", sans-serif;}"+
                        "    .wrapper {\n" +
                        "    background: white;\n" +
                        "    padding: 16px;\n" +
                        "}\n" +
                        "\n" +
                        "#right_content {\n" +
                        "    display: inline-block;\n" +
                        "    vertical-align: top;\n" +
                        "    padding: 16px;\n" +
                        "    padding-top: 0;\n" +
                        "}\n" +
                        "\n" +
                        "#bottom label {\n" +
                        "    display: block;\n" +
                        "    padding: 0 0px 5px 0px;\n" +
                        "}\n" +
                        "  </style>\n" +
                        "</head>"+
                "<body>\n" +
                "  <div class=\"wrapper\">\n" +
                "  <img style=\"display:inline-block\" src=\"http://www.ncr.com/wp-content/themes/ncr-dotcom-wp-theme_STRIPPED/_assets/images/logo_116x116.jpg\" alt=\"NCR Logo\">\n" +
                " \n" +
                        " <div id=\"right_content\">\n" +
                        "  <h2>" + getSubject() + "</h2>\n" +
                        "   <h3>%s</h3>\n" +
                        "   \n" +
                        " </div>"+
                " <div id=\"bottom\">\n" +
                "    <label for=\"\">\n" +
                "      CSR CODE : %s\n" +
                "  </label>\n" +
                "  <label for=\"\">\n" +
                "    WorkOrder:%s\n" +
                "  </label>\n" +
                "  <label for=\"\">\n" +
                "    #Serie: %s\n" +
                "  </label>\n" +
                "  <label for=\"\">\n" +
                "    ID Equipo: %s\n" +
                "  </label>\n" +
                "  <label for=\"\">\n" +
                "    Cliente: %s\n" +
                "  </label>\n" +
                "  <label for=\"\">\n" +
                "    TICKET ID:%s\n" +
                "  </label>\n" +
                "  <label for=\"\">\n" +
                "    PHONE NUMBER: %s\n" +
                "  </label>\n" +
                "  <label for=\"\">\n" +
                "    Ref. Reparador: %s\n" +
                "  </label>\n" +
                "  <label for=\"\">\n" +
                "    #Parte: %s\n" +
                "  </label>\n" +
                "  <label for=\"\">\n" +
                "    #Retorno: %s\n" +
                "  </label>\n" +
                "  <label for=\"\">\n" +
                "    Fecha: %s\n" +
                "  </label>\n" +
                "  <label for=\"\">\n" +
                "    Motivo del Daño :%s \n" +
                "  </label>\n" +
                " </div>\n" +
                "</div>\n" +
                "  \n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "</body>"+
                "</html>";
        //endregion

        Formatter formatter = new Formatter();
        String createdBody = "body";
        if (body instanceof EnvironmentalSiteForm){
            String problems = "";
            for (String str:
                 ((EnvironmentalSiteForm) body).getProblems()) {

                problems += str + "\n";
            }

            createdBody = formatter.format(b,
                    ((EnvironmentalSiteForm) body).getCliente(),
                    CSRCode,
                    ((EnvironmentalSiteForm) body).getWorkOrder(),
                    ((EnvironmentalSiteForm) body).getSerie(),
                    ((EnvironmentalSiteForm) body).getIdEquipo(),
                    ((EnvironmentalSiteForm) body).getCliente(),
                   null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null).toString();

        }

        if (body instanceof LogisticsSurveyForm){

            createdBody = formatter.format(b,
                    "",
                    CSRCode,
                    ((LogisticsSurveyForm) body).getWorkOrder(),
                    "",
                    "",
                    "",
                    "",
                    "",
                    ((LogisticsSurveyForm) body).getRefReparador(),
                    ((LogisticsSurveyForm) body).getParte(),
                    ((LogisticsSurveyForm) body).getRetorno(),
                    "",
                    "").toString();


        }

        if (body instanceof MantenimientoSurveyForm){

            createdBody = formatter.format(b,
                    "",
                    CSRCode,
                    ((MantenimientoSurveyForm) body).getWorkOrder(),
                    ((MantenimientoSurveyForm) body).getSerie(),
                    ((MantenimientoSurveyForm) body).getEquipo(),
                    ((MantenimientoSurveyForm) body).getCliente(),
                    "",
                    "",
                    "",
                    "",
                    "",
                    ((MantenimientoSurveyForm) body).getFecha(),
                    "").toString();
        }

        if (body instanceof MemoriaFiscalForm){
            createdBody = formatter.format(b,
                    ((MemoriaFiscalForm) body).getCliente(),
                    CSRCode,
                    ((MemoriaFiscalForm) body).getWorkOrder(),
                    "",
                    "",
                    ((MemoriaFiscalForm) body).getCliente(),
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "").toString();
        }

        if (body instanceof CambioPidPadForm){

            createdBody = formatter.format(b,
                    "",
                    CSRCode,
                    ((CambioPidPadForm) body).getWorkOrder(),
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "").toString();
        }

        if (body instanceof VisitaTecnicaForm){

            createdBody = formatter.format(b,
                    "",
                    ((VisitaTecnicaForm) body).getCliente(),
                    CSRCode,
                    ((VisitaTecnicaForm) body).getWorkOrder(),
                    ((VisitaTecnicaForm) body).getSerie(),
                    ((VisitaTecnicaForm) body).getEquipo(),
                    ((VisitaTecnicaForm) body).getCliente(),
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "").toString();
        }
        if (body instanceof TecladoEncryptorModel){
            createdBody = formatter.format(b,
                    "",
                    ((TecladoEncryptorModel) body).getCliente(),
                    CSRCode,
                    ((TecladoEncryptorModel) body).getWorkOrder(),
                    ((TecladoEncryptorModel) body).getSerie(),
                    ((TecladoEncryptorModel) body).getEquipo(),
                    ((TecladoEncryptorModel) body).getCliente(),
                    "",
                    "",
                    "",
                    "","","",""

            ).toString();
        }

        if (body instanceof DevolucionPartes){
            createdBody = formatter.format(b,
                    "",
                    CSRCode,
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    ((DevolucionPartes) body).getParte(),
                    "",
                    ((DevolucionPartes) body).fechaToString(),
                    ""

                    ).toString();
        }

        this.body = createdBody;

    }

    public static ArrayList<EmailSender> readEmails(Context mContext){
        ArrayList<EmailSender> emailsList = new ArrayList<>();
        String URL = "content://mc185249.webforms.EmailsProvider/emails";
        Uri emails = Uri.parse(URL);
        ContentResolver contentResolver = mContext.getContentResolver();
        Cursor cursor = contentResolver.query(emails,null,null,null,null);
        if (cursor != null
                && cursor.moveToFirst()){

            do{
                WebFormsPreferencesManager preferencesManager =
                        new WebFormsPreferencesManager(mContext);

                String account =
                        preferencesManager.getUserName();
                if (account != null
                        && !account.isEmpty()){
                    EmailSender emailSender = null;
                    emailSender = new EmailSender(mContext);

                    emailSender.activity = cursor.getString(cursor.getColumnIndex(EmailsProvider.ACTIVITY));
                    emailSender.setSubject(cursor.getString(cursor.getColumnIndex(mc185249.webforms.EmailsProvider.SUBJECT)));
                    emailSender.setBody(cursor.getString(cursor.getColumnIndex(mc185249.webforms.EmailsProvider.BODY)));
                    emailSender.setRecipients(new String[]{cursor.getString(cursor.getColumnIndex(mc185249.webforms.EmailsProvider.RECIPIENT))});
                    emailSender.setFrom(cursor.getString(cursor.getColumnIndex(mc185249.webforms.EmailsProvider.FROM)));
                    emailSender.setPasswordAuthentication(account.trim(), "");
                    emailSender.setCurrentState(cursor.getInt(cursor.getColumnIndex(EmailsProvider.CURRENT_STATE)));
                    String fecha = cursor.getString(cursor.getColumnIndex(EmailsProvider.FECHA));
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    try {
                        emailSender.setFecha(dateFormat.parse(fecha));
                    } catch (ParseException e) {
                        emailSender.setFecha(new Date());
                    }
                    emailSender.setId(cursor.getInt(cursor.getColumnIndex(EmailsProvider.ID)));
                    emailSender.setSender(
                            new Sender(
                                    "",
                                    preferencesManager.getUserName(),
                                    preferencesManager.getUserName(),
                                    preferencesManager.getCsrCode()
                            )
                    );
                    int CustomEmailID = cursor.
                            getInt(cursor.getColumnIndex(EmailsProvider.ID));

                    LogProvider logProvider =
                            new LogProvider(mContext);
                    WebFormsLogModel[] logs = logProvider.query(null,new String[]{
                            String.valueOf(CustomEmailID)
                    },(LogProvider.emailID + " = ?"));

                    if (logs.length > 0)
                        emailSender.setForm(logs[0]);

                    Uri attachment_files = Uri.parse(AttachementProvider.URL);
                    ContentResolver contentResolver1 = mContext.getContentResolver();
                    Cursor mCursor = contentResolver1.query(
                            attachment_files,null, mc185249.webforms.AttachementProvider.EMAIL_ID + " = ?",
                            new String[]{
                                    String.valueOf(cursor.getInt(cursor.getColumnIndex(mc185249.webforms.EmailsProvider.ID)))
                            },null
                    );

                    if(mCursor.moveToFirst()){
                        do{
                            byte[] encodedFile = mCursor
                                    .getBlob
                                            (mCursor
                                                    .getColumnIndex(
                                                            AttachementProvider.BLOB));


                            emailSender
                                    .Attach(new models
                                            .File(
                                            ("WebFormsIMG_" + Calendar.getInstance()
                                                    .get(Calendar.SECOND)+ "_"+
                                                    new WebFormsPreferencesManager(mContext)
                                                            .getCsrCode())
                                            , Base64.encodeToString(encodedFile,0)
                                            , BitmapFactory
                                            .decodeByteArray(
                                                    encodedFile,0,encodedFile.length
                                            )
                                    ));


                        }while(mCursor.moveToNext());
                    }
                    emailsList.add(emailSender);
                }


            }while (cursor.moveToNext());

        }
        return emailsList;
    }

    /**
     *
     * @param mContext
     *  Context donde se llama a la funcion
     * @param currentState
     *  Acepta valores del 0 al 3.
     *  <ul>
     *      <li>0 = guardado localmente.</li>
     *      <li>1 = guardado localmente pero no fue posible enviar al servidor Por algun error.</li>
     *      <li>2 = sincronizado con el servidor. Pendiente de envio.</li>
     *      <li>3 = email enviado.</li>
     *  </ul>
     * @return
     * <div>
     *     Lista de EmailSender.
     * </div>
     * @see EmailSender
     */
    public static ArrayList<EmailSender> readEmails(Context mContext,int currentState){
        ArrayList<EmailSender> emailsList = new ArrayList<>();
        String URL = "content://mc185249.webforms.EmailsProvider/emails";
        Uri emails = Uri.parse(URL);
        ContentResolver contentResolver = mContext.getContentResolver();
        Cursor cursor = contentResolver.query(emails,null,
                EmailsProvider.CURRENT_STATE + " = " + currentState,null,null);
        if (cursor != null
                && cursor.moveToFirst()){

            do{
                WebFormsPreferencesManager preferencesManager =
                        new WebFormsPreferencesManager(mContext);

                String account =
                        preferencesManager.getUserName();
                if (account != null
                        && !account.isEmpty()){
                    EmailSender emailSender = null;
                    emailSender = new EmailSender(mContext);

                    emailSender.activity = cursor.getString(cursor.getColumnIndex(EmailsProvider.ACTIVITY));
                    emailSender.setSubject(cursor.getString(cursor.getColumnIndex(mc185249.webforms.EmailsProvider.SUBJECT)));
                    emailSender.setBody(cursor.getString(cursor.getColumnIndex(mc185249.webforms.EmailsProvider.BODY)));
                    emailSender.setRecipients(new String[]{cursor.getString(cursor.getColumnIndex(mc185249.webforms.EmailsProvider.RECIPIENT))});
                    emailSender.setFrom(cursor.getString(cursor.getColumnIndex(mc185249.webforms.EmailsProvider.FROM)));
                    emailSender.setPasswordAuthentication(account.trim(), "");
                    emailSender.setId(cursor.getInt(cursor.getColumnIndex(EmailsProvider.ID)));
                    emailSender.setSender(
                            new Sender(
                                    "",
                                    preferencesManager.getUserName(),
                                    preferencesManager.getUserName(),
                                    preferencesManager.getCsrCode()
                            )
                    );
                    int CustomEmailID = cursor.
                            getInt(cursor.getColumnIndex(EmailsProvider.ID));

                    LogProvider logProvider =
                            new LogProvider(mContext);
                    WebFormsLogModel[] logs = logProvider.query(null,new String[]{
                            String.valueOf(CustomEmailID)
                    },(LogProvider.emailID + " = ?"));

                    if (logs.length > 0)
                        emailSender.setForm(logs[0]);

                    Uri attachment_files = Uri.parse(AttachementProvider.URL);
                    ContentResolver contentResolver1 = mContext.getContentResolver();
                    Cursor mCursor = contentResolver1.query(
                            attachment_files,null, mc185249.webforms.AttachementProvider.EMAIL_ID + " = ?",
                            new String[]{
                                    String.valueOf(cursor.getInt(cursor.getColumnIndex(mc185249.webforms.EmailsProvider.ID)))
                            },null
                    );

                    if(mCursor.moveToFirst()){
                        do{
                            byte[] encodedFile = mCursor
                                    .getBlob
                                            (mCursor
                                                    .getColumnIndex(
                                                            AttachementProvider.BLOB));


                            emailSender
                                    .Attach(new models
                                            .File(
                                            ("WebFormsIMG_" + Calendar.getInstance()
                                                    .get(Calendar.SECOND)+ "_"+
                                                    new WebFormsPreferencesManager(mContext)
                                                            .getCsrCode())
                                            , Base64.encodeToString(encodedFile,0)
                                            , BitmapFactory
                                            .decodeByteArray(
                                                    encodedFile,0,encodedFile.length
                                            )
                                    ));


                        }while(mCursor.moveToNext());
                    }
                    emailsList.add(emailSender);
                }


            }while (cursor.moveToNext());

        }
        return emailsList;
    }


   private void formatBody(){

   }
}
