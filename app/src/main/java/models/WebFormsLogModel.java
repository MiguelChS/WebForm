package models;

import java.io.Serializable;

/**
 * Created by jn185090 on 5/17/2016.
 */
public class WebFormsLogModel {



    private String appVersion,txtFecha,txtCsrCode,txtIDATM,selCliente
            ,txtWO,txtSerie,txtContacto,txtParte,txtComentario,
             formID;

    private int chkProElectrico,chkVolNoRegulado,txtFN,txtFT,txtNT
            ,chkNoUps,chkNoTierraFisica,chkNoEnergia,chkProSite
            ,chkSuciedad,chkGoteras,chkPlagas,chkExpSol,chkHumedad
            ,chkMalaIluminacion,chkNoAA,chkProComms,selComunicaciones
            ,chkProOperativo,chkSinInsumos,chkSinBilletes,chkMalaCalidadBilletes
            ,chkMalaCalidadInsumos,chkErrorOperador,chkCargaIncPapel
            ,chkCargaIncCaseteras,chkSupervisor,chkErrorBalanceo
            ,chkProVandalismo,chkProOtros,chkProFotos,emailID;



    private int ID;

    public WebFormsLogModel() {

    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getFormID() {
        return formID;
    }

    public void setFormID(String formID) {
        this.formID = formID;
    }

    public int getEmailID() {
        return emailID;
    }

    public void setEmailID(int emailID) {
        this.emailID = emailID;
    }

    public WebFormsLogModel(String appVersion, String txtFecha
            , String txtCsrCode, String txtIDATM, String selCliente, String txtWO
            , String txtSerie, String txtContacto, String txtParte, String txtComentario, int chkProElectrico
            , int chkVolNoRegulado, int txtFN, int txtFT, int txtNT, int chkNoUps, int chkNoTierraFisica, int chkNoEnergia
            , int chkProSite, int chkSuciedad, int chkGoteras, int chkPlagas, int chkExpSol, int chkHumedad, int chkMalaIluminacion
            , int chkNoAA, int chkProComms, int selComunicaciones, int chkProOperativo, int chkSinInsumos, int chkSinBilletes
            , int chkMalaCalidadBilletes, int chkMalaCalidadInsumos, int chkErrorOperador, int chkCargaIncPapel
            , int chkCargaIncCaseteras, int chkSupervisor, int chkErrorBalanceo, int chkProVandalismo, int chkProOtros, int chkProFotos) {

        this.appVersion = appVersion;
        this.txtFecha = txtFecha;
        this.txtCsrCode = txtCsrCode;
        this.txtIDATM = txtIDATM;
        this.selCliente = selCliente;
        this.txtWO = txtWO;
        this.txtSerie = txtSerie;
        this.txtContacto = txtContacto;
        this.txtParte = txtParte;
        this.txtComentario = txtComentario;
        this.chkProElectrico = chkProElectrico;
        this.chkVolNoRegulado = chkVolNoRegulado;
        this.txtFN = txtFN;
        this.txtFT = txtFT;
        this.txtNT = txtNT;
        this.chkNoUps = chkNoUps;
        this.chkNoTierraFisica = chkNoTierraFisica;
        this.chkNoEnergia = chkNoEnergia;
        this.chkProSite = chkProSite;
        this.chkSuciedad = chkSuciedad;
        this.chkGoteras = chkGoteras;
        this.chkPlagas = chkPlagas;
        this.chkExpSol = chkExpSol;
        this.chkHumedad = chkHumedad;
        this.chkMalaIluminacion = chkMalaIluminacion;
        this.chkNoAA = chkNoAA;
        this.chkProComms = chkProComms;
        this.selComunicaciones = selComunicaciones;
        this.chkProOperativo = chkProOperativo;
        this.chkSinInsumos = chkSinInsumos;
        this.chkSinBilletes = chkSinBilletes;
        this.chkMalaCalidadBilletes = chkMalaCalidadBilletes;
        this.chkMalaCalidadInsumos = chkMalaCalidadInsumos;
        this.chkErrorOperador = chkErrorOperador;
        this.chkCargaIncPapel = chkCargaIncPapel;
        this.chkCargaIncCaseteras = chkCargaIncCaseteras;
        this.chkSupervisor = chkSupervisor;
        this.chkErrorBalanceo = chkErrorBalanceo;
        this.chkProVandalismo = chkProVandalismo;
        this.chkProOtros = chkProOtros;
        this.chkProFotos = chkProFotos;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getTxtFecha() {
        return txtFecha;
    }

    public void setTxtFecha(String txtFecha) {
        this.txtFecha = txtFecha;
    }

    public String getTxtCsrCode() {
        return txtCsrCode;
    }

    public void setTxtCsrCode(String txtCsrCode) {
        this.txtCsrCode = txtCsrCode;
    }

    public String getTxtIDATM() {
        return txtIDATM;
    }

    public void setTxtIDATM(String txtIDATM) {
        this.txtIDATM = txtIDATM;
    }

    public String getSelCliente() {
        return selCliente;
    }

    public void setSelCliente(String selCliente) {
        this.selCliente = selCliente;
    }

    public String getTxtWO() {
        return txtWO;
    }

    public void setTxtWO(String txtWO) {
        this.txtWO = txtWO;
    }

    public String getTxtSerie() {
        return txtSerie;
    }

    public void setTxtSerie(String txtSerie) {
        this.txtSerie = txtSerie;
    }

    public String getTxtContacto() {
        return txtContacto;
    }

    public void setTxtContacto(String txtContacto) {
        this.txtContacto = txtContacto;
    }

    public String getTxtParte() {
        return txtParte;
    }

    public void setTxtParte(String txtParte) {
        this.txtParte = txtParte;
    }

    public String getTxtComentario() {
        return txtComentario;
    }

    public void setTxtComentario(String txtComentario) {
        this.txtComentario = txtComentario;
    }

    public int getChkProElectrico() {
        return chkProElectrico;
    }

    public void setChkProElectrico(int chkProElectrico) {
        this.chkProElectrico = chkProElectrico;
    }

    public int getChkVolNoRegulado() {
        return chkVolNoRegulado;
    }

    public void setChkVolNoRegulado(int chkVolNoRegulado) {
        this.chkVolNoRegulado = chkVolNoRegulado;
    }

    public int getTxtFN() {
        return txtFN;
    }

    public void setTxtFN(int txtFN) {
        this.txtFN = txtFN;
    }

    public int getTxtFT() {
        return txtFT;
    }

    public void setTxtFT(int txtFT) {
        this.txtFT = txtFT;
    }

    public int getTxtNT() {
        return txtNT;
    }

    public void setTxtNT(int txtNT) {
        this.txtNT = txtNT;
    }

    public int getChkNoUps() {
        return chkNoUps;
    }

    public void setChkNoUps(int chkNoUps) {
        this.chkNoUps = chkNoUps;
    }

    public int getChkNoTierraFisica() {
        return chkNoTierraFisica;
    }

    public void setChkNoTierraFisica(int chkNoTierraFisica) {
        this.chkNoTierraFisica = chkNoTierraFisica;
    }

    public int getChkNoEnergia() {
        return chkNoEnergia;
    }

    public void setChkNoEnergia(int chkNoEnergia) {
        this.chkNoEnergia = chkNoEnergia;
    }

    public int getChkProSite() {
        return chkProSite;
    }

    public void setChkProSite(int chkProSite) {
        this.chkProSite = chkProSite;
    }

    public int getChkSuciedad() {
        return chkSuciedad;
    }

    public void setChkSuciedad(int chkSuciedad) {
        this.chkSuciedad = chkSuciedad;
    }

    public int getChkGoteras() {
        return chkGoteras;
    }

    public void setChkGoteras(int chkGoteras) {
        this.chkGoteras = chkGoteras;
    }

    public int getChkPlagas() {
        return chkPlagas;
    }

    public void setChkPlagas(int chkPlagas) {
        this.chkPlagas = chkPlagas;
    }

    public int getChkExpSol() {
        return chkExpSol;
    }

    public void setChkExpSol(int chkExpSol) {
        this.chkExpSol = chkExpSol;
    }

    public int getChkHumedad() {
        return chkHumedad;
    }

    public void setChkHumedad(int chkHumedad) {
        this.chkHumedad = chkHumedad;
    }

    public int getChkMalaIluminacion() {
        return chkMalaIluminacion;
    }

    public void setChkMalaIluminacion(int chkMalaIluminacion) {
        this.chkMalaIluminacion = chkMalaIluminacion;
    }

    public int getChkNoAA() {
        return chkNoAA;
    }

    public void setChkNoAA(int chkNoAA) {
        this.chkNoAA = chkNoAA;
    }

    public int getChkProComms() {
        return chkProComms;
    }

    public void setChkProComms(int chkProComms) {
        this.chkProComms = chkProComms;
    }

    public int getSelComunicaciones() {
        return selComunicaciones;
    }

    public void setSelComunicaciones(int selComunicaciones) {
        this.selComunicaciones = selComunicaciones;
    }

    public int getChkProOperativo() {
        return chkProOperativo;
    }

    public void setChkProOperativo(int chkProOperativo) {
        this.chkProOperativo = chkProOperativo;
    }

    public int getChkSinInsumos() {
        return chkSinInsumos;
    }

    public void setChkSinInsumos(int chkSinInsumos) {
        this.chkSinInsumos = chkSinInsumos;
    }

    public int getChkSinBilletes() {
        return chkSinBilletes;
    }

    public void setChkSinBilletes(int chkSinBilletes) {
        this.chkSinBilletes = chkSinBilletes;
    }

    public int getChkMalaCalidadBilletes() {
        return chkMalaCalidadBilletes;
    }

    public void setChkMalaCalidadBilletes(int chkMalaCalidadBilletes) {
        this.chkMalaCalidadBilletes = chkMalaCalidadBilletes;
    }

    public int getChkMalaCalidadInsumos() {
        return chkMalaCalidadInsumos;
    }

    public void setChkMalaCalidadInsumos(int chkMalaCalidadInsumos) {
        this.chkMalaCalidadInsumos = chkMalaCalidadInsumos;
    }

    public int getChkErrorOperador() {
        return chkErrorOperador;
    }

    public void setChkErrorOperador(int chkErrorOperador) {
        this.chkErrorOperador = chkErrorOperador;
    }

    public int getChkCargaIncPapel() {
        return chkCargaIncPapel;
    }

    public void setChkCargaIncPapel(int chkCargaIncPapel) {
        this.chkCargaIncPapel = chkCargaIncPapel;
    }

    public int getChkCargaIncCaseteras() {
        return chkCargaIncCaseteras;
    }

    public void setChkCargaIncCaseteras(int chkCargaIncCaseteras) {
        this.chkCargaIncCaseteras = chkCargaIncCaseteras;
    }

    public int getChkSupervisor() {
        return chkSupervisor;
    }

    public void setChkSupervisor(int chkSupervisor) {
        this.chkSupervisor = chkSupervisor;
    }

    public int getChkErrorBalanceo() {
        return chkErrorBalanceo;
    }

    public void setChkErrorBalanceo(int chkErrorBalanceo) {
        this.chkErrorBalanceo = chkErrorBalanceo;
    }

    public int getChkProVandalismo() {
        return chkProVandalismo;
    }

    public void setChkProVandalismo(int chkProVandalismo) {
        this.chkProVandalismo = chkProVandalismo;
    }

    public int getChkProOtros() {
        return chkProOtros;
    }

    public void setChkProOtros(int chkProOtros) {
        this.chkProOtros = chkProOtros;
    }

    public int getChkProFotos() {
        return chkProFotos;
    }

    public void setChkProFotos(int chkProFotos) {
        this.chkProFotos = chkProFotos;
    }

}
