package com.example.design;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RequestOpenFoodFacts
{

    private static Retrofit retrofit;
    private static final String BASE_URL = "https://world.openfoodfacts.org/";  // The URL for the API (Open Food Facts)

    public static Retrofit getRetrofitInstance()
    {
        if (retrofit == null)
        {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
