package models;

import android.database.Cursor;
import android.databinding.tool.util.L;

import mc185249.webforms.ClientsContentProvider;


/**
 * Created by jn185090 on 5/27/2016.
 */
public class Cliente {
    private String pais, nombre, numero;
    private int id;

    public static Cliente fromCursor(Cursor cursor){
        Cliente cliente = new Cliente();
        cliente.setId(cursor.getInt(cursor.getColumnIndex(ClientsContentProvider.ID)));
        cliente.setPais(cursor.getString(cursor.getColumnIndex(ClientsContentProvider.PAIS)));
        cliente.setNombre(cursor.getString(cursor.getColumnIndex(ClientsContentProvider.NOMBRE)));
        cliente.setNumero(cursor.getString(cursor.getColumnIndex(ClientsContentProvider.NUMERO)));

        return cliente;
    }
    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cliente(String pais, String nombre, String numero) {
        this.pais = pais;
        this.nombre = nombre;
        this.numero = numero;
    }

    public Cliente() {
    }
}
