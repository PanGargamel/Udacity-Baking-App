package pl.piotrskiba.bakingapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.piotrskiba.bakingapp.adapters.StepListAdapter;
import pl.piotrskiba.bakingapp.models.Step;

public class StepListFragment extends Fragment implements StepListAdapter.StepListItemClickListener {

    @BindView(R.id.rv_steps)
    RecyclerView mStepList;

    private List<Step> stepList;

    public final static String TAG = "FRAGMENT_STEP_LIST";

    StepListAdapter stepListAdapter;

    public StepListFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step_list, container, false);

        ButterKnife.bind(this, rootView);

        setRetainInstance(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mStepList.setLayoutManager(layoutManager);
        mStepList.setHasFixedSize(true);

        stepListAdapter = new StepListAdapter(getContext(), stepList, this);
        mStepList.setAdapter(stepListAdapter);

        return rootView;
    }

    public void setStepList(List<Step> stepList){
        this.stepList = stepList;
    }

    @Override
    public void onClick(int index) {
        ((StepListActivity) getActivity()).onClick(index);
        StepListActivity.selectedStep = index;
    }
}
