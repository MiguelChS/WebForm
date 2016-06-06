package models;

import mc185249.webforms.IWebForm;

/**
 * Created by jn185090 on 6/1/2016.
 */
public class TecladoEncryptorModel implements IWebForm {

    private String workOrder,cliente,serie,equipo,motivoDaño,comentario;

    public TecladoEncryptorModel() {
    }

    public TecladoEncryptorModel(String workOrder, String cliente,
                                 String serie, String equipo,
                                 String motivoDaño, String comentario) {
        this.workOrder = workOrder;
        this.cliente = cliente;
        this.serie = serie;
        this.equipo = equipo;
        this.motivoDaño = motivoDaño;
        this.comentario = comentario;
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

    public String getMotivoDaño() {
        return motivoDaño;
    }

    public void setMotivoDaño(String motivoDaño) {
        this.motivoDaño = motivoDaño;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
