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

        TextView textViewInstructions = findViewById(R.id.instructions);
        TextView ingredientsTextView = findViewById(R.id.ingredients);
        TextView typeTextView = findViewById(R.id.type);
        ImageView imageViewMeal = findViewById(R.id.image);
        Button returnButton = findViewById(R.id.toMenuButton);
        TextView mealTitleTextView = findViewById(R.id.title);

        // Button to go back to the menu
        returnButton.setOnClickListener(v -> {
            Intent intent = new Intent(MealInfo.this, MenuActivity.class);
            startActivity(intent);
            finish();
        });

        // Retrieve the passed recipe ID and image URL from the intent
        String mealTitle = getIntent().getStringExtra("MEAL_TITLE");
        int recipeId = getIntent().getIntExtra("MEAL_ID", -1);
        String imageUrl = getIntent().getStringExtra("IMAGE_URL");
        Picasso.get().load(imageUrl).into(imageViewMeal);
        mealTitleTextView.setText(mealTitle);

        // Fetch the recipe details including ingredients and type
        fetchRecipeDetails(recipeId, ingredientsTextView, typeTextView);

        // Display the cooking instructions
        fetchCookingInstructions(recipeId, textViewInstructions);
    }

    private void fetchRecipeDetails(int recipeId, TextView ingredientsTextView, TextView typeTextView)
    {
        RequestManager.getInstance().getRecipeDetails(recipeId, true, new Callback<RecipeDetailResponse>()
        {
            @Override
            public void onResponse(@NonNull Call<RecipeDetailResponse> call, @NonNull Response<RecipeDetailResponse> response)
            {
                //Log.d("MealInfo", "API Response: " + new Gson().toJson(response.body()));
                if (response.isSuccessful() && response.body() != null)
                {
                    RecipeDetailResponse recipeDetails = response.body();

                    StringBuilder ingredientsBuilder = getStringBuilder(recipeDetails);

                    ingredientsTextView.setText(ingredientsBuilder.toString());
                    StringBuilder dishTypesBuilder = new StringBuilder("Meal Type:\n");
                    if (recipeDetails.getDishTypes() != null) { // Null check for dishTypes
                        for (String dishType : recipeDetails.getDishTypes())
                        {
                            dishTypesBuilder.append("- ").append(dishType).append("\n");
                        }
                    }
                    else
                    {
                        dishTypesBuilder.append("No meal type available.\n");
                    }

                    typeTextView.setText(dishTypesBuilder.toString());
                }
                else
                {
                    Toast.makeText(MealInfo.this, "Failed to fetch recipe details.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<RecipeDetailResponse> call, @NonNull Throwable t)
            {
                Toast.makeText(MealInfo.this, "An error occurred: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @NonNull
    private static StringBuilder getStringBuilder(RecipeDetailResponse recipeDetails) {
        StringBuilder ingredientsBuilder = new StringBuilder("Ingredients:\n");
        if (recipeDetails.getIngredients() != null) { // Null check for ingredients
            for (Ingredient ingredient : recipeDetails.getIngredients())
            {
                ingredientsBuilder.append("- ")
                        .append(ingredient.getName())
                        .append(": ")
                        .append(ingredient.getAmount())
                        .append(" ")
                        .append(ingredient.getUnit())
                        .append("\n");
            }

        }
        else
        {
            ingredientsBuilder.append("No ingredients available.\n");
        }
        return ingredientsBuilder;
    }






    private void fetchCookingInstructions(int recipeId, TextView textViewInstructions)
    {
        RequestManager.getInstance().getAnalyzedRecipeInstructions(recipeId, true, new Callback<List<AnalyzedInstruction>>()
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
                    textViewInstructions.setText(instructionsBuilder.toString());
                }
                else
                {
                    Toast.makeText(MealInfo.this, "Failed to load cooking instructions.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<AnalyzedInstruction>> call, @NonNull Throwable t)
            {
                Toast.makeText(MealInfo.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
