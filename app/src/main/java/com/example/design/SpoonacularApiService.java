package com.example.design;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SpoonacularApiService
{
    @GET("food/ingredients/{id}/information")   //the endpoint to get the data
    Call<SpoonacularIngredient> getIngredientInformation(
            //these need to match what is on the Spoonacular API documentation in order to correctly connect to the API and get data from it.
            @Path("id") int ingredientId,   //the id of the food to display the information of that food
            @Query("amount") int amount,    //amount of the item
            @Query("unit") String unit,     //unit of measurement of the item
            @Query("apiKey") String apiKey  //my API key from the Spoonacular API website
    );
}
