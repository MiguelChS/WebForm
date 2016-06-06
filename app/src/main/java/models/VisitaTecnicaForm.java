package models;

import mc185249.webforms.IWebForm;

/**
 * Created by jn185090 on 5/31/2016.
 */
public class VisitaTecnicaForm implements IWebForm {
    private String workOrder,cliente,serie,equipo;

    public VisitaTecnicaForm() {
    }

    public VisitaTecnicaForm(String workOrder, String cliente,
                             String serie, String equipo) {
        this.workOrder = workOrder;
        this.cliente = cliente;
        this.serie = serie;
        this.equipo = equipo;
    }

    public String getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrder(String workOrder) {
        this.workOrder = workOrder;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getEquipo() {
        return equipo;
    }

    public void setEquipo(String equipo) {
        this.equipo = equipo;
    }
}
