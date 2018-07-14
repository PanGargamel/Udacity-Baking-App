package pl.piotrskiba.bakingapp;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.ButterKnife;
import pl.piotrskiba.bakingapp.models.Recipe;
import pl.piotrskiba.bakingapp.models.Step;
import pl.piotrskiba.bakingapp.utils.StringUtils;

public class StepListActivity extends AppCompatActivity {

    Recipe mRecipe;
    public static Boolean mTwoPane = false;

    public static int selectedStep = 0;

    final static String KEY_SELECTED_STEP = "selected_step";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_list);

        ButterKnife.bind(this);

        if(findViewById(R.id.two_pane_layout) != null)
            mTwoPane = true;

        Intent parentIntent = getIntent();
        if(parentIntent.hasExtra(MainActivity.KEY_RECIPE)){
            mRecipe = (Recipe) parentIntent.getSerializableExtra(MainActivity.KEY_RECIPE);

            if(!mTwoPane)
                getSupportActionBar().setTitle(mRecipe.getName());

            // check if fragment is not already instantiated
            FragmentManager fragmentManager = getSupportFragmentManager();

            StepListFragment stepListFragment = (StepListFragment) fragmentManager.findFragmentByTag(StepListFragment.TAG);
            if(stepListFragment == null) {

                // add step with ingredient list to the beginning
                Step step = new Step(-1, getString(R.string.ingredients), new StringUtils().getIngredientsList(this, mRecipe), "", "");
                mRecipe.addStep(step);

                // instantiate fragment with step list
                stepListFragment = new StepListFragment();
                stepListFragment.setStepList(mRecipe.getSteps());

                fragmentManager.beginTransaction()
                        .add(R.id.step_list_container, stepListFragment, StepListFragment.TAG)
                        .commit();
            }
        }

        // load selected position after rotation change
        if(savedInstanceState != null && savedInstanceState.containsKey(KEY_SELECTED_STEP)){
            selectedStep = savedInstanceState.getInt(KEY_SELECTED_STEP);
        }

        if(mTwoPane){
            Step step = mRecipe.getSteps().get(selectedStep);

            getSupportActionBar().setTitle(mRecipe.getName() + " - " + step.getShortDescription());


            // instantiate fragment with step details
            FragmentManager fragmentManager = getSupportFragmentManager();

            StepDetailFragment stepDetailFragment = (StepDetailFragment) fragmentManager.findFragmentByTag(StepDetailFragment.TAG);
            if(stepDetailFragment == null) {
                stepDetailFragment = new StepDetailFragment();
                stepDetailFragment.setStep(step);

                fragmentManager.beginTransaction()
                        .add(R.id.step_detail_container, stepDetailFragment, StepDetailFragment.TAG)
                        .commit();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(KEY_SELECTED_STEP, selectedStep);
    }

    public void onClick(int index) {
        selectedStep = index;

        if(mTwoPane){
            FragmentManager fragmentManager = getSupportFragmentManager();

            StepDetailFragment stepDetailFragment = (StepDetailFragment) fragmentManager.findFragmentByTag(StepDetailFragment.TAG);
            if(stepDetailFragment != null) {
                stepDetailFragment.setStep(mRecipe.getSteps().get(selectedStep));
                stepDetailFragment.updateUI();
            }
        }
        else {
            Intent intent = new Intent(this, StepDetailActivity.class);
            intent.putExtra(MainActivity.KEY_RECIPE, mRecipe);
            intent.putExtra(Intent.EXTRA_INDEX, index);
            startActivity(intent);
        }
    }
}
