package models;

import mc185249.webforms.IWebForm;
import mc185249.webforms.LogisticsSurveyForm;
import mc185249.webforms.MantenimientoSurveyForm;
import mc185249.webforms.MemoriaFiscalForm;
import mc185249.webforms.VisitaTecnica;
import mc185249.webforms.WebFormsPreferencesManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Formatter;


/**
 * Created by mc185249 on 3/31/2016.
 */
public class  Email {


    /*API PARAMETERS TO POST
    * public class Mail
    {
        public bool hasAttachment { get; set; }
        public String body { get; set; }
        public String recipients { get; set; }
        public Sender sender { get; set; }
        public IList<File> files { get; set; }
        public Form form { get; set; }
    }

    public class Sender
    {
        public string passwd { get; set; }
        public string emailAddress { get; set; }
        public string name { get; set; }
        public string CSRCode { get; set; }
    }

    public class File
    {
        public string name { get; set; }
        public string blob { get; set; }
    }
    * */
    protected String recipients;
    protected String from = "";
    protected String body = "";
    protected Sender sender;
    protected String subject = "";
    protected boolean hasAttachment = false;
    protected ArrayList<models.File> files = new ArrayList<>();
    protected WebFormsLogModel form = null;
    protected String CSRCode = "";

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
    public Email(){}

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
        StringBuilder str = new StringBuilder();
        int count = 0;
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


    public void bodyMaker(IWebForm body){
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
                        "#right_content label {\n" +
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
            b = "Work Order: " + ((MemoriaFiscalForm) body).getWorkOrder() + "\n" +
                    "Cliente: "+ ((MemoriaFiscalForm) body).getCliente() +"\n"+
                    "Cust Ref#: " + ((MemoriaFiscalForm) body).getCustRef() + "\n" +
                    "Site Name: " + ((MemoriaFiscalForm) body).getSiteName() + "\n" +
                    "Nro.Punto de venta: " + ((MemoriaFiscalForm) body).getPuntoVenta() + "\n" +
                    "Ver.Firmware placa fiscal: " + ((MemoriaFiscalForm) body).getFirmwarePlaca() + "\n" +
                   "Serie impresor: " + ((MemoriaFiscalForm) body).getSerieImpresor();
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
            b = "WorkOrder: " + ((CambioPidPadForm) body).getWorkOrder() + "\n" +
                    "Cust Ref:" + ((CambioPidPadForm) body).getCustRef() + "\n" +
                    "Site Name:" + ((CambioPidPadForm) body).getSiteName() + "\n"+
                    "Nro de POS:" + ((CambioPidPadForm) body).getSiteName() + "\n"+
                    "#Serie Saliente:" + ((CambioPidPadForm) body).getSerieSaliente()+"\n"+
                    "#Serie Entrante:" + ((CambioPidPadForm) body).getSerieEntrante();

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
            b = "WorkOder: " + ((VisitaTecnicaForm) body).getWorkOrder() + "\n"+
                    "Cliente: " + ((VisitaTecnicaForm) body).getCliente() + "\n"+
                    "#Serie: " + ((VisitaTecnicaForm) body).getSerie() + "\n"+
                    "#Equipo: " + ((VisitaTecnicaForm) body).getEquipo();

            createdBody = formatter.format(b,
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
      /*  if (body instanceof TecladoEncryptorModel){
            b = "CSR CODE: " + new WebFormsPreferencesManager().getCsrCode()
        }*/

        this.body = createdBody;

    }
}
