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
import pl.piotrskiba.bakingapp.models.Recipe;
import pl.piotrskiba.bakingapp.models.Step;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.rv_steps)
    RecyclerView mStepList;

    private StepListAdapter mStepListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        Intent parentIntent = getIntent();
        if(parentIntent.hasExtra(MainActivity.KEY_RECIPE)){
            Recipe recipe = (Recipe) parentIntent.getSerializableExtra(MainActivity.KEY_RECIPE);
            getSupportActionBar().setTitle(recipe.getName());


            // add step with ingredient list to the beginning
            StringBuilder stepDescription = new StringBuilder();
            stepDescription.append(getString(R.string.ingredients) + ":\n");
            for(int i = 0; i < recipe.getIngredients().size(); i++){
                stepDescription.append(" - " + recipe.getIngredients().get(i));
            }

            Step step = new Step(-1, getString(R.string.ingredients), stepDescription.toString(), "", "");
            recipe.addStep(step);

            setupRecyclerView(recipe.getSteps());
        }
    }

    private void setupRecyclerView(List<Step> stepList){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mStepList.setLayoutManager(layoutManager);
        mStepList.setHasFixedSize(true);

        mStepListAdapter = new StepListAdapter(this, stepList);
        mStepList.setAdapter(mStepListAdapter);
    }
}
