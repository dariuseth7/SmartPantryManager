package com.pantrymanager.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pantrymanager.app.R;

public class SettingsActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        bottomNav = findViewById(R.id.bottom_navigation);
        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
        bottomNav.setSelectedItemId(R.id.nav_settings);
        bottomNav.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_pantry) {
                    startActivity(new Intent(SettingsActivity.this, PantryActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (id == R.id.nav_recipes) {
                    startActivity(new Intent(SettingsActivity.this, SuggestedRecipesActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                }
                return id == R.id.nav_settings;
            }
        });
    }
}
