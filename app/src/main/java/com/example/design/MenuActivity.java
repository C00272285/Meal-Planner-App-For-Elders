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
import com.google.firebase.auth.FirebaseAuth;
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

        Button signOutButton = findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut(); // Sign out from Firebase
            Intent intent = new Intent(MenuActivity.this, Login.class); // Assuming you have a LoginActivity
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the activity stack
            startActivity(intent);
            finish(); // Finish the current activity
        });


        adapter = new MenuAdapter(this, recipeList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

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

    //had to have the intent.putExtra to have a String name to be able to add meals to the MainActivity class.
    @Override
    public void onRecipeSelected(String recipeName, String mealTime) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("RECIPE_NAME", recipeName);
        returnIntent.putExtra("MEAL_TIME", mealTime);
        setResult(Activity.RESULT_OK, returnIntent);
        finish(); // This will close MenuActivity and return to MainActivity
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