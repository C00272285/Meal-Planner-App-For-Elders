package com.example.design;
import com.google.gson.annotations.SerializedName;

public class SpoonacularRecipe
{
    @SerializedName("diet")
    String diet;

    @SerializedName("includeIngredients")
    String includeIngredients;

}