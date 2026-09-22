package com.pantrymanager.app.ui;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.pantrymanager.app.R;
import com.pantrymanager.app.data.DatabaseHelper;
import com.pantrymanager.app.models.Ingredient;
import com.pantrymanager.app.models.Recipe;

import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity {

    private TextView tvTitle, tvIngredients, tvInstructions;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        dbHelper = new DatabaseHelper(this);

        tvTitle = findViewById(R.id.tv_detail_title);
        tvIngredients = findViewById(R.id.tv_detail_ingredients);
        tvInstructions = findViewById(R.id.tv_detail_instructions);

        int recipeId = getIntent().getIntExtra("RECIPE_ID", -1);
        if (recipeId != -1) {
            loadRecipeDetails(recipeId);
        }
    }

    private void loadRecipeDetails(int recipeId) {
        List<Recipe> allRecipes = dbHelper.getAllRecipes();
        for (Recipe r : allRecipes) {
            if (r.getId() == recipeId) {
                tvTitle.setText(r.getName());
                tvInstructions.setText(r.getInstructions());

                StringBuilder ingBuilder = new StringBuilder();
                for (Ingredient ing : r.getRequiredIngredients()) {
                    ingBuilder.append("• ")
                            .append(ing.getName())
                            .append(" - ")
                            .append(ing.getQuantity())
                            .append(" ")
                            .append(ing.getUnit())
                            .append("\n");
                }
                tvIngredients.setText(ingBuilder.toString());
                break;
            }
        }
    }
}