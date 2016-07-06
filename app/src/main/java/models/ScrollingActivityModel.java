package models;

import android.content.Context;
import android.content.Intent;

import mc185249.webforms.R;
import mc185249.webforms.WebFormsPreferencesManager;

/**
 * Created by jn185090 on 6/14/2016.
 * A partir del CSRCode determina los formularios disponibles por pais
 *
 */
public class ScrollingActivityModel {

    Context mContext;
    public ScrollingActivityModel(Context context) {
        mContext = context;
        String country = new WebFormsPreferencesManager(mContext).getCsrCode().substring(0,2);
        switch (country.toUpperCase().trim()){
            case "AR":
                if (argentina.equals("*")){
                    copyAll();
                    break;
                }
                copy(argentina);
                break;
            case "BR":
                if (brasil.equals("+")){
                    copyAll();
                }
                copy(brasil);
                break;
            case "CL":
                if (chile.equals("*")){
                    copyAll();
                }
                copy(chile);
                break;
            case "CO":
                if (colombia.equals("*")){
                    copyAll();
                }
                copy(colombia);
                break;
            case "MX":
                if (mexico.equals("*")){
                    copyAll();
                }
                copy(mexico);
                break;
            case "PE":
                if (peru.equals("*")){
                    copyAll();
                }
                copy(peru);
                break;
        }
    }


    private void copyAll(){
        resourcesPerCountry = resources;
        formsPerCountry = textViewsText;
    }

    private void copy(String country){
        int x = 0;
        int length = country.split(",").length;
        resourcesPerCountry = new int[length];
        formsPerCountry = new String[length];
        for (String f:
                country.split(",")) {

            resourcesPerCountry[x] = resources[Integer.parseInt(f)];
            formsPerCountry[x] = textViewsText[Integer.parseInt(f)];
            x++;
        }
    }
    String argentina = "*"; //* = TODOS
    String brasil = "2,1,6";
    String chile = "1,2,6";
    String colombia = "1,2,6";
    String mexico = "1,2,6";
    String peru = "1,2,0";
    /**
     * almacena los textos de los menu
     */
    String[] textViewsText = {
            "Inventario Partes",//0
            "Environmental Site",//1
            "Logistics Survey",//2
            "Mantenimiento",//3
            "Memoria Fiscal",//4
            "Cambio Pid Pad",//5
            "Visita Tecnica",//6
            "Teclado Encryptor",//7
            "Devolucion Partes"//8

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
    int [] resourcesPerCountry;
    String[] formsPerCountry;


    public String getText(int position){
        return formsPerCountry[position];
    }

    public int getResource(int position){

        return resourcesPerCountry[position];
    }

    public int count(){
        return formsPerCountry != null ?
                formsPerCountry.length : 0;
    }
}
