package pl.piotrskiba.bakingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.piotrskiba.bakingapp.interfaces.GetDataService;
import pl.piotrskiba.bakingapp.models.Recipe;
import pl.piotrskiba.bakingapp.network.RetrofitClientInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.rv_recipes)
    RecyclerView mRecipeList;
    private RecipeListAdapter mRecipeListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

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
                setupRecyclerView(response.body());
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                // TODO: move string to strings.xml
                Toast.makeText(MainActivity.this, "Error occurred. Please try again later.\n" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupRecyclerView(List<Recipe> recipeList){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecipeList.setLayoutManager(layoutManager);
        mRecipeList.setHasFixedSize(true);

        mRecipeListAdapter = new RecipeListAdapter(recipeList);
        mRecipeList.setAdapter(mRecipeListAdapter);
    }
}
