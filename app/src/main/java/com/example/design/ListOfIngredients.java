package com.example.design;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListOfIngredients {
    static class MealDetail {
        String mealName;
        List<String> ingredients;

        public MealDetail(String mealName, List<String> ingredients) {
            this.mealName = mealName;
            this.ingredients = ingredients;
        }
    }

    private final Map<String, MealDetail> meals;

    public ListOfIngredients() {
        meals = new HashMap<>();
    }

    public void addMeal(String mealType, String mealName, List<String> ingredients) {
        meals.put(mealType, new MealDetail(mealName, ingredients));
    }

    public MealDetail getMeal(String mealType) {
        return meals.get(mealType);
    }
}
