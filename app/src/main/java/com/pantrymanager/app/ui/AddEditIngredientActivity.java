package com.pantrymanager.app.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pantrymanager.app.R;
import com.pantrymanager.app.data.DatabaseHelper;

public class AddEditIngredientActivity extends AppCompatActivity {

    private EditText etName, etQuantity, etUnit, etExpiry;
    private Button btnSave;
    private TextView tvTitle;
    private DatabaseHelper dbHelper;
    private int ingredientId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_ingredient);

        dbHelper = new DatabaseHelper(this);

        tvTitle = findViewById(R.id.tv_form_title);
        etName = findViewById(R.id.et_name);
        etQuantity = findViewById(R.id.et_quantity);
        etUnit = findViewById(R.id.et_unit);
        etExpiry = findViewById(R.id.et_expiry);
        btnSave = findViewById(R.id.btn_save);

        // Check if editing existing item
        if (getIntent().hasExtra("INGREDIENT_ID")) {
            ingredientId = getIntent().getIntExtra("INGREDIENT_ID", -1);
            etName.setText(getIntent().getStringExtra("INGREDIENT_NAME"));
            etQuantity.setText(String.valueOf(getIntent().getDoubleExtra("INGREDIENT_QTY", 0)));
            etUnit.setText(getIntent().getStringExtra("INGREDIENT_UNIT"));
            etExpiry.setText(getIntent().getStringExtra("INGREDIENT_EXPIRY"));
            tvTitle.setText("Edit Pantry Item");
        }

        btnSave.setOnClickListener(v -> saveIngredient());
    }

    private void saveIngredient() {
        String name = etName.getText().toString().trim();
        String qtyStr = etQuantity.getText().toString().trim();
        String unit = etUnit.getText().toString().trim();
        String expiry = etExpiry.getText().toString().trim();

        // Input Validation (Section 3.1)
        if (name.isEmpty()) {
            etName.setError("Name is required");
            return;
        }
        if (qtyStr.isEmpty()) {
            etQuantity.setError("Quantity is required");
            return;
        }
        double quantity;
        try {
            quantity = Double.parseDouble(qtyStr);
            if (quantity <= 0) {
                etQuantity.setError("Quantity must be greater than 0");
                return;
            }
        } catch (NumberFormatException e) {
            etQuantity.setError("Enter a valid number");
            return;
        }
        if (unit.isEmpty()) {
            etUnit.setError("Unit is required (e.g., pcs, g, ml)");
            return;
        }

        if (ingredientId == -1) {
            dbHelper.addPantryItem(name, quantity, unit, expiry);
            Toast.makeText(this, "Ingredient added!", Toast.LENGTH_SHORT).show();
        } else {
            dbHelper.updatePantryItem(ingredientId, name, quantity, unit, expiry);
            Toast.makeText(this, "Ingredient updated!", Toast.LENGTH_SHORT).show();
        }

        finish();
    }
}