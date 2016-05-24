package com.example.mc185249.webforms;

import java.util.ArrayList;

/**
 * Created by mc185249 on 4/11/2016.
 */
public class LogisticsSurveyForm implements IWebForm {
    private String workOrder, parte, refReparador, retorno, comentario;

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getParte() {
        return parte;
    }

    public void setParte(String parte) {
        this.parte = parte;
    }

    public String getRefReparador() {
        return refReparador;
    }

    public void setRefReparador(String refReparador) {
        this.refReparador = refReparador;
    }

    public String getRetorno() {
        return retorno;
    }

    public void setRetorno(String retorno) {
        this.retorno = retorno;
    }

    public String getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrder(String workOrder) {
        this.workOrder = workOrder;
    }
}
