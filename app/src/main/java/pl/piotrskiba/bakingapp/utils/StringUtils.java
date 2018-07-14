package pl.piotrskiba.bakingapp.utils;

import android.content.Context;

import pl.piotrskiba.bakingapp.R;
import pl.piotrskiba.bakingapp.models.Ingredient;
import pl.piotrskiba.bakingapp.models.Recipe;

public class StringUtils {
    public String getIngredientsList(Context context, Recipe recipe){
        StringBuilder ingredientList = new StringBuilder();

        ingredientList.append(context.getString(R.string.ingredients) + ":\n");

        for(int i = 0; i < recipe.getIngredients().size(); i++){
            Ingredient ingredient = recipe.getIngredients().get(i);

            // convert numbers like 5.0 to 5
            String quantity;
            if(ingredient.getQuantity() == (int) ingredient.getQuantity())
                quantity = String.format("%d",(int) ingredient.getQuantity());
            else
                quantity = String.format("%s", ingredient.getQuantity());

            ingredientList.append(" - " + ingredient.getIngredient() + " (" + quantity + " " + ingredient.getMeasure() + ")");
            if(i < recipe.getIngredients().size() - 1)
                ingredientList.append("\n");
        }

        return ingredientList.toString();
    }
}
