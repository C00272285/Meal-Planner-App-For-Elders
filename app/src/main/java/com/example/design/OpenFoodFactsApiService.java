package com.example.design;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface OpenFoodFactsApiService
{
    // Connects to the Open Foods Facts API endpoint
    @GET("api/v0/product/{barcode}.json")
    Call<ProductResponse> getProductByBarcode(
            @Header("User-Agent") String userAgent,
            @Path("barcode") String barcode
    );

}
