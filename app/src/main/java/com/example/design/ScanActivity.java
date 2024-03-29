package com.example.design;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
//  This code is used to start the Barcode scanner
public class ScanActivity extends AppCompatActivity
{
    // Start the Barcode Scanner
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        new IntentIntegrator(this).initiateScan();
    }
    // Handles the result of the barcode scan (If it was a success or not)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null && result.getContents() != null)
        {
            fetchProductInfo(result.getContents());
        }
        else
        {
            Toast.makeText(this, "Scan was cancelled.", Toast.LENGTH_LONG).show();
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    //   Getting the product information
    private void fetchProductInfo(String barcode)
    {
        // A Service is created for API Calls
        OpenFoodFactsApiService service = RetrofitClientInstance.getRetrofitInstance().create(OpenFoodFactsApiService.class);
        // Need User Agent because getProductByBarcode needs to have a barcode and UserAgent to work
        String userAgent = "Meal Planner For Elders App";
        //  Make the API call using the Scanned Barcode
        Call<ProductResponse> call = service.getProductByBarcode(userAgent, barcode);
        //  Handles the Response from the API call
        call.enqueue(new Callback<ProductResponse>()
        {
            @Override
            public void onResponse(@NonNull Call<ProductResponse> call, @NonNull Response<ProductResponse> response)
            {
                if (response.isSuccessful() && response.body() != null && response.body().product != null)
                {
                    Intent intent = new Intent(ScanActivity.this, ProductDetailActivity.class);
                    ProductResponse.Product product = response.body().product;
                    intent.putExtra("productName", product.getProductName());
                    intent.putExtra("brand", product.getBrands());

                    ProductResponse.Nutriments nutriments = product.getNutriments();
                    if (nutriments != null)
                    {
                        intent.putExtra("energy", nutriments.getEnergy());
                        intent.putExtra("protein", nutriments.getProtein());
                        intent.putExtra("sugars", nutriments.getSugars());
                        intent.putExtra("fat", nutriments.getFat());
                        intent.putExtra("fiber", nutriments.getFiber());
                    }

                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(ScanActivity.this, "Product not found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProductResponse> call, @NonNull Throwable t) {
                Toast.makeText(ScanActivity.this, "Error fetching product details.", Toast.LENGTH_LONG).show();
            }
        });
    }
}
