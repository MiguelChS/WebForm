package mc185249.webforms;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.text.SimpleDateFormat;
import java.util.ArrayList;

import Tabs.SlidingTabLayout;
import app.AppController;
import models.Email;
import models.EmailSender;
import sync.ClientsSyncAdapter;
import sync.ContactsSyncAdapter;
import sync.SynInventarioPartes;

public class ScrollingActivity extends AppCompatActivity {
  //region sync
    private static final int REQUEST_LOGIN_CODE = 9000;
    public static final long SYNC_INTERVAL = Long.parseLong(String.valueOf(R.string.pollFrequency));
    public static final String AUTHORITY = "ClientsContentProvider";
    ContentResolver mResolver;
    private String user = "";
    SlidingTabLayout mTabs;
    ViewPager mPager;
    AppController appController;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (isFirstTime()){
            new WebFormsPreferencesManager(this).put(WebFormsPreferencesManager.IS_FIRST_TIME,false);
            dialog = ProgressDialog.show(this, "Sincronizacion en proceso",
                    "Aguarde mientras sincronizamos su configuracion...", true);
        }
        appController = AppController.getInstance();
        //region verifica existencia de credenciales NCR
        Intent i = new Intent(this, EmailService.class);
        if (!AppController.getInstance().checkCredentials()) {
            showLogin();
        } else {
            appController.initializeSync();
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

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(syncFinishedReceiver,new IntentFilter(ClientsSyncAdapter.CLIENT_SYNC_FINISHED));
        registerReceiver(syncFinishedReceiver,new IntentFilter(ContactsSyncAdapter.SYNC_CONTACTOS));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(syncFinishedReceiver);
    }

    private BroadcastReceiver syncFinishedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            WebFormsPreferencesManager preferencesManager = new WebFormsPreferencesManager(getApplicationContext());
            if (preferencesManager.getSyncClientes()
                    && preferencesManager.getSyncContactos()
                    && isFirstTime()){
                dialog.dismiss();
            }
            switch (intent.getAction()){
                case ClientsSyncAdapter.CLIENT_SYNC_FINISHED:
                    preferencesManager.put(
                            WebFormsPreferencesManager.SYNCRONIZES_CLIENTES,true
                    );
                    break;
                case ContactsSyncAdapter.SYNC_CONTACTOS:
                    preferencesManager.put(
                            WebFormsPreferencesManager.SYNCRONIZES_CONTACTOS,true
                    );
                    break;
                case SynInventarioPartes.PARTES_SYNC:
                    preferencesManager.put(
                            WebFormsPreferencesManager.SYNCRONIZES_PARTES,true
                    );
                    break;
            }

        }
    };

    /**
     * Determina si es la primera vez que utiliza la app
     * @return true si es la primera vez, sino false
     */
    public Boolean isFirstTime(){
        WebFormsPreferencesManager webFormsPreferencesManager = new WebFormsPreferencesManager(this);
        return webFormsPreferencesManager.getFirstTime();
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
            appController.initializeSync();

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
           /* Drawable drawable = getResources().getDrawable(icons[position]);
            drawable.setBounds(0,0,36,36);
            ImageSpan imageSpan = new ImageSpan(drawable);
            SpannableString spannableString = new SpannableString(" ");
            spannableString.setSpan(imageSpan,0,spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return spannableString;*/
            return tabs[position];
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

        RecyclerView recyclerView;
        CustomAdapter customAdapter;

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
                        recyclerView = (RecyclerView) layout.findViewById(R.id.recycleView);
                        final ArrayList<EmailSender> emailSenders = Email.readEmails(getContext());
                        ArrayList<Information> informations = new ArrayList<>();
                        if (emailSenders != null
                                && !emailSenders.isEmpty()){
                            for (EmailSender emailSender:
                                 emailSenders) {
                                String strCurrentState = "";
                                switch (emailSender.getCurrentState()){
                                    case 0:
                                        strCurrentState = "Guardado localmente";
                                        break;
                                    case 2:
                                        strCurrentState = "Sincronizado con servidor";
                                        break;
                                    case 1:
                                        strCurrentState = "Guardado localmente. No fue posible sincronizar";
                                        break;
                                    case 3:
                                        strCurrentState = "Enviado";
                                        break;
                                }

                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
                                String text = emailSender.getSubject().split("-")[1];
                                informations.add(
                                  new Information(
                                          R.drawable.ic_launcher,
                                          text,
                                          dateFormat.format(emailSender.getFecha()),
                                          strCurrentState
                                  )
                                );
                                recyclerView.addOnItemTouchListener(
                                        new RecyclerItemClickListener(getContext(),new RecyclerItemClickListener.OnItemClickListener(){

                                            @Override
                                            public void onItemClick(View view, int position) {
                                               EmailSender emails = emailSenders.get(position);
                                                Intent intent = new Intent(getContext(),EmailActivity.class);
                                                intent.putExtra(EmailActivity.SUBJECT,emails.getSubject());
                                                intent.putExtra(EmailActivity.BODY,emails.getBody());
                                                intent.putExtra(EmailActivity.RECIPIENTS,emails.getRecipients());
                                                startActivity(intent);
                                            }
                                        })
                                );
                            }
                        }

                        customAdapter = new CustomAdapter(getContext(),informations);
                        recyclerView.setAdapter(customAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        break;
                    default:
                        layout = inflater.inflate(R.layout.content_scrolling,container,false);

                        RecyclerView rvForms = (RecyclerView)layout.findViewById(R.id.rvForms);
                        ScrollingActivityRvAdapter adapter =
                                new ScrollingActivityRvAdapter(getContext());
                        rvForms.setAdapter(adapter);
                        rvForms.setLayoutManager(new LinearLayoutManager(
                                getContext()
                        ));
                        break;
                }
            }

            return layout;
        }
    }
}
