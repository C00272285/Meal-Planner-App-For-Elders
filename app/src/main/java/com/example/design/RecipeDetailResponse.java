package com.example.design;
import com.google.gson.annotations.SerializedName;
import java.util.List;
public class RecipeDetailResponse
{
    @SerializedName("servings")
    private int servings;

    @SerializedName("readyInMinutes")
    private int readyInMinutes;
    @SerializedName("extendedIngredients")  // get the JSON for the Ingredients
    private final List<Ingredient> ingredients;

    @SerializedName("nutrition")
    Nutrition nutrition;

    @SerializedName("dishTypes") // get the JSON for the type of dish that the meal is
    private final List<String> dishTypes;

    public RecipeDetailResponse(List<Ingredient> ingredients, List<String> dishTypes) {
        this.ingredients = ingredients;
        this.dishTypes = dishTypes;
    }

    public List<Ingredient> getIngredients()
    {
        return ingredients;
    }

    public List<String> getDishTypes()
    {
        return dishTypes;
    }

    public static class Nutrition
    {
        @SerializedName("nutrients")
        List<Nutrient> nutrients;
        public static class Nutrient
        {
            @SerializedName("name")
            String title;
            @SerializedName("amount")
            double amount;
            @SerializedName("unit")
            String unit;


            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public double getAmount() {
                return amount;
            }

            public void setAmount(double amount) {
                this.amount = amount;
            }

            public String getUnit() {
                return unit;
            }

            public void setUnit(String unit) {
                this.unit = unit;
            }
        }
        public double getNutrientAmount(String nutrientName)
        {
            if (nutrients != null)
            {
                for (Nutrient nutrient : nutrients)
                {
                    if (nutrientName.equals(nutrient.getTitle()))
                    {
                        return nutrient.getAmount();
                    }
                }
            }
            return 0;
        }
    }


    public Nutrition getNutrition()
    {
        return nutrition;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public int getReadyInMinutes() {
        return readyInMinutes;
    }

    public void setReadyInMinutes(int readyInMinutes) {
        this.readyInMinutes = readyInMinutes;
    }
}
