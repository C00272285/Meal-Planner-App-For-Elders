package com.example.design;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class RetrofitClientInstance
{
    // This class will get the Base URL for the Open Food Facts API allowing for API calls
    private static Retrofit retrofit;
    private static final String BASE_URL = "https://world.openfoodfacts.org/";

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
