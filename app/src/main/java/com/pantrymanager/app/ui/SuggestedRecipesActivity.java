package com.pantrymanager.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pantrymanager.app.R;
import com.pantrymanager.app.adapters.RecipeAdapter;
import com.pantrymanager.app.data.DatabaseHelper;
import com.pantrymanager.app.models.Ingredient;
import com.pantrymanager.app.models.Recipe;
import com.pantrymanager.app.utils.IngredientMatcher;

import java.util.ArrayList;
import java.util.List;

public class SuggestedRecipesActivity extends AppCompatActivity {

    private RecyclerView rvRecipes;
    private RecipeAdapter adapter;
    private TextView tvEmptyState;
    private BottomNavigationView bottomNav;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_recipes);

        dbHelper = new DatabaseHelper(this);
        rvRecipes = findViewById(R.id.rv_suggested_recipes);
        tvEmptyState = findViewById(R.id.tv_empty_state);
        bottomNav = findViewById(R.id.bottom_navigation);

        rvRecipes.setLayoutManager(new LinearLayoutManager(this));

        adapter = new RecipeAdapter(new ArrayList<>(), recipe -> {
            Intent intent = new Intent(SuggestedRecipesActivity.this, RecipeDetailActivity.class);
            intent.putExtra("RECIPE_ID", recipe.getId());
            startActivity(intent);
        });

        rvRecipes.setAdapter(adapter);
        setupBottomNavigation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSuggestedRecipes();
    }

    private void loadSuggestedRecipes() {
        List<Ingredient> userPantry = dbHelper.getAllPantryItems();
        List<Recipe> allRecipes = dbHelper.getAllRecipes();
        List<Recipe> matchingRecipes = new ArrayList<>();

        // Execute Strict-Matching Logic (Section 2.3)
        for (Recipe recipe : allRecipes) {
            if (IngredientMatcher.matchesStrictly(recipe.getRequiredIngredients(), userPantry)) {
                matchingRecipes.add(recipe);
            }
        }

        // Empty State Handler (Section 2.2)
        if (matchingRecipes.isEmpty()) {
            tvEmptyState.setVisibility(View.VISIBLE);
            rvRecipes.setVisibility(View.GONE);
        } else {
            tvEmptyState.setVisibility(View.GONE);
            rvRecipes.setVisibility(View.VISIBLE);
            adapter.updateList(matchingRecipes);
        }
    }

    private void setupBottomNavigation() {
        bottomNav.setSelectedItemId(R.id.nav_recipes);
        bottomNav.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_pantry) {
                    startActivity(new Intent(SuggestedRecipesActivity.this, PantryActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (id == R.id.nav_settings) {
                    startActivity(new Intent(SuggestedRecipesActivity.this, SettingsActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                }
                return id == R.id.nav_recipes;
            }
        });
    }
}