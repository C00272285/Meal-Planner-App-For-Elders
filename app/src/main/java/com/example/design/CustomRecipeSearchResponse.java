package com.example.design;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CustomRecipeSearchResponse {
    @SerializedName("results")
    private List<RecipeResult> results;

    public List<RecipeResult> getResults() {
        return results;
    }

    public static class RecipeResult {
        @SerializedName("id")
        private int id;

        public int getId() {
            return id;
        }
    }
}
