package pl.piotrskiba.bakingapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.piotrskiba.bakingapp.R;
import pl.piotrskiba.bakingapp.models.Ingredient;
import pl.piotrskiba.bakingapp.models.Recipe;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder> {

    public List<Recipe> recipeList;

    final private ListItemClickListener mOnClickListener;

    public RecipeListAdapter(List<Recipe> recipeList, ListItemClickListener clickListener){
        this.recipeList = recipeList;
        this.mOnClickListener = clickListener;
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

        // set recipe image
        if(!TextUtils.isEmpty(recipeList.get(position).getImage())){
            Picasso.get()
                    .load(recipeList.get(position).getImage())
                    .placeholder(R.drawable.ic_image_black_24dp)
                    .error(R.drawable.ic_broken_image_black_24dp)
                    .into(holder.mRecipeImage);

            holder.mRecipeImage.setVisibility(View.VISIBLE);
        }
        else{
            holder.mRecipeImage.setVisibility(View.GONE);
        }

        // set recipe name
        holder.recipeNameTextView.setText(recipeList.get(position).getName());

        // set recipe ingredients
        StringBuilder stringBuilder = new StringBuilder();
        List<Ingredient> ingredients = recipeList.get(position).getIngredients();
        for(int i = 0; i < ingredients.size(); i++){
            stringBuilder.append(ingredients.get(i).getIngredient());
            if(i < ingredients.size()-1)
                stringBuilder.append(", ");
        }

        holder.recipeIngredientsTextView.setText(stringBuilder.toString());
    }

    @Override
    public int getItemCount() {
        if(recipeList == null)
            return 0;
        else
            return recipeList.size();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.iv_recipe_image)
        ImageView mRecipeImage;

        @BindView(R.id.tv_recipe_name)
        TextView recipeNameTextView;

        @BindView(R.id.tv_recipe_ingredients)
        TextView recipeIngredientsTextView;

        RecipeViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnClickListener.onClick(getAdapterPosition());
        }
    }

    public interface ListItemClickListener{
        void onClick(int index);
    }
}
