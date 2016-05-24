package com.example.mc185249.webforms;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.gc.materialdesign.views.ButtonRectangle;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class FilterDialogActivity extends AppCompatActivity {

    EditText clase, clase_modelo, parte, descripcion;
    ButtonRectangle filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_filter_dialog);

        clase = (EditText) findViewById(R.id.editText_clase);
        clase_modelo = (EditText) findViewById(R.id.editText_claseModelo);
        parte = (EditText) findViewById(R.id.editText_parte);
        descripcion = (EditText) findViewById(R.id.editText_descripcion);
        filter = (ButtonRectangle) findViewById(R.id.button_apply);
        filter.setOnClickListener(button_handler);

    }

    View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus){
                clase.setText("");
                clase_modelo.setText("");
                parte.setText("");
                descripcion.setText("");

            }
        }
    };

    View.OnClickListener button_handler = new View.OnClickListener(){

        @Override
        public void onClick(View v){


            Intent i = getIntent();
            i.putExtra("CLASE",clase.getText());
            i.putExtra("CLASEMODELO",clase_modelo.getText());
            i.putExtra("PARTE",parte.getText());
            i.putExtra("DESCRIPCION", descripcion.getText());

            setResult(RESULT_OK, i);
            finish();
        }
    };

 /*   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }*/
}
