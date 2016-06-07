package mc185249.webforms;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;


import org.w3c.dom.Text;

import Tabs.SlidingTabLayout;
import app.AppController;

public class ScrollingActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton btnGoToparts, btnSite, btnLogistics, btnMant, btnMen, btnPid, btnTech, btnReca;

    //region sync
    private static final int REQUEST_LOGIN_CODE = 9000;
    public static final long SYNC_INTERVAL = Long.parseLong(String.valueOf(R.string.pollFrequency));
    public static final String AUTHORITY = "ClientsContentProvider";
    ContentResolver mResolver;
    private String user = "";
    SlidingTabLayout mTabs;
    ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //region verifica existencia de credenciales NCR
        Intent i = new Intent(this, EmailService.class);
        if (!AppController.getInstance().checkCredentials()) {
            showLogin();
        } else {
            startService(i);
        }
        user = new WebFormsPreferencesManager(this).getUserName();
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


        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(new WebFormsPagerAdapter(getSupportFragmentManager()));
        mTabs = (SlidingTabLayout)findViewById(R.id.tabs);
        mTabs.setCustomTabView(R.layout.custom_tab_view,R.id.tabText);
        mTabs.setDistributeEvenly(true);
        mTabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.blanco);
            }
        });
        mTabs.setViewPager(mPager);

    }


    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.btnGoToparts:
                Intent i = new Intent(this, InventoryActivity.class);
                startActivity(i);
                break;

            case R.id.btnSite:
                Intent intent = new Intent(this, EnvironmentalSiteActivity.class);
                startActivity(intent);
                break;

            case R.id.btnLogistics:
                Intent siteIntent = new Intent(this, LogisticsSurveyActivity.class);
                startActivity(siteIntent);
                break;
            case R.id.btnMant:
                Intent mantenimientoIntent = new Intent(this, MantenimientoSurveyActivity.class);
                startActivity(mantenimientoIntent);
                break;
            case R.id.btnMen:
                Intent in = new Intent(this, memoriaFiscalActivity.class);
                startActivity(in);
                break;
            case R.id.btnPid:
                Intent intent1 = new Intent(this,cambioPidPad.class);
                startActivity(intent1);
                break;
            case R.id.btnTech:
                Intent intent2 = new Intent(this,VisitaTecnica.class);
                startActivity(intent2);
                break;
            case R.id.btnReca:
                Intent intent3 = new Intent(this,TecladoEncryptorActivity.class);
                startActivity(intent3);
                break;
        }
    }

    private void showLogin() {
        Intent serviceIntent = new Intent(this, EmailService.class);
        stopService(serviceIntent);
        Intent in = new Intent(this, Stepper.class);
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
            Intent serviceIntent = new Intent(this, EmailService.class);
            startService(serviceIntent);


            return;

        }

    }

    class WebFormsPagerAdapter extends FragmentPagerAdapter{

        int icons[] = {
                R.drawable.ic_assignment
                ,R.drawable.ic_history
        };
        String[] tabs;
        public WebFormsPagerAdapter(FragmentManager fm) {
            super(fm);
            tabs = getResources().getStringArray(R.array.tabs);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Drawable drawable = getResources().getDrawable(icons[position]);
            drawable.setBounds(0,0,36,36);
            ImageSpan imageSpan = new ImageSpan(drawable);
            SpannableString spannableString = new SpannableString(" ");
            spannableString.setSpan(imageSpan,0,spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return spannableString;
        }

        @Override
        public Fragment getItem(int position) {
            WebFormsTabsFragment webFormsTabsFragment = WebFormsTabsFragment.getInstance(position);
            return webFormsTabsFragment;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    public static class WebFormsTabsFragment extends Fragment{
        public static WebFormsTabsFragment getInstance(int position){
           WebFormsTabsFragment webFormsTabsFragment = new WebFormsTabsFragment();
            Bundle args = new Bundle();
            args.putInt("position",position);
            webFormsTabsFragment.setArguments(args);
            return webFormsTabsFragment;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            Bundle bundle = getArguments();
            View layout = null;
            if (bundle != null){
                switch (bundle.getInt("position")){

                    case 1:
                        layout = inflater.inflate(R.layout.main_activity_fragment01,container,false);
                        TextView textView = (TextView) layout.findViewById(R.id.textview);
                        textView.setText("Hola mundo!!");
                        break;
                    default:
                        layout = inflater.inflate(R.layout.content_scrolling,container,false);
                        break;
                }
            }

            return layout;
        }
    }
}
