package adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
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

import com.example.mc185249.webforms.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import models.Elemento;

import static com.example.mc185249.webforms.R.color.bgToolbar;

/**
 * Created by mc185249 on 3/18/2016.
 */

//
public class ElementAdapter extends ArrayAdapter<Elemento> {

    protected LayoutInflater inflater;
    protected int layout;

    public ElementAdapter(Context context, int resource, Elemento[] objects) {
        super(context, resource, objects);
        layout = resource;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public ElementAdapter(Context context, int resource, List<Elemento> objects) {
        super(context, resource, objects);
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layout = resource;

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


}


