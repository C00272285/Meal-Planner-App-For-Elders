package com.example.design;

import retrofit2.Call;
import retrofit2.Callback;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RequestManager {
    private static final String BASE_URL = "https://api.spoonacular.com/";
    private static final String API_KEY = "a68da0ca4bd7457b93c813fb3fa6043f";

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

    public void getIngredientInformation(int ingredientId, int amount, String unit, Callback<SpoonacularIngredient> callback)
    {
        Call<SpoonacularIngredient> call = apiService.getIngredientInformation(ingredientId, amount, unit, API_KEY);
        call.enqueue(callback);
    }

    public void searchRecipes(String query, String diet, String excludeIngredients, String intolerances, Callback<RecipeSearchResponse> callback)
    {
        Call<RecipeSearchResponse> call = apiService.searchRecipes(query, diet, excludeIngredients, intolerances, API_KEY);
        call.enqueue(callback);
    }

    public void searchRecipesByIntolerance(String query, String excludeIngredients, String intolerances, Callback<RecipeSearchResponse> callback)
    {
        Call<RecipeSearchResponse> call = apiService.searchRecipesByIntolerance(query, excludeIngredients, intolerances, API_KEY);
        call.enqueue(callback);
    }
    public static String getApiKey()
    {
        return API_KEY;
    }
}