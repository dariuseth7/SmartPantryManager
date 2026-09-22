package com.pantrymanager.app.utils;

import com.pantrymanager.app.models.Ingredient;
import java.util.List;

public class IngredientMatcher {

    // Normalizes ingredient names (handles case-sensitivity, extra spaces, basic singular/plural)
    public static String normalizeName(String name) {
        if (name == null) return "";
        String clean = name.trim().toLowerCase();

        // Handle common plural endings
        if (clean.endsWith("es") && clean.length() > 3) {
            return clean.substring(0, clean.length() - 2);
        } else if (clean.endsWith("s") && clean.length() > 2) {
            return clean.substring(0, clean.length() - 1);
        }
        return clean;
    }

    // Strict-Matching Algorithm:
    // Every single required ingredient must be present in the user's pantry in equal or greater quantity.
    public static boolean matchesStrictly(List<Ingredient> recipeReqs, List<Ingredient> userPantry) {
        for (Ingredient req : recipeReqs) {
            boolean reqSatisfied = false;
            String reqNorm = normalizeName(req.getName());

            for (Ingredient pantryItem : userPantry) {
                String pantryNorm = normalizeName(pantryItem.getName());

                if (reqNorm.equals(pantryNorm)) {
                    // Compare required vs available quantity
                    if (pantryItem.getQuantity() >= req.getQuantity()) {
                        reqSatisfied = true;
                        break;
                    }
                }
            }

            // If even ONE ingredient is missing or insufficient, reject the recipe
            if (!reqSatisfied) {
                return false;
            }
        }
        return true;
    }
}
