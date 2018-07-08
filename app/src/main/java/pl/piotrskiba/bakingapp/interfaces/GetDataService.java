package pl.piotrskiba.bakingapp.interfaces;

import java.util.List;

import pl.piotrskiba.bakingapp.models.Recipe;
import retrofit2.Call;
import retrofit2.http.GET;

public interface GetDataService {

    @GET("/topher/2017/May/59121517_baking/baking.json")
    Call<List<Recipe>> getAllRecipes();
}
