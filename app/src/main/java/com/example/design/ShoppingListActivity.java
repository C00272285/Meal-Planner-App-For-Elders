package com.example.design;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShoppingListActivity extends AppCompatActivity
{
    private LinearLayout breakfastIngredientsLayout, lunchIngredientsLayout, dinnerIngredientsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingredient_list);
        breakfastIngredientsLayout = findViewById(R.id.breakfastIngredientsLayout);
        lunchIngredientsLayout = findViewById(R.id.lunchIngredientsLayout);
        dinnerIngredientsLayout = findViewById(R.id.dinnerIngredientsLayout);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null)
        {
            String encodedEmail = Objects.requireNonNull(currentUser.getEmail()).replace(".", ",");
            fetchMeals(encodedEmail);
        }
        else
        {
            Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateIngredientViews(LinearLayout layout, List<ShoppingListRecipeDetail.ExtendedIngredient> ingredients)
    {
        layout.removeAllViews();
        double total = 0;
        for (ShoppingListRecipeDetail.ExtendedIngredient ingredient : ingredients)
        {
            TextView ingredientView = new TextView(this);
            ingredientView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            double price = ingredient.getAmount() * ingredient.getPrice();
            total += price;
            ingredientView.setText(String.format(Locale.getDefault(), "- %s, %.2f %s, $%.2f", ingredient.getName(), ingredient.getAmount(), ingredient.getUnit(), price));layout.addView(ingredientView);
        }

        TextView totalView = new TextView(this);
        totalView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        totalView.setText(String.format(Locale.getDefault(), "Total: $%.2f", total));totalView.setTextColor(Color.GREEN);layout.addView(totalView);
    }

    private void fetchMeals(String userEmail)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String today = sdf.format(new Date());

        DatabaseReference dbRef = FirebaseDatabase.getInstance("https://mealplanner-a23cb-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users").child(userEmail).child("Meals").child(today);
        dbRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    updateMeals(dataSnapshot, "breakfast", breakfastIngredientsLayout);
                    updateMeals(dataSnapshot, "lunch", lunchIngredientsLayout);
                    updateMeals(dataSnapshot, "dinner", dinnerIngredientsLayout);
                }
                else
                {
                    Toast.makeText(ShoppingListActivity.this, "No meals found for today.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Toast.makeText(ShoppingListActivity.this, "Failed to load meals: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void updateMeals(DataSnapshot dataSnapshot, String mealType, LinearLayout layout)
    {
        String meal = dataSnapshot.child(mealType).getValue(String.class);
        if (meal != null && !meal.isEmpty())
        {
            fetchRecipeDetails(meal, layout);
        }
        else
        {
            TextView noMealText = new TextView(this);
            noMealText.setText("No " + mealType + " found.");
            layout.addView(noMealText);
        }
    }

    private void fetchRecipeDetails(String mealName, LinearLayout layout)
    {
        RequestManager.getInstance().searchRecipesByNameCustom(mealName, new Callback<CustomRecipeSearchResponse>()
        {
            @Override
            public void onResponse(@NonNull Call<CustomRecipeSearchResponse> call, @NonNull Response<CustomRecipeSearchResponse> response)
            {
                if (response.isSuccessful() && response.body() != null && !response.body().getResults().isEmpty())
                {
                    int recipeId = response.body().getResults().get(0).getId();
                    RequestManager.getInstance().getRecipeDetailsForShoppingList(recipeId, true, new Callback<ShoppingListRecipeDetail>()
                    {
                        @Override
                        public void onResponse(@NonNull Call<ShoppingListRecipeDetail> call, @NonNull Response<ShoppingListRecipeDetail> response)
                        {
                            if (response.isSuccessful() && response.body() != null)
                            {
                                Log.d("API", "Raw JSON response: " + new Gson().toJson(response.body()));

                                updateIngredientViews(layout, response.body().getExtendedIngredients());
                            }
                            else
                            {
                                updateTextViewWithError(layout, "Details not found for " + mealName);
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<ShoppingListRecipeDetail> call, @NonNull Throwable t)
                        {
                            updateTextViewWithError(layout, "Error fetching recipe details: " + t.getMessage());
                        }
                    });
                }
                else
                {
                    Log.e("API", "No results found or error in response");
                    updateTextViewWithError(layout, "Recipe not found: " + mealName);
                }
            }

            @Override
            public void onFailure(@NonNull Call<CustomRecipeSearchResponse> call, @NonNull Throwable t)
            {
                Log.e("API", "Error searching for recipe", t);
                updateTextViewWithError(layout, "Error searching for recipe: " + t.getMessage());
            }
        });
    }

    private void updateTextViewWithError(LinearLayout layout, String errorMessage)
    {
        layout.removeAllViews();
        TextView errorView = new TextView(this);
        errorView.setText(errorMessage);
        layout.addView(errorView);
    }
}