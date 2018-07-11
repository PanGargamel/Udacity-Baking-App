package pl.piotrskiba.bakingapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.piotrskiba.bakingapp.models.Step;

public class StepDetailFragment extends Fragment {

    @BindView(R.id.tv_step_description)
    TextView mStepDescriptionTextView;

    Step mStep;

    public final static String TAG = "FRAGMENT_STEP_DETAIL";

    public StepDetailFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step_detail, container, false);

        ButterKnife.bind(this, rootView);

        setRetainInstance(true);

        updateUI();

        return rootView;
    }

    public void setStep(Step step){
        this.mStep = step;
    }

    public void updateUI(){
        mStepDescriptionTextView.setText(mStep.getDescription());
    }
}
