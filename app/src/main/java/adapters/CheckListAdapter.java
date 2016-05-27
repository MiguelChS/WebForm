package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mc185249.webforms.R;
import com.innodroid.expandablerecycler.ExpandableRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jn185090 on 5/26/2016.
 */
public class CheckListAdapter extends ExpandableRecyclerAdapter<CheckListAdapter.CheckListItem> {

    public static final int TYPE_PERSON = 1001;

    public CheckListAdapter(Context context ) {
        super(context);
        setItems(getCheckListItem());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case TYPE_HEADER:
                return new HeaderViewHolder(inflate(R.layout.expandable_listview_group,parent),null);
            case TYPE_PERSON:
                return new ChildViewHolder(inflate(R.layout.expandable_listview_child_group,parent));
            default:
                return new ChildViewHolder(inflate(R.layout.expandable_listview_child_group,parent));
        }
    }

    @Override
    public void onBindViewHolder(ExpandableRecyclerAdapter.ViewHolder holder, int position) {
        switch (getItemViewType(position)){
            case TYPE_HEADER:
                ((HeaderViewHolder)holder).bind(position);
                break;
            case TYPE_PERSON:
            default:
                ((ChildViewHolder)holder).bind(position);
                break;
        }
    }



    public static class CheckListItem extends ExpandableRecyclerAdapter.ListItem{
        public String Text;

        public CheckListItem(String group) {
            super(TYPE_HEADER);
            Text = group;
        }
        public CheckListItem(String item, int type){
            super(TYPE_PERSON);
            Text = item;
        }
    }

    public class HeaderViewHolder extends ExpandableRecyclerAdapter.HeaderViewHolder{

        TextView header;
        public HeaderViewHolder(View view, ImageView arrow) {
            super(view, arrow);
            header = (TextView) view.findViewById(R.id.textView_group);

        }

        public void bind(int position){
            header.setText(visibleItems.get(position).Text);
        }
    }

    public class ChildViewHolder extends
            ExpandableRecyclerAdapter.ViewHolder{

        TextView item;
    //    CheckBox checkBox_item;

        public ChildViewHolder(View view) {
            super(view);
      //      checkBox_item = (CheckBox) view.findViewById(R.id.chk);
            item = (TextView) view.findViewById(R.id.textView_child);
        }

        public void bind(int position){
            item.setText("Hola mundo");
        }
    }

    private List<CheckListAdapter.CheckListItem> getCheckListItem(){
        List<CheckListAdapter.CheckListItem> checkListItems = new ArrayList<>();
        checkListItems.add(new CheckListAdapter.CheckListItem("Problema electrico"));
        checkListItems.add(new CheckListAdapter.CheckListItem( "Voltaje no regulado",1));
        checkListItems.add(new CheckListAdapter.CheckListItem("No posee UPS",1));
        checkListItems.add(new CheckListAdapter.CheckListItem("No posee tierra fisica",1));
        checkListItems.add(new CheckListAdapter.CheckListItem("Sin energia electrica",1));

        checkListItems.add(new CheckListAdapter.CheckListItem("Problema de Site"));
        checkListItems.add(new CheckListAdapter.CheckListItem("Suciedad",1));
        checkListItems.add(new CheckListAdapter.CheckListItem("Goteras",1));
        checkListItems.add(new CheckListAdapter.CheckListItem("Plagas",1));
        checkListItems.add(new CheckListAdapter.CheckListItem("Exposicion directa al sol",1));
        checkListItems.add(new CheckListAdapter.CheckListItem("Humedad",1));
        checkListItems.add(new CheckListAdapter.CheckListItem("Mala iluminacion",1));
        checkListItems.add(new CheckListAdapter.CheckListItem("Sin AA/Calefaccion",1));

        checkListItems.add(new CheckListAdapter.CheckListItem("Problema de comunicaciones"));
        checkListItems.add(new CheckListAdapter.CheckListItem("Causa",1));

        checkListItems.add(new CheckListAdapter.CheckListItem("Problema operativo",1));
        checkListItems.add(new CheckListAdapter.CheckListItem("Sin insumos",1));
        checkListItems.add(new CheckListAdapter.CheckListItem("Sin billetes",1));
        checkListItems.add(new CheckListAdapter.CheckListItem("Mala calidad de billetes",1));
        checkListItems.add(new CheckListAdapter.CheckListItem("Mala calidad de insumos",1));
        checkListItems.add(new CheckListAdapter.CheckListItem("Error de operador",1));

        checkListItems.add(new CheckListAdapter.CheckListItem("Problema de vandalismo"));
        checkListItems.add(new CheckListAdapter.CheckListItem("Problema de vandalismo",1));

        checkListItems.add(new CheckListAdapter.CheckListItem("Otros problemas"));
        checkListItems.add(new CheckListAdapter.CheckListItem("Otros problemas",1));

        return checkListItems;
    }
}
