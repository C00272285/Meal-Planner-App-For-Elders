package com.example.design;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class RecipeDetailResponse
{

    @SerializedName("extendedIngredients")  // get the JSON for the Ingredients
    private List<Ingredient> ingredients;

    @SerializedName("dishTypes") // get the JSON for the type of dish that the meal is
    private List<String> dishTypes;

    public List<Ingredient> getIngredients()
    {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients)
    {
        this.ingredients = ingredients;
    }

    public List<String> getDishTypes()
    {
        return dishTypes;
    }
    public void setDishTypes(List<String> dishTypes)
    {
        this.dishTypes = dishTypes;
    }


}
