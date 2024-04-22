package com.example.design;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
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

    @GET("recipes/{id}/analyzedInstructions")
    Call<List<AnalyzedInstruction>> getAnalyzedRecipeInstructions
            (
            @Path("id") int recipeId,
            @Query("apiKey") String apiKey,
            @Query("stepBreakdown") boolean stepBreakdown);

    @GET("recipes/{id}/information")
    Call<RecipeDetailResponse> getRecipeDetails(
            @Path("id") int recipeId,
            @Query("includeNutrition") boolean includeNutrition,
            @Query("apiKey") String apiKey);

    @GET("recipes/complexSearch")
    Call<RecipeSearchResponse> searchRecipes(
            @Query("query") String query,
            @Query("cuisine") String cuisine,
            @Query("intolerances") String intolerances,
            @Query("apiKey") String apiKey
    );

    @POST("mealplanner/{username}/shopping-list/items")
    Call<Void> addToShoppingList(
            @Path("username") String username,
            @Query("hash") String hash,
            @Body ShoppingListItem item);

    @GET("recipes/{id}/information")
    Call<ShoppingListRecipeDetail> getRecipeDetailsForShoppingList(
            @Path("id") int recipeId,
            @Query("includeNutrition") boolean includeNutrition,
            @Query("apiKey") String apiKey);

    @GET("recipes/complexSearch")
    Call<CustomRecipeSearchResponse> searchRecipesCustom(
            @Query("query") String query,
            @Query("cuisine") String cuisine,
            @Query("excludeIngredients") String excludeIngredients,
            @Query("intolerances") String intolerances,
            @Query("apiKey") String apiKey);


    @GET("recipes/{id}/priceBreakdownWidget.json")
    Call<PriceBreakdownResponse> getPriceBreakdown(
            @Path("id") int recipeId,
            @Query("apiKey") String apiKey);

}