package com.example.design;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        // Start scanner
        new IntentIntegrator(this).setCaptureActivity(CaptureActivity.class).initiateScan();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null)
        {
            if (result.getContents() == null)
            {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            }
            else
            {
                // Scan was successful
                fetchProductInfo(result.getContents()); // Get the Product information
            }
        }
        else
        {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    public void fetchProductInfo(String barcode)
    {
        OpenFoodFactsApiService service = RequestOpenFoodFacts.getRetrofitInstance().create(OpenFoodFactsApiService.class);
        Call<ProductResponse> call = service.getProductByBarcode("MealPlannerForEldersApp.com", barcode);

        call.enqueue(new Callback<ProductResponse>()
        {
            @Override
            public void onResponse(@NonNull Call<ProductResponse> call, @NonNull Response<ProductResponse> response)
            {
                if (response.isSuccessful() && response.body() != null)
                {
                    ProductResponse.Product product = response.body().product;
                    Toast.makeText(getApplicationContext(), "Product: " + product.product_name, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProductResponse> call, @NonNull Throwable t)
            {
                Toast.makeText(getApplicationContext(), "Failed to fetch product details.", Toast.LENGTH_LONG).show();
            }
        });
    }
}
