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
public  class Stepper extends TextStepper {

    private int i = 0;
    public Stepper() {
        super();
    }
    public static interface ICallback{
        void retorno();
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

    private AbstractStep createFragment(AbstractStep fragment){
        Bundle bundle = new Bundle();
        bundle.putInt("position", i++);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public  void onComplete() {
        EditText editTextuser = (EditText) findViewById(R.id.input_email);
        EditText editTextCSR = (EditText) findViewById(R.id.csrCode_edit);
        String user = editTextuser.getText().toString().trim() + "@ncr.com";
        String CSRCode = editTextCSR.getText().toString().trim();
        Intent intent = new Intent();
        intent.putExtra(String.valueOf(R.string.accountName),user);
        intent.putExtra(String.valueOf(R.string.passwd),"asd");
        intent.putExtra(String.valueOf(R.string.CSRCode),CSRCode);

        this.setResult(RESULT_OK,intent);
        //setResult(RESULT_OK, intent);
        this.finish();
    }

    public  class Step extends AbstractStep{

        private int i = 1;
        private android.widget.Button button;
        private TextView editText_texto;

        @Override
        public String name() {
            return "NCR";
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.step, container, false);
            editText_texto = (TextView) view.findViewById(R.id.texto);
            return view;
        }


        public Step() {
            super();
        }

        @Override
        public AbstractStep stepper(BaseStyle mStepper) {
            return super.stepper(mStepper);
        }

        @Override
        public String optional() {
            return "Omitir";
        }

        @Override
        public boolean isOptional() {
            return true;
        }

        @Override
        public void onStepVisible() {
            super.onStepVisible();
        }

        @Override
        public boolean nextIf() {
            return i < 2;
        }

        @Override
        public String error() {
            return "Click";
        }
    }




    public class LoginStep extends AbstractStep{
        private int i = 2;

        public  EditText editTextUser, editTextCSRCode;

        public LoginStep() {
            super();
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.login_step,container,false);
            editTextCSRCode = (EditText) view.findViewById(R.id.csrCode_edit);
            editTextUser = (EditText) view.findViewById(R.id.input_email);
            editTextCSRCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus){
                        if (!isCSRCodeValid(editTextCSRCode.getText().toString().trim())){
                            editTextCSRCode.setError("CSR Code incorrecto!");
                        }
                    }
                }
            });


            return view;
        }

        @Override
        public AbstractStep stepper(BaseStyle mStepper) {
            return super.stepper(mStepper);
        }



        @Override
        public String optional() {
            return super.optional();
        }

        @Override
        public boolean isOptional() {
            return super.isOptional();
        }

        @Override
        public void onStepVisible() {
            super.onStepVisible();
        }

        @Override
        public boolean nextIf() {
            return false;
        }

        private  boolean isEmailValid(String email){
            return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }

        private boolean isCSRCodeValid(String csrCode){
            Pattern pattern = Pattern.compile("[a-zA-Z]{2}[0-9]{3}[a-zA-Z]");
            Matcher matcher = pattern.matcher(csrCode);
            if (matcher.find()){
                return true;
            }
            return false;
        }

        @Override
        public String error() {
            String email = editTextUser.getText().toString().trim() + "@ncr.com";

            String csr = editTextCSRCode.getText().toString().trim();
            if (isCSRCodeValid(csr)){
                onComplete();
                return "";
            }

            return "Verifique los datos";
        }

        @Override
        public String name() {
            return "Step 1";
        }
    }
}
