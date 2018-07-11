package pl.piotrskiba.bakingapp;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.ButterKnife;
import pl.piotrskiba.bakingapp.models.Ingredient;
import pl.piotrskiba.bakingapp.models.Recipe;
import pl.piotrskiba.bakingapp.models.Step;

public class DetailActivity extends AppCompatActivity {

    Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_list);

        ButterKnife.bind(this);

        Intent parentIntent = getIntent();
        if(parentIntent.hasExtra(MainActivity.KEY_RECIPE)){
            mRecipe = (Recipe) parentIntent.getSerializableExtra(MainActivity.KEY_RECIPE);
            getSupportActionBar().setTitle(mRecipe.getName());

            // check if fragment is not already instantiated
            FragmentManager fragmentManager = getSupportFragmentManager();

            StepListFragment stepListFragment = (StepListFragment) fragmentManager.findFragmentByTag(StepListFragment.TAG);
            if(stepListFragment == null) {

                // add step with ingredient list to the beginning
                StringBuilder stepDescription = new StringBuilder();
                stepDescription.append(getString(R.string.ingredients) + ":\n");
                for(int i = 0; i < mRecipe.getIngredients().size(); i++){
                    Ingredient ingredient = mRecipe.getIngredients().get(i);

                    // convert numbers like 5.0 to 5
                    String quantity;
                    if(ingredient.getQuantity() == (int) ingredient.getQuantity())
                        quantity = String.format("%d",(int) ingredient.getQuantity());
                    else
                        quantity = String.format("%s", ingredient.getQuantity());

                    stepDescription.append(" - " + ingredient.getIngredient() + " (" + quantity + " " + ingredient.getMeasure() + ")");
                    if(i < mRecipe.getIngredients().size() - 1)
                        stepDescription.append("\n");
                }

                Step step = new Step(-1, getString(R.string.ingredients), stepDescription.toString(), "", "");
                mRecipe.addStep(step);

                // instantiate fragment with step list
                stepListFragment = new StepListFragment();
                stepListFragment.setStepList(mRecipe.getSteps());

                fragmentManager.beginTransaction()
                        .add(R.id.step_list_container, stepListFragment, StepListFragment.TAG)
                        .commit();
            }
        }
    }

    public void onClick(int index) {
        Intent intent = new Intent(this, StepDetailActivity.class);
        intent.putExtra(MainActivity.KEY_RECIPE, mRecipe);
        intent.putExtra(Intent.EXTRA_INDEX, index);
        startActivity(intent);
    }
}
