package pl.piotrskiba.bakingapp;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.piotrskiba.bakingapp.models.Recipe;
import pl.piotrskiba.bakingapp.models.Step;

public class StepDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);

        Intent parentIntent = getIntent();
        if(parentIntent.hasExtra(MainActivity.KEY_RECIPE) && parentIntent.hasExtra(Intent.EXTRA_INDEX)){
            Recipe recipe = (Recipe) parentIntent.getSerializableExtra(MainActivity.KEY_RECIPE);
            Step step = recipe.getSteps().get(parentIntent.getIntExtra(Intent.EXTRA_INDEX, 0));

            getSupportActionBar().setTitle(recipe.getName() + " - " + step.getShortDescription());


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
}
