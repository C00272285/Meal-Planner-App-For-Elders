package com.example.design;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
//  To get the API for making API calls
public interface OpenFoodFactsApiService
{
    @GET("api/v0/product/{barcode}.json")
    Call<ProductResponse>
    getProductByBarcode(
    @Header("User-Agent") String userAgent,
    @Path("barcode") String barcode);
}

