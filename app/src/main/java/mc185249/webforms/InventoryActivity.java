package mc185249.webforms;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.databinding.tool.util.StringUtils;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.app.SearchManager;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.gc.materialdesign.views.ButtonFlat;

import java.util.ArrayList;
import java.util.HashMap;

import adapters.ElementAdapter;
import connectivity.CsvTask;
import models.Elemento;

public class InventoryActivity extends AppCompatActivity {

    ArrayList<Elemento> gridArray = new ArrayList<Elemento>();
    ArrayList<Elemento> filterArray = new ArrayList<>();
    ButtonFlat filter_button;
    SwipeMenuListView swipeMenuListView;
    ElementAdapter mAdapter;

    ClipboardManager clipboardManager;
    ClipData clipData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_inventory);

        gridArray.add(new Elemento("F792", "2012-F792", "INTEL DUAL CORE 2", 1, "060084423"));
        gridArray.add(new Elemento("F792", "2012-F792", "PEPE", 1, "060084423"));

        filter_button = (ButtonFlat) findViewById(R.id.button_filter);
        filter_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(InventoryActivity.this, FilterDialogActivity.class);
                startActivityForResult(i, 1);

            }


        });

        //region swipeMenuList

        clipboardManager = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        this.swipeMenuListView = (SwipeMenuListView) findViewById(R.id.listView);
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem copyItem = new SwipeMenuItem(
                        getApplicationContext()
                );

                copyItem.setBackground(new ColorDrawable(Color.rgb(0xc9,0xc9,0xcE)));
                copyItem.setWidth(120);
                copyItem.setIcon(R.drawable.ic_content_copy);
                menu.addMenuItem(copyItem);
            }
        };

        swipeMenuListView.setMenuCreator(creator);
        swipeMenuListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index){
                    case 0:
                        Elemento elemento = (Elemento) mAdapter.getItem(position);
                        String data =
                            elemento.getParte() + "," + elemento.getDescripcion() + "," +
                                elemento.getClase() + "," +  elemento.getClaseModelo() + ","+
                                String.valueOf(elemento.getId())
                        ;

                        clipData = ClipData.newPlainText("elemento",data);
                        clipboardManager.setPrimaryClip(clipData);
                        Toast.makeText(InventoryActivity.this,"Copiado al portapapeles!",Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });

        swipeMenuListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        mAdapter = new ElementAdapter(InventoryActivity.this,R.layout.inventory_list_item,gridArray);
        swipeMenuListView.setAdapter(mAdapter);

        //endregion

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    protected void con(){

        String stringUrl = getResources().getString(R.string.urlCsv);
        ConnectivityManager connMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMan.getActiveNetworkInfo();
        boolean hasCon = networkInfo.isConnected();
         new CsvTask(hasCon).execute(stringUrl);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(RESULT_OK == resultCode){
            Bundle extras = data.getExtras();
            String parte = new String(extras.get("PARTE").toString());
            String clase = new String(extras.get("CLASE").toString());
            String claseModelo = new String(extras.get("CLASEMODELO").toString());
            String descripcion = new String(extras.get("DESCRIPCION").toString());

            //TEMPORALMENTE FILTRA UNICAMENTE POR DESCRIPCION
            Elemento e = new Elemento();
            e.setClase(clase);
            e.setClaseModelo(claseModelo);
            e.setDescripcion(descripcion);
            e.setParte(parte);

            mAdapter = new ElementAdapter(InventoryActivity.this,R.layout.inventory_list_item,filter(e));
            swipeMenuListView.setAdapter(mAdapter);
            swipeMenuListView.deferNotifyDataSetChanged();
        }

    }

    protected ArrayList<Elemento> filter(String cons){
        ArrayList<Elemento> arrayList = new ArrayList<>();
        ArrayList<Elemento> toSearch = new ArrayList<>();
        if (filterArray.size() > 0){
            toSearch = filterArray;
        }else{
            toSearch = gridArray;
        }
        for (Elemento elemento : toSearch){
            if (elemento.getClase().toLowerCase().contains(cons)
                    || elemento.getParte().toLowerCase().contains(cons)
                    || elemento.getClaseModelo().toLowerCase().contains(cons)
                    || elemento.getDescripcion().toLowerCase().contains(cons))
            {
                arrayList.add(elemento);

            }
        }


        return arrayList;
    }

    protected ArrayList<Elemento> filter(Elemento e){
        ArrayList<Elemento> arrayList = new ArrayList<>();
        ArrayList<Elemento> toSearch = new ArrayList<>();

        if (filterArray.size() > 0){
            toSearch = filterArray;
        }else{
            toSearch = gridArray;
        }
        for (Elemento elemento : toSearch){
            if (elemento.getClase().toLowerCase().equals(e.getClase())){
                arrayList.add(elemento);
                continue;
            }
            if (elemento.getParte().toLowerCase().equals(e.getParte())){
                arrayList.add(elemento);
                continue;
            }

            if (elemento.getClaseModelo().toLowerCase().equals(e.getClaseModelo())){
                arrayList.add(elemento);
                continue;
            }
            if (elemento.getDescripcion().toLowerCase().equals(e.getDescripcion())){
                arrayList.add(elemento);
                continue;
            }
        }

        if (arrayList.size() == 0){
            Toast.makeText(this,"La busqueda no tuvo coincidencias",Toast.LENGTH_SHORT).show();
            return gridArray;
        }
        return arrayList;

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        android.support.v7.widget.SearchView searchView =
                (android.support.v7.widget.SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() < 1){
                    mAdapter = new ElementAdapter(InventoryActivity.this,R.layout.inventory_list_item,gridArray);
                    swipeMenuListView.setAdapter(mAdapter);
                    swipeMenuListView.deferNotifyDataSetChanged();
                }else{
                    filterArray = filter(newText);
                    mAdapter = new ElementAdapter(InventoryActivity.this,R.layout.inventory_list_item,filterArray);
                    swipeMenuListView.setAdapter(mAdapter);
                    swipeMenuListView.deferNotifyDataSetChanged();
                }

                return false;
            }
        });

        MenuItem settingItem = menu.findItem(R.id.setting);
        settingItem.setVisible(false);
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
