package mc185249.webforms;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.gc.materialdesign.views.ButtonFlat;

import java.util.ArrayList;

import adapters.ElementAdapter;
import models.Elemento;

public class InventoryActivity extends AppCompatActivity {


    ArrayList<Elemento> gridArray = new ArrayList<Elemento>();
    ArrayList<Elemento> old_gridArray = new ArrayList<>();
    ButtonFlat filter_button;
    RecyclerView recycleView;
    ElementAdapter mAdapter;
    TextView totalRegistros;
    android.app.ProgressDialog dialog;

    LinearLayout linlaHeaderProgress;
    RecyclerView.LayoutManager mLayoutManager;
    ProgressBar progressBar;
    ClipboardManager clipboardManager;
    ClipData clipData;
    private static Boolean MODO_ESTRICTO = false;

    private class LoadData extends AsyncTask<Void, Void, ArrayList<Elemento>> {

        @Override
        protected void onPreExecute() {
            linlaHeaderProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Elemento> doInBackground(Void... params) {
            Log.v("NCR","Cargando...");
            Cursor cursor = getContentResolver().query(
                    InventarioProvider.CONTENT_URL,
                    null,
                    null,
                    null,
                    null
            );
            if (cursor != null){
                while(cursor.moveToNext()){
                    gridArray.add(
                            new Elemento(
                                    cursor.getString(cursor.getColumnIndex(InventarioProvider.CLASE)),
                                    cursor.getString(cursor.getColumnIndex(InventarioProvider.CLASEMODELO)),
                                    cursor.getString(cursor.getColumnIndex(InventarioProvider.DESCRIPCION)),
                                    cursor.getInt(cursor.getColumnIndex(InventarioProvider.ID)),
                                    cursor.getString(cursor.getColumnIndex(InventarioProvider.PARTE))
                            )
                    );
                }
            }
            return gridArray;
        }

        @Override
        protected void onPostExecute(ArrayList<Elemento> aVoid) {
            linlaHeaderProgress.setVisibility(View.GONE);
            init();
            mAdapter = new ElementAdapter(InventoryActivity.this,0,aVoid);
            recycleView.setAdapter(mAdapter);
            setProgressBarIndeterminateVisibility(false);
            totalRegistros.setText("Total de registros: " + aVoid.size());
        }
    }

    private class FilterData extends AsyncTask<Elemento, Void, Void> {


        @Override
        protected void onPreExecute() {
            recycleView.setVisibility(View.GONE);
            linlaHeaderProgress.setVisibility(View.VISIBLE);
            setProgressBarIndeterminateVisibility(true);

        }

        @Override
        protected Void doInBackground(Elemento... params) {
            //mAdapter.setmElemento(params[0]);
            mAdapter.getFilter().filter(null);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mAdapter.notifyDataSetChanged();
            setProgressBarIndeterminateVisibility(false);
            recycleView.setVisibility(View.VISIBLE);
            totalRegistros.setText("Total de registros: " + mAdapter.getItemCount());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_inventory);
        linlaHeaderProgress = (LinearLayout)findViewById(R.id.linlaHeaderProgress);
        progressBar = (ProgressBar) findViewById(R.id.pbHeaderProgress);
        setProgressBarIndeterminateVisibility(true);
        filter_button = (ButtonFlat) findViewById(R.id.button_filter);
        filter_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(InventoryActivity.this, FilterDialogActivity.class);
                startActivityForResult(i, 1);

            }


        });


        totalRegistros = (TextView)findViewById(R.id.totalRegistros);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        new LoadData().execute();
    }

    private void init(){
        //region swipeMenuList

        clipboardManager = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        recycleView = (RecyclerView) findViewById(R.id.recycleView);
        recycleView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        recycleView.setLayoutManager(mLayoutManager);
        //endregion
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

            Elemento e = new Elemento();
            e.setClase(clase);
            e.setClaseModelo(claseModelo);
            e.setDescripcion(descripcion);
            e.setParte(parte);

           new FilterData().execute(e);
        }

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
                    if (newText.length() < 4) return false;



                mAdapter.setmElemento(new Elemento(
                        newText,
                        newText,
                        newText,
                        0,
                        newText)
                );

                recycleView.setVisibility(View.GONE);
                linlaHeaderProgress.setVisibility(View.VISIBLE);
                setProgressBarIndeterminateVisibility(true);
                mAdapter.getFilter().filter(null);
                mAdapter.notifyDataSetChanged();
                setProgressBarIndeterminateVisibility(false);
                recycleView.setVisibility(View.VISIBLE);
                totalRegistros.setText("Total de registros: " + mAdapter.getItemCount());

                return true;
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
