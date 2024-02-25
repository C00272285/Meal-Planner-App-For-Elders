package com.example.design;


import retrofit2.Call;
import retrofit2.Callback;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RequestManager
{
    private static final String BASE_URL = "https://api.spoonacular.com/";  //the base URL that connects to the API
    private static final String API_KEY = "a68da0ca4bd7457b93c813fb3fa6043f"; //my API key that allows me to parse the information from the SPI to my android app.

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

    //getting the food information
    public void getIngredientInformation(int ingredientId, int amount, String unit, Callback<SpoonacularIngredient> callback)
    {
        Call<SpoonacularIngredient> call = apiService.getIngredientInformation(ingredientId, amount, unit, API_KEY);
        call.enqueue(callback);
    }
}
