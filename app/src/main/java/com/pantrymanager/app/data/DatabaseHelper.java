package com.pantrymanager.app.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.pantrymanager.app.models.Ingredient;
import com.pantrymanager.app.models.Recipe;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "pantry_manager.db";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    public static final String TABLE_PANTRY = "pantry";
    public static final String TABLE_RECIPES = "recipes";
    public static final String TABLE_RECIPE_INGREDIENTS = "recipe_ingredients";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Table 1: User's Pantry Items
        String CREATE_PANTRY_TABLE = "CREATE TABLE " + TABLE_PANTRY + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "quantity REAL NOT NULL, " +
                "unit TEXT NOT NULL, " +
                "expiry_date TEXT)";

        // Table 2: Pre-loaded Recipes
        String CREATE_RECIPES_TABLE = "CREATE TABLE " + TABLE_RECIPES + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "instructions TEXT NOT NULL)";

        // Table 3: Required Recipe Ingredients
        String CREATE_RECIPE_INGREDIENTS_TABLE = "CREATE TABLE " + TABLE_RECIPE_INGREDIENTS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "recipe_id INTEGER, " +
                "name TEXT NOT NULL, " +
                "quantity REAL NOT NULL, " +
                "unit TEXT NOT NULL, " +
                "FOREIGN KEY(recipe_id) REFERENCES " + TABLE_RECIPES + "(id) ON DELETE CASCADE)";

        db.execSQL(CREATE_PANTRY_TABLE);
        db.execSQL(CREATE_RECIPES_TABLE);
        db.execSQL(CREATE_RECIPE_INGREDIENTS_TABLE);

        seedInitialRecipes(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PANTRY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE_INGREDIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        onCreate(db);
    }

    // --- SEED 15 RECIPES ON FIRST LAUNCH ---
    private void seedInitialRecipes(SQLiteDatabase db) {
        insertSeededRecipe(db, "Scrambled Eggs", "1. Whisk eggs and milk.\n2. Melt butter in pan.\n3. Cook until fluffy.",
                new String[][]{{"egg", "2", "pcs"}, {"milk", "50", "ml"}, {"butter", "10", "g"}});

        insertSeededRecipe(db, "Tomato Pasta", "1. Boil pasta.\n2. Heat tomato sauce in a pan.\n3. Combine and serve.",
                new String[][]{{"pasta", "200", "g"}, {"tomato sauce", "150", "ml"}, {"garlic", "1", "clove"}});

        insertSeededRecipe(db, "Grilled Cheese Sandwich", "1. Butter bread.\n2. Place cheese between bread.\n3. Grill until golden.",
                new String[][]{{"bread", "2", "slices"}, {"cheese", "2", "slices"}, {"butter", "10", "g"}});

        insertSeededRecipe(db, "Pancakes", "1. Mix flour, milk, and egg.\n2. Pour batter on hot pan.\n3. Flip when bubbly.",
                new String[][]{{"flour", "100", "g"}, {"milk", "150", "ml"}, {"egg", "1", "pcs"}});

        insertSeededRecipe(db, "Garlic Rice", "1. Heat oil in a pan.\n2. Fry minced garlic until golden.\n3. Stir in cooked rice and salt.",
                new String[][]{{"rice", "200", "g"}, {"garlic", "2", "cloves"}, {"oil", "15", "ml"}});

        insertSeededRecipe(db, "Omelette", "1. Beat eggs with salt.\n2. Pour into oiled pan.\n3. Fold when cooked through.",
                new String[][]{{"egg", "3", "pcs"}, {"oil", "10", "ml"}});

        insertSeededRecipe(db, "Cheesy Toast", "1. Toast bread slices.\n2. Top with cheese.\n3. Melt under grill.",
                new String[][]{{"bread", "2", "slices"}, {"cheese", "1", "slice"}});

        insertSeededRecipe(db, "Oatmeal", "1. Combine oats and milk in a pot.\n2. Simmer for 5 minutes stirring continuously.",
                new String[][]{{"oats", "50", "g"}, {"milk", "200", "ml"}});

        insertSeededRecipe(db, "French Toast", "1. Whisk egg and milk.\n2. Dip bread slices.\n3. Fry in butter until browned.",
                new String[][]{{"bread", "2", "slices"}, {"egg", "1", "pcs"}, {"milk", "30", "ml"}, {"butter", "10", "g"}});

        insertSeededRecipe(db, "Boiled Eggs", "1. Boil water in a pot.\n2. Lower eggs and boil for 7 minutes.",
                new String[][]{{"egg", "2", "pcs"}});

        insertSeededRecipe(db, "Garlic Butter Pasta", "1. Boil pasta.\n2. Melt butter and sauté garlic.\n3. Toss pasta in garlic butter.",
                new String[][]{{"pasta", "150", "g"}, {"butter", "20", "g"}, {"garlic", "2", "cloves"}});

        insertSeededRecipe(db, "Rice and Beans", "1. Heat canned beans.\n2. Serve hot over steamed rice.",
                new String[][]{{"rice", "200", "g"}, {"beans", "150", "g"}});

        insertSeededRecipe(db, "Butter Toast", "1. Toast bread.\n2. Spread butter evenly while warm.",
                new String[][]{{"bread", "2", "slices"}, {"butter", "10", "g"}});

        insertSeededRecipe(db, "Cheese Omelette", "1. Beat eggs.\n2. Pour into pan and sprinkle cheese.\n3. Fold and serve.",
                new String[][]{{"egg", "2", "pcs"}, {"cheese", "1", "slice"}, {"butter", "5", "g"}});

        insertSeededRecipe(db, "Fried Egg", "1. Heat oil in pan.\n2. Crack egg into pan and cook to desire.",
                new String[][]{{"egg", "1", "pcs"}, {"oil", "5", "ml"}});
    }

    private void insertSeededRecipe(SQLiteDatabase db, String name, String instructions, String[][] ingredients) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("instructions", instructions);
        long recipeId = db.insert(TABLE_RECIPES, null, values);

        for (String[] ing : ingredients) {
            ContentValues ingValues = new ContentValues();
            ingValues.put("recipe_id", recipeId);
            ingValues.put("name", ing[0]);
            ingValues.put("quantity", Double.parseDouble(ing[1]));
            ingValues.put("unit", ing[2]);
            db.insert(TABLE_RECIPE_INGREDIENTS, null, ingValues);
        }
    }

    // --- PANTRY CRUD OPERATIONS ---
    public long addPantryItem(String name, double quantity, String unit, String expiry) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("quantity", quantity);
        values.put("unit", unit);
        values.put("expiry_date", expiry);
        return db.insert(TABLE_PANTRY, null, values);
    }

    public List<Ingredient> getAllPantryItems() {
        List<Ingredient> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PANTRY, null);
        if (cursor.moveToFirst()) {
            do {
                Ingredient item = new Ingredient(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("quantity")),
                        cursor.getString(cursor.getColumnIndexOrThrow("unit")),
                        cursor.getString(cursor.getColumnIndexOrThrow("expiry_date"))
                );
                list.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public int updatePantryItem(int id, String name, double quantity, String unit, String expiry) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("quantity", quantity);
        values.put("unit", unit);
        values.put("expiry_date", expiry);
        return db.update(TABLE_PANTRY, values, "id = ?", new String[]{String.valueOf(id)});
    }

    public void deletePantryItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PANTRY, "id = ?", new String[]{String.valueOf(id)});
    }

    // --- GET ALL RECIPES WITH INGREDIENTS ---
    public List<Recipe> getAllRecipes() {
        List<Recipe> recipeList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_RECIPES, null);

        if (cursor.moveToFirst()) {
            do {
                int recipeId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String instructions = cursor.getString(cursor.getColumnIndexOrThrow("instructions"));

                List<Ingredient> reqIngredients = getIngredientsForRecipe(db, recipeId);
                recipeList.add(new Recipe(recipeId, name, instructions, reqIngredients));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return recipeList;
    }

    private List<Ingredient> getIngredientsForRecipe(SQLiteDatabase db, int recipeId) {
        List<Ingredient> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_RECIPE_INGREDIENTS + " WHERE recipe_id = ?", new String[]{String.valueOf(recipeId)});
        if (cursor.moveToFirst()) {
            do {
                list.add(new Ingredient(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("quantity")),
                        cursor.getString(cursor.getColumnIndexOrThrow("unit")),
                        ""
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
}