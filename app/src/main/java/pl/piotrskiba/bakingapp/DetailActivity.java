package pl.piotrskiba.bakingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.piotrskiba.bakingapp.adapters.StepListAdapter;
import pl.piotrskiba.bakingapp.models.Ingredient;
import pl.piotrskiba.bakingapp.models.Recipe;
import pl.piotrskiba.bakingapp.models.Step;

public class DetailActivity extends AppCompatActivity implements StepListAdapter.StepListItemClickListener {

    @BindView(R.id.rv_steps)
    RecyclerView mStepList;

    private StepListAdapter mStepListAdapter;

    Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        Intent parentIntent = getIntent();
        if(parentIntent.hasExtra(MainActivity.KEY_RECIPE)){
            mRecipe = (Recipe) parentIntent.getSerializableExtra(MainActivity.KEY_RECIPE);
            getSupportActionBar().setTitle(mRecipe.getName());


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

            setupRecyclerView(mRecipe.getSteps());
        }
    }

    private void setupRecyclerView(List<Step> stepList){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mStepList.setLayoutManager(layoutManager);
        mStepList.setHasFixedSize(true);

        mStepListAdapter = new StepListAdapter(this, stepList, this);
        mStepList.setAdapter(mStepListAdapter);
    }

    @Override
    public void onClick(int index) {
        Intent intent = new Intent(this, StepDetailActivity.class);
        intent.putExtra(MainActivity.KEY_RECIPE, mRecipe);
        intent.putExtra(Intent.EXTRA_INDEX, index);
        startActivity(intent);
    }
}
