package com.example.design;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SpoonacularApiService {

    //the GET for the ingredient information
    @GET("food/ingredients/{id}/information")
    Call<SpoonacularIngredient> getIngredientInformation(
            @Path("id") int ingredientId,
            @Query("amount") int amount,
            @Query("unit") String unit,
            @Query("apiKey") String apiKey
    );

    //the GET for the Search Recipes
    @GET("recipes/complexSearch")
    Call<RecipeSearchResponse> searchRecipes(
            @Query("query") String query,
            @Query("diet") String diet,
            @Query("excludeIngredients") String excludeIngredients,
            @Query("intolerances") String intolerances,
            @Query("apiKey") String apiKey
    );

    @GET("recipes/complexSearch")
    Call<RecipeSearchResponse>searchRecipesByIntolerance(
            @Query("query") String query,
            @Query("excludeIngredients") String excludeIngredients,
            @Query("intolerances") String intolerances,
            @Query("apiKey") String apiKey
    );
}
