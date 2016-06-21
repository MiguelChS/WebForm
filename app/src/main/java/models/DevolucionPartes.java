package models;

import java.text.SimpleDateFormat;
import java.util.Date;

import mc185249.webforms.IWebForm;

/**
 * Created by jn185090 on 6/20/2016.
 */
public class DevolucionPartes implements IWebForm {
    private String wareHouse, localidad,guiaDHL, guia, empresa, ordenRetiro, parte,descripcion
            ,cantidad,estado;
    private Boolean GNG, OCA;
    private Date fecha;

    public DevolucionPartes() {
    }

    public DevolucionPartes(String wareHouse, String localidad, String guiaDHL, String guia, String empresa, String ordenRetiro, String parte, String descripcion, String cantidad, String estado, Boolean GNG, Boolean OCA, Date fecha) {
        this.wareHouse = wareHouse;
        this.localidad = localidad;
        this.guiaDHL = guiaDHL;
        this.guia = guia;
        this.empresa = empresa;
        this.ordenRetiro = ordenRetiro;
        this.parte = parte;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.estado = estado;
        this.GNG = GNG;
        this.OCA = OCA;
        this.fecha = fecha;
    }

    public String getWareHouse() {
        return wareHouse;
    }

    public void setWareHouse(String wareHouse) {
        this.wareHouse = wareHouse;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getGuiaDHL() {
        return guiaDHL;
    }

    public void setGuiaDHL(String guiaDHL) {
        this.guiaDHL = guiaDHL;
    }

    public String getGuia() {
        return guia;
    }

    public void setGuia(String guia) {
        this.guia = guia;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getOrdenRetiro() {
        return ordenRetiro;
    }

    public void setOrdenRetiro(String ordenRetiro) {
        this.ordenRetiro = ordenRetiro;
    }

    public String getParte() {
        return parte;
    }

    public void setParte(String parte) {
        this.parte = parte;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Boolean getGNG() {
        return GNG;
    }

    public void setGNG(Boolean GNG) {
        this.GNG = GNG;
    }

    public Boolean getOCA() {
        return OCA;
    }

    public void setOCA(Boolean OCA) {
        this.OCA = OCA;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String fechaToString(){

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String date = format.format(fecha);
        return date;
    }
}
