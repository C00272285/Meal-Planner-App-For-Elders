package com.example.design;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RequestManager {
    private static final String BASE_URL = "https://api.spoonacular.com/";
    private static final String API_KEY = "aeeac25e08f34e77a4d2a6505dfcffb3";

    private static RequestManager instance;
    private final SpoonacularApiService apiService;

    private RequestManager()
    {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        apiService = retrofit.create(SpoonacularApiService.class);
    }

    public static RequestManager getInstance()
    {
        if (instance == null) {
            instance = new RequestManager();
        }
        return instance;
    }

    public void searchRecipesByIntolerance(String query, String excludeIngredients, String intolerances, Callback<RecipeSearchResponse> callback)
    {
        Call<RecipeSearchResponse> call = apiService.searchRecipesByIntolerance(query, excludeIngredients, intolerances, API_KEY);
        call.enqueue(callback);
    }

    public void getAnalyzedRecipeInstructions(int recipeId, boolean stepBreakdown, Callback<List<AnalyzedInstruction>> callback) {
        Call<List<AnalyzedInstruction>> call = apiService.getAnalyzedRecipeInstructions(recipeId, API_KEY, stepBreakdown);
        call.enqueue(callback);
    }

    public void getRecipeDetails(int recipeId, boolean includeNutrition, Callback<RecipeDetailResponse> callback)
    {
        Call<RecipeDetailResponse> call = apiService.getRecipeDetails(recipeId, includeNutrition, API_KEY);
        call.enqueue(callback);
    }

    public void searchRecipes(String query, String cuisine, String intolerances, Callback<RecipeSearchResponse> callback) {
        Call<RecipeSearchResponse> call = apiService.searchRecipes(query, cuisine, "", intolerances, API_KEY);
        call.enqueue(callback);
    }

    public void addToShoppingList(String username, String hash, ShoppingListItem item, Callback<Void> callback) {
        Call<Void> call = apiService.addToShoppingList(username, hash, item);
        call.enqueue(callback);
    }

    public void searchRecipesByNameCustom(String query, Callback<CustomRecipeSearchResponse> callback) {
        Call<CustomRecipeSearchResponse> call = apiService.searchRecipesCustom(query, "", "", "", API_KEY);
        call.enqueue(callback);
    }

    public void getRecipeDetailsForShoppingList(int recipeId, boolean includeNutrition, Callback<ShoppingListRecipeDetail> callback) {
        Call<ShoppingListRecipeDetail> call = apiService.getRecipeDetailsForShoppingList(recipeId, includeNutrition, API_KEY);
        call.enqueue(callback);
    }

    public void getPriceBreakdown(int recipeId, Callback<PriceBreakdownResponse> callback) {
        Call<PriceBreakdownResponse> call = apiService.getPriceBreakdown(recipeId, API_KEY);
        call.enqueue(callback);
    }



}