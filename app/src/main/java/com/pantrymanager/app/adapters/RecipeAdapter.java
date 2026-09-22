package com.pantrymanager.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.pantrymanager.app.R;
import com.pantrymanager.app.models.Recipe;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private List<Recipe> recipeList;
    private OnRecipeClickListener listener;

    public interface OnRecipeClickListener {
        void onRecipeClick(Recipe recipe);
    }

    public RecipeAdapter(List<Recipe> recipeList, OnRecipeClickListener listener) {
        this.recipeList = recipeList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe_row, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe currentRecipe = recipeList.get(position);
        holder.tvName.setText(currentRecipe.getName());
        holder.tvIngredientCount.setText(currentRecipe.getRequiredIngredients().size() + " Required Ingredients (100% Strict Match)");

        holder.itemView.setOnClickListener(v -> listener.onRecipeClick(currentRecipe));
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public void updateList(List<Recipe> newList) {
        recipeList = newList;
        notifyDataSetChanged();
    }

    static class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvIngredientCount;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_recipe_name);
            tvIngredientCount = itemView.findViewById(R.id.tv_recipe_ingredient_count);
        }
    }
}