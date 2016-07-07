package mc185249.webforms;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.fcannizzaro.materialstepper.AbstractStep;
import com.github.fcannizzaro.materialstepper.style.BaseStyle;

public class Step extends AbstractStep {

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
