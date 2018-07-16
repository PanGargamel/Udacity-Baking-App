package pl.piotrskiba.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.piotrskiba.bakingapp.adapters.RecipeListAdapter;
import pl.piotrskiba.bakingapp.interfaces.GetDataService;
import pl.piotrskiba.bakingapp.models.Recipe;
import pl.piotrskiba.bakingapp.network.RetrofitClientInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements RecipeListAdapter.ListItemClickListener {

    @BindView(R.id.pb_loading)
    ProgressBar mLoadingIndicator;

    @BindView(R.id.rv_recipes)
    RecyclerView mRecipeList;
    private RecipeListAdapter mRecipeListAdapter;

    public final static String KEY_RECIPE = "recipe";
    public final static String KEY_WIDGET_ID = "widget_id";
    public final static String ACTION_SELECT_RECIPE = "select_recipe";

    private List<Recipe> mRecipes;
    private boolean mRecipeSelection;
    private int mWidgetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        loadData();
    }

    private void loadData(){

        showLoadingIndicator();

        /*
            Get Recipe List using Retrofit
            created basing on this tutorial:
            https://medium.com/@prakash_pun/retrofit-a-simple-android-tutorial-48437e4e5a23
         */

        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<List<Recipe>> call = service.getAllRecipes();
        call.enqueue(new Callback<List<Recipe>>(){

            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                mRecipes = response.body();
                setupRecyclerView(mRecipes);
                hideLoadingIndicator();
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Toast.makeText(MainActivity.this, getString(R.string.error), Toast.LENGTH_LONG).show();
                Log.w(getClass().getSimpleName(), t.getMessage());
                hideLoadingIndicator();
            }
        });
    }

    private void showLoadingIndicator(){
        mLoadingIndicator.setVisibility(View.VISIBLE);
        mRecipeList.setVisibility(View.INVISIBLE);
    }

    private void hideLoadingIndicator(){
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mRecipeList.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        this.setIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // check if the app was launched using widget for a recipe selection
        Intent parentIntent = getIntent();
        mRecipeSelection = parentIntent.getBooleanExtra(ACTION_SELECT_RECIPE, false);
        mWidgetId = parentIntent.getIntExtra(KEY_WIDGET_ID, 0);

        if(mRecipeSelection){
            setTitle(getString(R.string.select_recipe));
        }
        else{
            setTitle(getString(R.string.app_name));
        }
    }

    private void setupRecyclerView(List<Recipe> recipeList){
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            mRecipeList.setLayoutManager(layoutManager);
        }
        else {
            GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
            mRecipeList.setLayoutManager(layoutManager);
        }

        mRecipeList.setHasFixedSize(true);

        mRecipeListAdapter = new RecipeListAdapter(recipeList, this);
        mRecipeList.setAdapter(mRecipeListAdapter);
    }

    @Override
    public void onClick(int index) {
        if(!mRecipeSelection) {
            Intent intent = new Intent(this, StepListActivity.class);
            intent.putExtra(KEY_RECIPE, mRecipeListAdapter.recipeList.get(index));
            startActivity(intent);
        }
        else{
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            Gson gson = new Gson();
            String json = gson.toJson(mRecipes.get(index));
            editor.putString(Integer.toString(mWidgetId), json);
            editor.commit();


            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            int[] widgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, IngredientsWidgetProvider.class));

            for(int i = 0; i < widgetIds.length; i++){
                IngredientsWidgetProvider.updateAppWidget(this, appWidgetManager, widgetIds[i]);
            }

            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        loadData();
        Toast.makeText(this, getString(R.string.refreshing), Toast.LENGTH_SHORT).show();

        return super.onOptionsItemSelected(item);
    }
}
