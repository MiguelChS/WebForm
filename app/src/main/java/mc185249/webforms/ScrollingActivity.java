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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.util.ArrayList;

import Tabs.SlidingTabLayout;
import app.AppController;
import models.Email;
import models.EmailSender;

public class ScrollingActivity extends AppCompatActivity {
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

    public static class WebFormsTabsFragment extends Fragment implements View.OnClickListener{

        private ImageButton btnGoToparts, btnSite, btnLogistics, btnMant, btnMen, btnPid, btnTech, btnReca;
        RecyclerView recyclerView;
        CustomAdapter customAdapter;

        public static WebFormsTabsFragment getInstance(int position){
           WebFormsTabsFragment webFormsTabsFragment = new WebFormsTabsFragment();
            Bundle args = new Bundle();
            args.putInt("position",position);
            webFormsTabsFragment.setArguments(args);
            return webFormsTabsFragment;
        }


        @Override
        public void onClick(View v) {


            switch (v.getId()) {
                case R.id.btnGoToparts:
                    Intent i = new Intent(getContext(), InventoryActivity.class);
                    startActivity(i);
                    break;

                case R.id.btnSite:
                    Intent intent = new Intent(getContext(), EnvironmentalSiteActivity.class);
                    startActivity(intent);
                    break;

                case R.id.btnLogistics:
                    Intent siteIntent = new Intent(getContext(), LogisticsSurveyActivity.class);
                    startActivity(siteIntent);
                    break;
                case R.id.btnMant:
                    Intent mantenimientoIntent = new Intent(getContext(), MantenimientoSurveyActivity.class);
                    startActivity(mantenimientoIntent);
                    break;
                case R.id.btnMen:
                    Intent in = new Intent(getContext(), memoriaFiscalActivity.class);
                    startActivity(in);
                    break;
                case R.id.btnPid:
                    Intent intent1 = new Intent(getContext(),cambioPidPad.class);
                    startActivity(intent1);
                    break;
                case R.id.btnTech:
                    Intent intent2 = new Intent(getContext(),VisitaTecnica.class);
                    startActivity(intent2);
                    break;
                case R.id.btnReca:
                    Intent intent3 = new Intent(getContext(),TecladoEncryptorActivity.class);
                    startActivity(intent3);
                    break;
            }
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
                        recyclerView = (RecyclerView) layout.findViewById(R.id.recycleView);
                        ArrayList<EmailSender> emailSenders = Email.readEmails(getContext());
                        ArrayList<Information> informations = new ArrayList<>();
                        if (emailSenders != null
                                && !emailSenders.isEmpty()){
                            for (EmailSender emailSender:
                                 emailSenders) {
                                informations.add(
                                  new Information(
                                          R.drawable.fab_bg_mini,
                                          emailSender.getSubject()
                                  )
                                );
                            }
                        }

                        customAdapter = new CustomAdapter(getContext(),informations);
                        recyclerView.setAdapter(customAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        break;
                    default:
                        layout = inflater.inflate(R.layout.content_scrolling,container,false);
                        this.btnGoToparts = (ImageButton) layout.findViewById(R.id.btnGoToparts);
                        this.btnSite = (ImageButton) layout.findViewById(R.id.btnSite);
                        this.btnLogistics = (ImageButton) layout.findViewById(R.id.btnLogistics);
                        this.btnMant = (ImageButton) layout.findViewById(R.id.btnMant);
                        this.btnMen = (ImageButton) layout.findViewById(R.id.btnMen);
                        this.btnPid = (ImageButton) layout.findViewById(R.id.btnPid);
                        this.btnTech = (ImageButton) layout.findViewById(R.id.btnTech);
                        this.btnReca = (ImageButton) layout.findViewById(R.id.btnReca);

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
                        break;
                }
            }

            return layout;
        }
    }
}
