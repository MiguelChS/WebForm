package models;

import mc185249.webforms.R;

/**
 * Created by jn185090 on 6/14/2016.
 */
public class ScrollingActivityModel {
    /**
     * almacena los textos de los menu
     */
    String[] textViewsText = {
            "Inventario Partes",
            "Environmental Site",
            "Logistics Survey",
            "Mantenimiento",
            "Memoria Fiscal",
            "Cambio Pid Pad",
            "Visita Tecnica",
            "Teclado Encryptor",
            "Devolucion Partes"

    };

    /**
     * Almacena los id de las imagenes para cada menu
     */
    int[] resources = {
            R.drawable.ic_launcher,
            R.drawable.ic_launcher,
            R.drawable.ic_launcher,
            R.drawable.ic_launcher,
            R.drawable.ic_launcher,
            R.drawable.ic_launcher,
            R.drawable.ic_launcher,
            R.drawable.ic_launcher,
            R.drawable.ic_launcher
    };

    public String getText(int position){
        return textViewsText[position];
    }

    public int getResource(int position){
        return resources[position];
    }

    public int count(){
        return textViewsText.length;
    }
}
