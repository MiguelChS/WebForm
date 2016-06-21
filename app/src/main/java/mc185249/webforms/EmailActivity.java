package mc185249.webforms;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.webkit.WebView;
import android.widget.TextView;

public class EmailActivity extends AppCompatActivity {

    /**
     * KEY PARA ACCEDER A LOS PARAMETROS DE LA LLAMADA DEL INTENT
     */
    public static String SUBJECT = "SUBJECT";
    public static String BODY = "BODY";
    public static String RECIPIENTS = "RECIPIENTS";

    String subject, body,recipients;

    TextView textView_subject,textView_recipient;
    WebView webView_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        textView_subject = (TextView) findViewById(R.id.textView_subject);
        webView_email = (WebView)findViewById(R.id.webView_email);
        textView_recipient = (TextView)findViewById(R.id.textView_recipient);

        Intent intent = getIntent();
        subject = intent.getStringExtra(SUBJECT);
        body = intent.getStringExtra(BODY);
        recipients = intent.getStringExtra(RECIPIENTS);

        textView_subject.setText(subject);
        textView_recipient.setText(recipients);
        webView_email.getSettings().setDomStorageEnabled(true);
        webView_email.loadDataWithBaseURL(null,body,"html/css","UTF-8",null);
    }
}
