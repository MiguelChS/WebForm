package models;

import mc185249.webforms.IWebForm;

/**
 * Created by jn185090 on 5/31/2016.
 */
public class CambioPidPadForm implements IWebForm {
    private String workOrder,custRef,siteName,nroPos,serieSaliente,serieEntrante;

    public CambioPidPadForm() {
    }

    public CambioPidPadForm(String workOrder, String custRef,
                            String siteName, String nroPos,
                            String serieSaliente, String serieEntrante) {
        this.workOrder = workOrder;
        this.custRef = custRef;
        this.siteName = siteName;
        this.nroPos = nroPos;
        this.serieSaliente = serieSaliente;
        this.serieEntrante = serieEntrante;
    }

    public String getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrder(String workOrder) {
        this.workOrder = workOrder;
    }

    public String getCustRef() {
        return custRef;
    }

    public void setCustRef(String custRef) {
        this.custRef = custRef;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getNroPos() {
        return nroPos;
    }

    public void setNroPos(String nroPos) {
        this.nroPos = nroPos;
    }

    public String getSerieSaliente() {
        return serieSaliente;
    }

    public void setSerieSaliente(String serieSaliente) {
        this.serieSaliente = serieSaliente;
    }

    public String getSerieEntrante() {
        return serieEntrante;
    }

    public void setSerieEntrante(String serieEntrante) {
        this.serieEntrante = serieEntrante;
    }
}
