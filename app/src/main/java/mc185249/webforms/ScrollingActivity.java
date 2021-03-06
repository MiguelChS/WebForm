package mc185249.webforms;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.util.Log;
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

public class ScrollingActivity extends AppCompatActivity {
  //region sync
    private static final int REQUEST_LOGIN_CODE = 9000;
    ContentResolver mResolver;
    private String user = "";
    SlidingTabLayout mTabs;
    ViewPager mPager;
    AppController appController;
    ProgressDialog dialog;
    WebFormsPagerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        appController = AppController.getInstance();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLogin();
            }
        });


        mPager = (ViewPager)findViewById(R.id.pager);
        adapter = new WebFormsPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(adapter);
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

    }

    @Override
    protected void onResume() {
        super.onResume();
        mPager = (ViewPager)findViewById(R.id.pager);
        adapter = new WebFormsPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(adapter);
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
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

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
            appController.onDemandSyncClientesContactos();
            new WebFormsPreferencesManager(this).put(WebFormsPreferencesManager.IS_FIRST_TIME,false);
            dialog = ProgressDialog.show(this, "Sincronizacion en proceso",
                    "Aguarde mientras sincronizamos su configuracion...", true);
            new FirstSync().execute();

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

    private class FirstSync extends AsyncTask<Void,Void,Boolean>{

        @Override
        protected Boolean doInBackground(Void... voids) {
            Log.v("NCR","se llama a contactos sync adapter");
            new ContactsSyncAdapter(ScrollingActivity.this,true).doSync();
            Log.v("NCR","se llama a clientes sync adapter");
            new ClientsSyncAdapter(ScrollingActivity.this,true).doSync();
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            dialog.dismiss();

        }
    }
    public static class WebFormsTabsFragment extends Fragment{

        RecyclerView recyclerView;
        RecyclerView rvForms;
        CustomAdapter customAdapter;
        ScrollingActivityRvAdapter adapter;

        public static WebFormsTabsFragment getInstance(int position){
           WebFormsTabsFragment webFormsTabsFragment = new WebFormsTabsFragment();
            Bundle args = new Bundle();
            args.putInt("position",position);
            webFormsTabsFragment.setArguments(args);
            return webFormsTabsFragment;
        }

        public  void refreshRecyclerView(Context context){
            adapter =
                    new ScrollingActivityRvAdapter(context);
            rvForms.setAdapter(adapter);
            rvForms.setLayoutManager(new LinearLayoutManager(
                    getContext()
            ));
            adapter.notifyDataSetChanged();
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

                         rvForms = (RecyclerView)layout.findViewById(R.id.rvForms);
                         refreshRecyclerView(getActivity().getApplicationContext());
                        break;
                }
            }

            return layout;
        }
    }
}
