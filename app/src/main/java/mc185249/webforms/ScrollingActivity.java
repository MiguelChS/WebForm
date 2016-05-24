package mc185249.webforms;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.example.mc185249.webforms.R;

public class ScrollingActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton btnGoToparts, btnSite, btnLogistics, btnMant, btnMen, btnPid, btnTech, btnReca;

    //region sync
    private static final int REQUEST_LOGIN_CODE = 9000;
    public static final long SYNC_INTERVAL = Long.parseLong(String.valueOf(R.string.pollFrequency));
    public static final String AUTHORITY = "com.example.mc185249.webforms.ClientsContentProvider";
    ContentResolver mResolver;
    private String user = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //region verifica existencia de credenciales NCR
        Intent i = new Intent(this, com.example.mc185249.webforms.EmailService.class);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String account = sharedPreferences.getString(String.valueOf(R.string.accountName), null);
        String pass = sharedPreferences.getString(String.valueOf(R.string.passwd), null);
        String csrCode = sharedPreferences.getString(String.valueOf(R.string.CSRCode), null);
        if ((account == null || account.isEmpty()) || (pass == null || pass.isEmpty()) || (csrCode == null || csrCode.isEmpty())) {
            showLogin();
        } else {
            startService(i);
        }
        user = account;
        //endregion

        //region GET BUTTONS ID AND ADD CLICK LISTENER
        this.btnGoToparts = (ImageButton) findViewById(R.id.btnGoToparts);
        this.btnSite = (ImageButton) findViewById(R.id.btnSite);
        this.btnLogistics = (ImageButton) findViewById(R.id.btnLogistics);
        this.btnMant = (ImageButton) findViewById(R.id.btnMant);
        this.btnMen = (ImageButton) findViewById(R.id.btnMen);
        this.btnPid = (ImageButton) findViewById(R.id.btnPid);
        this.btnTech = (ImageButton) findViewById(R.id.btnTech);
        this.btnReca = (ImageButton) findViewById(R.id.btnReca);

        ImageButton[] buttons = {
                btnGoToparts, btnSite, btnLogistics
                , btnMant, btnMen, btnPid
                , btnTech, btnReca
        };

        for (ImageButton btn : buttons) {
            if (btn != null){
                btn.setOnClickListener(this);
            }

        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLogin();
            }
        });
    }


    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.btnGoToparts:
                Intent i = new Intent(this, com.example.mc185249.webforms.InventoryActivity.class);
                startActivity(i);
                break;

            case R.id.btnSite:
                Intent intent = new Intent(this, com.example.mc185249.webforms.EnvironmentalSiteActivity.class);
                startActivity(intent);
                break;

            case R.id.btnLogistics:
                Intent siteIntent = new Intent(this, com.example.mc185249.webforms.LogisticsSurveyActivity.class);
                startActivity(siteIntent);
                break;
            case R.id.btnMant:
                Intent mantenimientoIntent = new Intent(this, com.example.mc185249.webforms.MantenimientoSurveyActivity.class);
                startActivity(mantenimientoIntent);
                break;
            case R.id.btnMen:
                Intent in = new Intent(this, com.example.mc185249.webforms.memoriaFiscalActivity.class);
                startActivity(in);
                break;
            case R.id.btnPid:
                //region sync adapter
                Account account1 = new Account(user, "com.webforms");
                AccountManager accountManager = (AccountManager) this.getSystemService(ACCOUNT_SERVICE);
                accountManager.addAccountExplicitly(account1, null, null);
                Bundle settingBundle = new Bundle();
                settingBundle.putBoolean(
                        ContentResolver.SYNC_EXTRAS_MANUAL, true
                );
                settingBundle.putBoolean(
                        ContentResolver.SYNC_EXTRAS_EXPEDITED, true
                );
                ContentResolver.requestSync(account1, AUTHORITY, settingBundle);

                //endregion
                break;
            case R.id.btnTech:

                break;
            case R.id.btnReca:
                break;
        }
    }

    private void showLogin() {
        Intent serviceIntent = new Intent(this, com.example.mc185249.webforms.EmailService.class);
        stopService(serviceIntent);
        Intent in = new Intent(this, com.example.mc185249.webforms.Stepper.class);
        startActivityForResult(in, REQUEST_LOGIN_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == REQUEST_LOGIN_CODE && resultCode == RESULT_OK) {
            user = data.getStringExtra(String.valueOf(R.string.accountName));
            String passwd = data.getStringExtra(String.valueOf(R.string.passwd));
            String csrCode = data.getStringExtra(String.valueOf(R.string.CSRCode));

            new WebFormsPreferencesManager(ScrollingActivity.this)
                    .save(user,
                            passwd,
                            csrCode);
            Intent serviceIntent = new Intent(this, com.example.mc185249.webforms.EmailService.class);
            startService(serviceIntent);


            return;

        }

    }
}
