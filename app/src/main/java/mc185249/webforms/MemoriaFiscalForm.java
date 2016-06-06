package mc185249.webforms;

/**
 * Created by jn185090 on 4/12/2016.
 */
public class MemoriaFiscalForm implements IWebForm {
    String WorkOrder, cliente, custRef, siteName, puntoVenta, firmwarePlaca,
            serieImpresor;

    public String getWorkOrder() {
        return WorkOrder;
    }

    public void setWorkOrder(String workOrder) {
        WorkOrder = workOrder;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
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

    public String getPuntoVenta() {
        return puntoVenta;
    }

    public void setPuntoVenta(String puntoVenta) {
        this.puntoVenta = puntoVenta;
    }

    public String getFirmwarePlaca() {
        return firmwarePlaca;
    }

    public void setFirmwarePlaca(String firmwarePlaca) {
        this.firmwarePlaca = firmwarePlaca;
    }

    public String getSerieImpresor() {
        return serieImpresor;
    }

    public void setSerieImpresor(String serieImpresor) {
        this.serieImpresor = serieImpresor;
    }
}
