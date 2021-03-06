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

    RecyclerView.LayoutManager mLayoutManager;
    ProgressBar progressBar;
    ClipboardManager clipboardManager;
    ClipData clipData;

    private class LoadData extends AsyncTask<Void, Void, ArrayList<Elemento>> {

        @Override
        protected void onPreExecute() {
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
            init();
            mAdapter = new ElementAdapter(InventoryActivity.this,0,aVoid);
            recycleView.setAdapter(mAdapter);
            setProgressBarIndeterminateVisibility(false);
            totalRegistros.setText("Total de registros: " + aVoid.size());
        }
    }

    private class FilterData extends AsyncTask<String, Void, Void> {


        @Override
        protected void onPreExecute() {
            recycleView.setVisibility(View.GONE);
            setProgressBarIndeterminateVisibility(true);

        }

        @Override
        protected Void doInBackground(String... params) {
            //mAdapter.setmElemento(params[0]);
            mAdapter.getFilter().filter(params[0]);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            recycleView.setVisibility(View.VISIBLE);
            totalRegistros.setText("Total de registros: " + mAdapter.getItemCount());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_inventory);
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

            mAdapter.setmElemento(e);
           new FilterData().execute("");
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

                if (query == null
                        || query.trim().length() == 0){
                    mAdapter.refreshAdapter();
                    totalRegistros.setText("Total de registros: " + mAdapter.getItemCount());
                }
                if (query.length() < 4) return true;


                new FilterData().execute(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
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
