package adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.TextView;


import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import mc185249.webforms.R;
import models.Elemento;


/**
 * Created by mc185249 on 3/18/2016.
 */

//
public class ElementAdapter extends RecyclerView.Adapter<ElementAdapter.ViewHolder> implements Filterable {

    protected LayoutInflater inflater;
    protected int layout;
    protected List<Elemento> elementos;
    private Elemento mElemento;
    private List<Elemento> old_elementos;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView descripcion, clase, parte;

        public ViewHolder(View itemView) {
            super(itemView);
            this.descripcion = (TextView) itemView.findViewById(R.id.textView_descripcion);
            this.clase = (TextView) itemView.findViewById(R.id.textView_clase);
            this.parte = (TextView) itemView.findViewById(R.id.textView_parte);
        }
    }

    public ElementAdapter(Context context, int resource, List<Elemento> objects) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layout = resource;
        elementos = objects;
    }
      public Elemento getItem(int position) {
        return elementos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return elementos.get(position).getId();
    }

    @Override
    public ElementAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType){
        View v = inflater.from(parent.getContext())
                .inflate(R.layout.inventory_list_item,parent,false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.descripcion.setText(elementos.get(position).getDescripcion());
        holder.clase.setText(elementos.get(position).getClase());
        holder.parte.setText(elementos.get(position).getParte());
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        View v = inflater.inflate(layout,parent,false);
        TextView tv_clase = (TextView)v.findViewById(R.id.textView_clase);
        TextView tv_parte = (TextView) v.findViewById(R.id.textView_parte);
        TextView tv_descripcion = (TextView) v.findViewById(R.id.textView_descripcion);

        tv_clase.setText(getItem(position).getClase());
        tv_parte.setText(getItem(position).getParte());
        tv_descripcion.setText(getItem(position).getDescripcion());
        return v;
    }


    public void setmElemento(Elemento elemento){
        mElemento = elemento;
    }

    @Override
    public int getItemCount() {
        return elementos != null ? elementos.size() : 0;
    }

    private List<Elemento> filtroAvanzado(){
        List<Elemento> tempList = new ArrayList<>();
        tempList = elementos;
        for (Iterator<Elemento> it = tempList.iterator();it.hasNext();){
            Elemento cElemento = it.next();

            if (!cElemento.getParte().toLowerCase().trim().contains(mElemento.getParte().toLowerCase().trim())){
                it.remove();
                continue;
            }
            if (!cElemento.getClase().toLowerCase().trim().contains(mElemento.getClase().toLowerCase().trim())){
                it.remove();
                continue;
            }
            if (!cElemento.getClaseModelo().toLowerCase().trim().contains(mElemento.getClaseModelo().toLowerCase().trim())){
                it.remove();
                continue;
            }
            if (!cElemento.getDescripcion().toLowerCase().trim().contains(mElemento.getDescripcion().toLowerCase().trim())){
                it.remove();
                continue;
            }

        }

        return tempList;
    }

    private List<Elemento> filtroBasico(String constraint){
        List<Elemento> tempList = new ArrayList<>();

        for (Iterator<Elemento> it = elementos.iterator();it.hasNext();){
            Elemento cElemento = it.next();

            if (cElemento.getDescripcion().toLowerCase().trim()
                    .contains(constraint.toLowerCase().trim())){
                tempList.add(cElemento);
                continue;
            }

        }

        return tempList;
    }

    @Override
    public Filter getFilter() {

        return filter;
    }

    Filter filter = new Filter() {
        FilterResults results = new FilterResults();
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Elemento> resultado;
            if (old_elementos != null
                    && old_elementos.size() > 0){
                elementos.clear();
                elementos = old_elementos;
                old_elementos.clear();
            }
            if (mElemento == null){
                resultado = filtroBasico(constraint.toString());
                results.values = resultado;
                results.count = resultado.size();
                return results;
            }

            resultado = filtroAvanzado();
            results.values = resultado;
            results.count = resultado.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            elementos.clear();
            old_elementos = elementos;
            elementos = (List<Elemento>) results.values;
            notifyDataSetChanged();
        }
    };

    public void refreshAdapter(){
        if (old_elementos != null
                && old_elementos.size() > 0){
            elementos.clear();
            elementos = old_elementos;
            notifyDataSetChanged();
        }
    }
}


