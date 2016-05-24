package models;

/**
 * Created by mc185249 on 3/18/2016.
 */

//REPRESENTA UNA FILA DE LA TABLA DENTRO DEL INVENTARIO
public class Elemento {

    private int id;
    private String clase;
    private String claseModelo;
    private String parte;
    private String descripcion;


    public Elemento(String clase, String claseModelo, String descripcion, int id, String parte) {
        this.clase = clase;
        this.claseModelo = claseModelo;
        this.descripcion = descripcion;
        this.id = id;
        this.parte = parte;
    }

    public Elemento() {
    }

    public String getClase() {
        return clase;
    }



    public void setClase(String clase) {
        this.clase = clase;
    }

    public String getParte() {

        return parte;
    }

    public void setParte(String parte) {
        this.parte = parte;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {

        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getClaseModelo() {

        return claseModelo;
    }

    public void setClaseModelo(String claseModelo) {
        this.claseModelo = claseModelo;
    }


    @Override
    public String toString() {
        return " Clase='" + clase + '\'' +
                ", Clase Modelo='" + claseModelo + '\'' +
                ", Parte='" + parte + '\'' +
                ", Descripcion='" + descripcion + '\'' +
                '}';
    }
}
