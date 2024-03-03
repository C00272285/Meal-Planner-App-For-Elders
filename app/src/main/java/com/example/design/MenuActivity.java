package com.example.design;

import android.annotation.SuppressLint;
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
    private MenuAdapter adapter;
    private List<RecipeSearchResponse.Recipe> recipeList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_item_main);

        //variables to use for connecting to the XML file
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recipeList = new ArrayList<>();
        adapter = new MenuAdapter(this, recipeList, this); // Pass 'this' as RecipeListener
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        Spinner intoleranceSpinner = findViewById(R.id.spinner_dietary);

        //Setup the spinner adapter and listener
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.dietary_options_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        intoleranceSpinner.setAdapter(spinnerAdapter);
        intoleranceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        // Corrected button reference
        Button button = findViewById(R.id.mealPlanButton);
        button.setOnClickListener(v -> MealPlan());
        loadRecipes(); //A food query, left black to not search for a specific food.




    }

    public void MealPlan() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    //had to have the intent.putExtra to have a String name to be able to add meals to the MainActivity class.
    @Override
    public void onRecipeSelected(String recipeName, String mealTime)
    {
        Intent intent = new Intent(MenuActivity.this, MainActivity.class);
        intent.putExtra("RECIPE_NAME", recipeName);
        intent.putExtra("MEAL_TIME", mealTime);
        startActivity(intent);
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



}