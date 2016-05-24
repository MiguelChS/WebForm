package models;

import com.example.mc185249.webforms.IWebForm;
import com.example.mc185249.webforms.LogisticsSurveyForm;
import com.example.mc185249.webforms.MantenimientoSurveyForm;
import com.example.mc185249.webforms.MemoriaFiscalForm;

import java.io.File;
import java.util.ArrayList;


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
    protected WebFormsLogModel form;

    public ArrayList<models.File> getFiles() {
        return files;
    }

    public void removeAttachFile(File file){
        this.files.remove(file);
    }

    public Email(String body, String subject, String[] to) {
        this.body = body;
        this.subject = subject;
        setRecipients(to);
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
        String b = "";

        if (body instanceof EnvironmentalSiteForm){
            String problems = "";
            for (String str:
                 ((EnvironmentalSiteForm) body).getProblems()) {

                problems += str + "\n";
            }
            b = "WorkOrder: " + ((EnvironmentalSiteForm) body).getWorkOrder() + "\n"+
                    "#Serie: " + ((EnvironmentalSiteForm) body).getSerie() + "\n"+
                    "ID Equipo: " + ((EnvironmentalSiteForm) body).getIdEquipo() + "\n"+
                    "Cliente: " + ((EnvironmentalSiteForm) body).getCliente() +"\n\n" +
                     problems + "\n" +
                    "Comentario" + "\n" + "==========" +
                    ((EnvironmentalSiteForm) body).getComentario();

        }

        if (body instanceof LogisticsSurveyForm){
            b = "Work Order: " + ((LogisticsSurveyForm) body).getWorkOrder() + "\n"+
                    "#Parte: " + ((LogisticsSurveyForm) body).getParte() + "\n"+
                    "#Retorno: "+ ((LogisticsSurveyForm) body).getRetorno() + "\n"+
                    "Ref.Reparador: " + ((LogisticsSurveyForm) body).getRefReparador() + "\n"+
                    "Comentario: " + ((LogisticsSurveyForm) body).getComentario();
        }

        if (body instanceof MantenimientoSurveyForm){
            b = "Work Order: " + ((MantenimientoSurveyForm) body).getWorkOrder() + "\n"+
                    "Cliente: " + ((MantenimientoSurveyForm) body).getCliente() + "\n"+
                    "ID Equipo: " + ((MantenimientoSurveyForm) body).getEquipo() + "\n"+
                    "#Serie: " + ((MantenimientoSurveyForm) body).getSerie() + "\n" +
                    "Fecha: " + ((MantenimientoSurveyForm) body).getFecha() + "\n"+
                    "Comentario: " + ((MantenimientoSurveyForm) body).getComentario();
        }

        if (body instanceof MemoriaFiscalForm){
            b = "Work Order: " + ((MemoriaFiscalForm) body).getWorkOrder() + "\n" +
                    "Cliente: "+ ((MemoriaFiscalForm) body).getCliente() +"\n"+
                    "Cust Ref#: " + ((MemoriaFiscalForm) body).getCustRef() + "\n" +
                    "Site Name: " + ((MemoriaFiscalForm) body).getSiteName() + "\n" +
                    "Nro.Punto de venta: " + ((MemoriaFiscalForm) body).getPuntoVenta() + "\n" +
                    "Ver.Firmware placa fiscal: " + ((MemoriaFiscalForm) body).getFirmwarePlaca() + "\n" +
                   "Serie impresor: " + ((MemoriaFiscalForm) body).getSerieImpresor();
        }

        this.body = b;

    }
}
