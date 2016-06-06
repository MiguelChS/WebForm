package models;

import android.database.Cursor;

import mc185249.webforms.ContactsProvider;

/**
 * Created by jn185090 on 5/29/2016.
 */
public class Contacto  {
    private String direcciones,nombre,pais,numero;
    private int id;

    public static Contacto fromCursor(Cursor cursor){
        Contacto contacto = new Contacto();
        contacto.setId(cursor.getInt(cursor.getColumnIndex(
                ContactsProvider.ID
        )));

        contacto.setDirecciones(cursor.getString(cursor.getColumnIndex(
                ContactsProvider.DIRECIONES
        )));
        contacto.setNombre(cursor.getString(cursor.getColumnIndex(
                ContactsProvider.NOMBRES
        )));
        contacto.setPais(cursor.getString(cursor.getColumnIndex(
                ContactsProvider.PAIS
        )));

        return contacto;
    }

    public Contacto(String direcciones, String nombre, String pais, String numero) {
        this.direcciones = direcciones;
        this.nombre = nombre;
        this.pais = pais;
        this.numero = numero;
    }

    public Contacto() {
    }

    public String getDirecciones() {
        return direcciones;
    }

    public void setDirecciones(String direcciones) {
        this.direcciones = direcciones;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
}
