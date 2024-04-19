package com.example.design;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class RecipeSearchResponse
{

    @SerializedName("results")
    List<Recipe> results;

    public static class Recipe
    {
        @SerializedName("id")
        int id;

        @SerializedName("title")
        String title;


        @SerializedName("image")
        String image;

        @SerializedName("readyInMinutes")
        int readyInMinutes;

        @SerializedName("servings")
        int servings;

    }


    public List<Recipe> getResults()
    {
        return results;
    }

}