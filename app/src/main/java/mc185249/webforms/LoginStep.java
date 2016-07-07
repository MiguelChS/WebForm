package mc185249.webforms;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.github.fcannizzaro.materialstepper.AbstractStep;
import com.github.fcannizzaro.materialstepper.style.BaseStyle;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginStep extends AbstractStep {
    private int i = 2;
    private Button button;
    private final static String CLICK = "click";
    private final static String NEXT_DATA = "next";

    public EditText editTextUser, editTextCSRCode;

    public LoginStep() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.login_step,container,false);
        editTextCSRCode = (EditText) view.findViewById(R.id.csrCode_edit);
        editTextUser = (EditText) view.findViewById(R.id.input_email);
        button = (Button) view.findViewById(R.id.button);
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CLICK,i);
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
        return matcher.find();
    }




    @Override
    public String error() {

        String email = editTextUser.getText().toString().trim() + "@ncr.com";

        String csr = editTextCSRCode.getText().toString().trim();
        if (isCSRCodeValid(csr)){
           mStepper.onComplete();
        }

        return "Verifique los datos";
    }

    @Override
    public String name() {
        return "Step 2";
    }
}
