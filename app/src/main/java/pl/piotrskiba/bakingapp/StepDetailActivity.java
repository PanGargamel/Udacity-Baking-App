package pl.piotrskiba.bakingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.piotrskiba.bakingapp.models.Recipe;
import pl.piotrskiba.bakingapp.models.Step;

public class StepDetailActivity extends AppCompatActivity {

    Recipe mRecipe;
    Step mStep;

    @BindView(R.id.tv_step_description)
    TextView mStepDescriptionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);

        ButterKnife.bind(this);

        Intent parentIntent = getIntent();
        if(parentIntent.hasExtra(MainActivity.KEY_RECIPE) && parentIntent.hasExtra(Intent.EXTRA_INDEX)){
            mRecipe = (Recipe) parentIntent.getSerializableExtra(MainActivity.KEY_RECIPE);
            mStep = (Step) mRecipe.getSteps().get(parentIntent.getIntExtra(Intent.EXTRA_INDEX, 0));

            getSupportActionBar().setTitle(mRecipe.getName() + " - " + mStep.getShortDescription());

            mStepDescriptionTextView.setText(mStep.getDescription());
        }
    }
}
