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

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
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
        return elementos.size();
    }

    private List<Elemento> filtroAvanzado(){
        List<Elemento> tempList = new ArrayList<>();
        tempList = elementos;
        for (Iterator<Elemento> it = tempList.iterator();it.hasNext();){
            Elemento cElemento = it.next();

            if (!cElemento.getParte().toLowerCase().contains(mElemento.getParte().toLowerCase())){
                it.remove();
                continue;
            }
            if (!cElemento.getClase().toLowerCase().contains(mElemento.getClase().toLowerCase())){
                it.remove();
                continue;
            }
            if (!cElemento.getClaseModelo().toLowerCase().contains(mElemento.getClaseModelo().toLowerCase())){
                it.remove();
                continue;
            }
            if (!cElemento.getDescripcion().toLowerCase().contains(mElemento.getDescripcion().toLowerCase())){
                it.remove();
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
            if (mElemento == null){
                mElemento = new Elemento(
                        constraint.toString(),constraint.toString(),constraint.toString(),0,constraint.toString()
                );
            }

            List<Elemento> resultado = filtroAvanzado();
            results.values = resultado;
            results.count = resultado.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            elementos = (List<Elemento>) results.values;
            if (results.count > 0){
                notifyDataSetChanged();
            }
        }
    };
   /* public ElementAdapter(Context context,int resource,List<Elemento> objects,Elemento filter){
        super(context,resource,objects);
        layout = resource;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mElemento = filter;
    }


    public ElementAdapter(Context context, int resource, Elemento[] objects) {
        super(context, resource, objects);
        layout = resource;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public ElementAdapter(Context context, int resource, List<Elemento> objects) {
        super(context, resource, objects);
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layout = resource;
        elementos = objects;
    }

    public void setmElemento(Elemento elemento){
        mElemento = elemento;
    }

    public List<Elemento> getElementos(){
        return elementos;
    }

    @Override
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

    @Override
    public Filter getFilter() {

        return filter;
    }

    Filter filter = new Filter() {
        FilterResults results = new FilterResults();
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (mElemento == null){
                mElemento = new Elemento(
                        constraint.toString(),constraint.toString(),constraint.toString(),0,constraint.toString()
                );
            }

            List<Elemento> resultado = filtroAvanzado();
            results.values = resultado;
            results.count = resultado.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            elementos = (List<Elemento>) results.values;
            if (results.count > 0){
                notifyDataSetChanged();
            }else{
                notifyDataSetInvalidated();
            }
        }
    };

    private List<Elemento> filtroAvanzado(){
        List<Elemento> tempList = new ArrayList<>();
        tempList = elementos;
        for (Iterator<Elemento> it = tempList.iterator();it.hasNext();){
            Elemento cElemento = it.next();

            if (!cElemento.getParte().toLowerCase().contains(mElemento.getParte().toLowerCase())){
                it.remove();
                continue;
            }
            if (!cElemento.getClase().toLowerCase().contains(mElemento.getClase().toLowerCase())){
                it.remove();
                continue;
            }
            if (!cElemento.getClaseModelo().toLowerCase().contains(mElemento.getClaseModelo().toLowerCase())){
                it.remove();
                continue;
            }
            if (!cElemento.getDescripcion().toLowerCase().contains(mElemento.getDescripcion().toLowerCase())){
                it.remove();
                continue;
            }

        }

        return tempList;
    }*/



}


