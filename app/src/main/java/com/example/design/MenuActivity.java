package com.example.design;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuActivity extends AppCompatActivity implements MenuAdapter.RecipeListener
{
    // creating an adapter to manage the lists of Meals
    private MenuAdapter adapter;
    // creating a list to hold the fetched Meals from the API
    private List<RecipeSearchResponse.Recipe> recipeList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_item_main);

        // variables to use for connecting to the XML file
        // setting up RecyclerView to display the list of Meals
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recipeList = new ArrayList<>();
        adapter = new MenuAdapter(recipeList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.setOnMealClickListener(new MenuAdapter.OnMealClickListener() {
            @Override
            public void onMealClick(RecipeSearchResponse.Recipe recipe) {
                // Intent to start MealDetailActivity with recipe ID
                Intent detailIntent = new Intent(MenuActivity.this, MealInfo.class);
                detailIntent.putExtra("MEAL_ID", recipe.id); // Pass recipe ID
                detailIntent.putExtra("MEAL_TITLE", recipe.title); // Pass title for display
                detailIntent.putExtra("IMAGE_URL", recipe.image);
                startActivity(detailIntent);
            }
        });

        Button scanButton = findViewById(R.id.scanButton);
        scanButton.setOnClickListener(v -> {
            Intent scanIntent = new Intent(MenuActivity.this, ScanActivity.class);
            startActivity(scanIntent);
        });

        // Setting up the Spinner for selecting intolerances
        Spinner intoleranceSpinner = findViewById(R.id.spinner_dietary);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.dietary_options_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        intoleranceSpinner.setAdapter(spinnerAdapter);

        // listener for spinner item selection to load Meals based on selected intolerance
        intoleranceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedIntolerance = parentView.getItemAtPosition(position).toString();
                loadRecipesBasedOnIntolerance(selectedIntolerance);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        // listener for the Meal Plan button to navigate back to MainActivity
        Button button = findViewById(R.id.mealPlanButton);
        button.setOnClickListener(v -> MealPlan());
        loadRecipes(); // A food query, left black to not search for a specific food.
    }
    // Method to navigate back to MainActivity
    public void MealPlan() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRecipeSelected(String recipeName, String mealTime) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("RECIPE_NAME", recipeName);
        returnIntent.putExtra("MEAL_TIME", mealTime);
        setResult(Activity.RESULT_OK, returnIntent);
    }


    private void loadRecipes()
    {
        RequestManager.getInstance().searchRecipesByIntolerance("", "", "", new Callback<RecipeSearchResponse>() //will get the meals based off of the users intolerances.
        {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<RecipeSearchResponse> call, @NonNull Response<RecipeSearchResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    recipeList.clear();
                    recipeList.addAll(response.body().getResults());
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(@NonNull Call<RecipeSearchResponse> call, @NonNull Throwable t)
            {
                Toast.makeText(MenuActivity.this, "There was an error!" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    // method to load recipes based on the selected intolerance

    private void loadRecipesBasedOnIntolerance(String selectedIntolerance) {
        RequestManager.getInstance().searchRecipesByIntolerance("", "", selectedIntolerance, new Callback<RecipeSearchResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<RecipeSearchResponse> call, @NonNull Response<RecipeSearchResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    recipeList.clear();
                    recipeList.addAll(response.body().getResults());
                    adapter.notifyDataSetChanged(); // refresh the adapter with new data
                }
            }
            @Override
            public void onFailure(@NonNull Call<RecipeSearchResponse> call, @NonNull Throwable t) {
                Toast.makeText(MenuActivity.this, "Error fetching recipes: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}