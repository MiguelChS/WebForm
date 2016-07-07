package mc185249.webforms;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.fcannizzaro.materialstepper.AbstractStep;
import com.github.fcannizzaro.materialstepper.style.BaseStyle;
import com.github.fcannizzaro.materialstepper.style.TextStepper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jn185090 on 4/13/2016.
 */
public class Stepper extends TextStepper {

    private int i = 0;

    public Stepper() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setErrorTimeout(1000);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setTitle("");
        setColorPrimary(R.color.colorPrimary);
        addStep(createFragment(new Step()));
        addStep(createFragment(new LoginStep()));
        super.onCreate(savedInstanceState);
    }

    private AbstractStep createFragment(AbstractStep fragment) {
        Bundle bundle = new Bundle();
        bundle.putInt("position", i++);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onComplete() {
        EditText editTextuser = (EditText) findViewById(R.id.input_email);
        EditText editTextCSR = (EditText) findViewById(R.id.csrCode_edit);
        String user = editTextuser.getText().toString().trim() + "@ncr.com";
        String CSRCode = editTextCSR.getText().toString().trim();
        Intent intent = new Intent();
        intent.putExtra(String.valueOf(R.string.accountName), user);
        intent.putExtra(String.valueOf(R.string.passwd), "asd");
        intent.putExtra(String.valueOf(R.string.CSRCode), CSRCode);

        this.setResult(RESULT_OK, intent);
        //setResult(RESULT_OK, intent);
        this.finish();
    }
}




