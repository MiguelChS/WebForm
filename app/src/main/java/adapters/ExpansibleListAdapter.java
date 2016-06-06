package adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.media.audiofx.EnvironmentalReverb;
import android.support.v7.widget.ListViewCompat;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;


import org.w3c.dom.Text;

import mc185249.webforms.EnvironmentalSiteActivity;
import mc185249.webforms.R;

public class ExpansibleListAdapter extends BaseExpandableListAdapter {

    Context mContext;
    ExpansibleListViewDataAdapter[] expansibleListViewDataAdapters;
    LayoutInflater layoutInflater;
    String[] tempChild;
    public static ArrayList<String> selectedChild = new ArrayList<>();
    public static ArrayList<String> selectedGroup = new ArrayList<>();

    public ExpansibleListAdapter(Context mContext, ExpansibleListViewDataAdapter[] expansibleListViewDataAdapters) {
        this.mContext = mContext;
        this.expansibleListViewDataAdapters = expansibleListViewDataAdapters;
        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getGroupCount() {
        return expansibleListViewDataAdapters.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return expansibleListViewDataAdapters[groupPosition].getChildGroup().length;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if (convertView == null){
            convertView = layoutInflater.inflate(R.layout.expandable_listview_group,null);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.textView_group);
        textView.setText(expansibleListViewDataAdapters[groupPosition].getGroup());
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        tempChild = expansibleListViewDataAdapters[groupPosition].getChildGroup();
        TextView textView;
        final CheckBox checkBox;
        if (convertView == null){
            convertView = layoutInflater.inflate(R.layout.expandable_listview_child_group,null);
        }
        textView = (TextView) convertView.findViewById(R.id.textView_child);
        checkBox = (CheckBox)convertView.findViewById(R.id.chk);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked){
                    selectedChild.remove(selectedChild.indexOf(tempChild[childPosition]));
                    selectedGroup.remove(selectedGroup.indexOf(expansibleListViewDataAdapters[groupPosition].getGroup()));
                    String group = expansibleListViewDataAdapters[groupPosition]
                            .getGroup().toLowerCase();
                    toggle(group,tempChild[childPosition],0);
                }else{
                    selectedChild.add(tempChild[childPosition]);
                    selectedGroup.add(expansibleListViewDataAdapters[groupPosition].getGroup());
                    String group = expansibleListViewDataAdapters[groupPosition]
                            .getGroup().toLowerCase();
                    toggle(group,tempChild[childPosition],1);
                }
            }
        });

        textView.setText(tempChild[childPosition]);

        return convertView;
    }

    private void toggle(String group,String child,int value){
        if (group.contains("problema electrico")){
            EnvironmentalSiteActivity.logModel
                    .setChkProElectrico(value);
            switch (child){
                case "Voltaje no regulado":
                    EnvironmentalSiteActivity.logModel
                            .setChkVolNoRegulado(value);
                    break;
                case "No posee UPS":
                    EnvironmentalSiteActivity.logModel
                            .setChkNoUps(value);
                    break;
                case "No posee tierra fisica":
                    EnvironmentalSiteActivity.logModel
                            .setChkNoTierraFisica(value);
                    break;
                case "Sin energia electrica":
                    EnvironmentalSiteActivity.logModel
                            .setChkNoEnergia(value);
                    break;

            }
        }

        if (group.contains("problema de site")){
            EnvironmentalSiteActivity.logModel
                    .setChkProSite(value);
            switch (child){
                case "Suciedad":
                    EnvironmentalSiteActivity.logModel
                            .setChkSuciedad(value);
                    break;
                case "Goteras":
                    EnvironmentalSiteActivity.logModel
                            .setChkGoteras(value);
                    break;
                case "Plagas":
                    EnvironmentalSiteActivity.logModel
                            .setChkPlagas(value);
                    break;
                case "Exposicion directa al sol":
                    EnvironmentalSiteActivity.logModel
                            .setChkExpSol(value);
                    break;
                case "Humedad":
                    EnvironmentalSiteActivity.logModel
                            .setChkHumedad(value);
                    break;
                case "Mala iluminacion":
                    EnvironmentalSiteActivity.logModel
                            .setChkMalaIluminacion(value);
                    break;
                case "Sin AA/Calefaccion":
                    EnvironmentalSiteActivity.logModel
                            .setChkNoAA(value);
                    break;
            }

        }
        if (group.contains("problema de comunicaciones")){
            EnvironmentalSiteActivity.logModel
                    .setChkProComms(value);

        }
        if (group.contains("problema operativo")){
            EnvironmentalSiteActivity.logModel
                    .setChkProOperativo(value);
            switch (child){
                case "Sin insumos":
                    EnvironmentalSiteActivity.logModel
                            .setChkSinInsumos(value);
                    break;
                case "Sin billetes":
                    EnvironmentalSiteActivity.logModel
                            .setChkSinBilletes(value);
                    break;
                case "Mala calidad de billetes":
                    EnvironmentalSiteActivity.logModel
                            .setChkMalaCalidadBilletes(value);
                    break;
                case "Mala calidad de insumos":
                    EnvironmentalSiteActivity.logModel
                            .setChkMalaCalidadInsumos(value);
                    break;
                case "Error de operador":
                    EnvironmentalSiteActivity.logModel
                            .setChkErrorOperador(value);
                    break;
            }
        }
        if (group.contains("problema de vandalismo")){
            EnvironmentalSiteActivity.logModel
                    .setChkProVandalismo(value);

        }
        if (group.contains("otros problemas")){
            EnvironmentalSiteActivity.logModel
                    .setChkProOtros(value);

        }
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


}