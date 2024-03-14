package com.example.design;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MealInfo extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_info);

        TextView textViewInstructions = findViewById(R.id.mealInstructionsTextView);
        ImageView imageViewMeal = findViewById(R.id.mealImageView);
        // Button to go back to the Menu Screen
        Button returnButton = findViewById(R.id.toMenuButton);
        returnButton.setOnClickListener(v -> {
            Intent intent = new Intent(MealInfo.this, MenuActivity.class);
            startActivity(intent);
            finish();
        });

        // The id and URL for displaying the cooking instructions and Image.
        int recipeId = getIntent().getIntExtra("MEAL_ID", -1);
        String imageUrl = getIntent().getStringExtra("IMAGE_URL");
        boolean stepBreakdown = true;

        // Load the image
        Picasso.get().load(imageUrl).into(imageViewMeal);

        RequestManager.getInstance().getAnalyzedRecipeInstructions(recipeId, stepBreakdown, new Callback<List<AnalyzedInstruction>>()
        {
            @Override
            public void onResponse(@NonNull Call<List<AnalyzedInstruction>> call, @NonNull Response<List<AnalyzedInstruction>> response)
            {
                if (response.isSuccessful() && response.body() != null)
                {
                    StringBuilder instructionsBuilder = new StringBuilder();
                    for (AnalyzedInstruction instruction : response.body())
                    {
                        for (AnalyzedInstruction.Step step : instruction.getSteps())
                        {
                            instructionsBuilder.append(step.getNumber()).append(". ").append(step.getStep()).append("\n\n");
                        }
                    }
                    // Update the screen with the cooking instructions
                    runOnUiThread(() -> textViewInstructions.setText(instructionsBuilder.toString()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<AnalyzedInstruction>> call, @NonNull Throwable t)
            {
                // Error message if instructions don't load
                runOnUiThread(() -> Toast.makeText(MealInfo.this, "Failed to load instructions.", Toast.LENGTH_LONG).show());
            }


        });
    }


}
