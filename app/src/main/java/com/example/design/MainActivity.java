package com.example.design;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
{

    private TextView tvCalories, tvFats, tvProteins, tvFiber, tvSugars;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // reference the UI from the XML File
        EditText etFoodName = findViewById(R.id.etFoodName);
        Button btnFetchNutrition = findViewById(R.id.btnFetchNutrition);
        tvCalories = findViewById(R.id.tvCalories);
        tvFats = findViewById(R.id.tvFats);
        tvProteins = findViewById(R.id.tvProteins);
        tvFiber = findViewById(R.id.tvFiber);
        tvSugars = findViewById(R.id.tvSugars);

        RequestManager requestManager = RequestManager.getInstance();
        btnFetchNutrition.setOnClickListener(v ->
        {
            final int ingredientId = 9266;  //the id for Pineapple.
            final int amount = 1;   // the amount of the item
            final String unit = "g";    // the unit of measurement

            requestManager.getIngredientInformation(ingredientId, amount, unit, new Callback<SpoonacularIngredient>()
            {
                @Override
                public void onResponse(@NonNull Call<SpoonacularIngredient> call, @NonNull Response<SpoonacularIngredient> response)
                {
                    if (response.isSuccessful() && response.body() != null)
                    {
                        SpoonacularIngredient ingredient = response.body();
                        NutritionData nutritionData = ingredient.nutrition;
                        updateNutritionUI(nutritionData);
                    } else
                    {
                        Toast.makeText(MainActivity.this, "Failed to fetch ingredient information", Toast.LENGTH_SHORT).show();
                    }
                }
                //if there is a problem with the data getting parsed, this error message will pop up on the screen.
                @Override
                public void onFailure(@NonNull Call<SpoonacularIngredient> call, @NonNull Throwable t)
                {
                    Toast.makeText(MainActivity.this, "Failed to fetch ingredient information: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
    @SuppressLint("SetTextI18n")
    private void updateNutritionUI(NutritionData nutritionData)
    {
        if (nutritionData != null && nutritionData.getNutrients() != null)
        {
            for (Nutrient nutrient : nutritionData.getNutrients())
            {
                switch (nutrient.getName())
                {
                    //get the Nutrition information for the following.
                    case "Calories":
                        tvCalories.setText("Calories: " + nutrient.getAmount() + " " + nutrient.getUnit());
                        break;
                    case "Fat":
                        tvFats.setText("Fats: " + nutrient.getAmount() + " " + nutrient.getUnit());
                        break;
                    case "Protein":
                        tvProteins.setText("Proteins: " + nutrient.getAmount() + " " + nutrient.getUnit());
                        break;
                    case "Fiber":
                        tvFiber.setText("Fiber: " + nutrient.getAmount() + " " + nutrient.getUnit());
                        break;
                    case "Sugar":
                        tvSugars.setText("Sugars: " + nutrient.getAmount() + " " + nutrient.getUnit());
                        break;

                }
            }
        } else
        {
            //if the data is null then this toast error message will be displayed.
            Toast.makeText(this, "Nutrition data is null", Toast.LENGTH_SHORT).show();
        }
    }
}