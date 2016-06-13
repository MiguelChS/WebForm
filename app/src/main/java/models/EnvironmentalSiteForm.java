package models;



import java.util.ArrayList;

import mc185249.webforms.IWebForm;

/**
 * Created by mc185249 on 4/7/2016.
 */
public class EnvironmentalSiteForm implements IWebForm {
    String workOrder;
    String serie;
    String idEquipo;
    String cliente;
    String parte;
    String contacto;
    String comentario;
    public ArrayList<String> problems = new ArrayList<>();

    public String getParte() {
        return parte;
    }

    public void setParte(String parte) {
        this.parte = parte;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(String idEquipo) {
        this.idEquipo = idEquipo;
    }

    public ArrayList<String> getProblems() {
        return problems;
    }

    public void setProblems(ArrayList<String> problems) {
        this.problems = problems;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrder(String workOrder) {
        this.workOrder = workOrder;
    }
}
