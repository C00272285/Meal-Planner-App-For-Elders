package com.example.design;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class RecipeSearchResponse {
    @SerializedName("results")
    List<Recipe> results;

    public static class Recipe {
        @SerializedName("id")
        int id;

        @SerializedName("title")
        String title;

        @SerializedName("image")
        String image;

        @SerializedName("servings")
        int servingSize; // Example field for serving size

        @SerializedName("readyInMinutes")
        int cookingTime; // Example field for cooking time

        // Getters
        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getImage() {
            return image;
        }

        public int getServingSize() {
            return servingSize;
        }

        public int getCookingTime() {
            return cookingTime;
        }
    }

    public List<Recipe> getResults() {
        return results;
    }

    public void setResults(List<Recipe> results) {
        this.results = results;
    }
}
