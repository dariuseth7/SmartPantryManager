package com.pantrymanager.app.models;

import java.util.List;

public class Recipe {
    private int id;
    private String name;
    private String instructions;
    private List<Ingredient> requiredIngredients;

    public Recipe(int id, String name, String instructions, List<Ingredient> requiredIngredients) {
        this.id = id;
        this.name = name;
        this.instructions = instructions;
        this.requiredIngredients = requiredIngredients;
    }

    // Getters and Setters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getInstructions() { return instructions; }
    public List<Ingredient> getRequiredIngredients() { return requiredIngredients; }
}
