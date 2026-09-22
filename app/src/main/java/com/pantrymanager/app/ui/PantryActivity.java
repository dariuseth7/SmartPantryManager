package com.pantrymanager.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pantrymanager.app.R;
import com.pantrymanager.app.adapters.PantryAdapter;
import com.pantrymanager.app.data.DatabaseHelper;
import com.pantrymanager.app.models.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class PantryActivity extends AppCompatActivity {

    private RecyclerView rvPantry;
    private PantryAdapter adapter;
    private List<Ingredient> ingredientList;
    private DatabaseHelper dbHelper;
    private FloatingActionButton fabAdd;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry);

        dbHelper = new DatabaseHelper(this);
        rvPantry = findViewById(R.id.rv_pantry_list);
        fabAdd = findViewById(R.id.fab_add_ingredient);
        bottomNav = findViewById(R.id.bottom_navigation);

        ingredientList = new ArrayList<>();
        rvPantry.setLayoutManager(new LinearLayoutManager(this));

        adapter = new PantryAdapter(ingredientList, new PantryAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(Ingredient ingredient) {
                Intent intent = new Intent(PantryActivity.this, AddEditIngredientActivity.class);
                intent.putExtra("INGREDIENT_ID", ingredient.getId());
                intent.putExtra("INGREDIENT_NAME", ingredient.getName());
                intent.putExtra("INGREDIENT_QTY", ingredient.getQuantity());
                intent.putExtra("INGREDIENT_UNIT", ingredient.getUnit());
                intent.putExtra("INGREDIENT_EXPIRY", ingredient.getExpiryDate());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(Ingredient ingredient) {
                dbHelper.deletePantryItem(ingredient.getId());
                Toast.makeText(PantryActivity.this, ingredient.getName() + " deleted", Toast.LENGTH_SHORT).show();
                loadPantryData();
            }
        });

        rvPantry.setAdapter(adapter);

        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(PantryActivity.this, AddEditIngredientActivity.class);
            startActivity(intent);
        });

        setupBottomNavigation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPantryData();
    }

    private void loadPantryData() {
        ingredientList = dbHelper.getAllPantryItems();
        adapter.updateList(ingredientList);
    }

    private void setupBottomNavigation() {
        bottomNav.setSelectedItemId(R.id.nav_pantry);
        bottomNav.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_recipes) {
                    startActivity(new Intent(PantryActivity.this, SuggestedRecipesActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (id == R.id.nav_settings) {
                    startActivity(new Intent(PantryActivity.this, SettingsActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                }
                return id == R.id.nav_pantry;
            }
        });
    }
}