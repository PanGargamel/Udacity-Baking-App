package pl.piotrskiba.bakingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.piotrskiba.bakingapp.models.Ingredient;
import pl.piotrskiba.bakingapp.models.Recipe;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder> {

    private List<Recipe> recipeList;

    public RecipeListAdapter(List<Recipe> recipeList){
        this.recipeList = recipeList;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.recipe_list_item, parent, false);
        RecipeViewHolder viewHolder = new RecipeViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {

        // set recipe name
        holder.recipeNameTextView.setText(recipeList.get(position).getName());

        // set recipe ingredients
        StringBuilder stringBuilder = new StringBuilder();
        Ingredient[] ingredients = recipeList.get(position).getIngredients();
        for(int i = 0; i < ingredients.length; i++){
            stringBuilder.append(ingredients[i].getIngredient());
            if(i < ingredients.length-1)
                stringBuilder.append(", ");
        }

        holder.recipeIngredientsTextView.setText(stringBuilder.toString());
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_recipe_name)
        TextView recipeNameTextView;

        @BindView(R.id.tv_recipe_ingredients)
        TextView recipeIngredientsTextView;

        public RecipeViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
