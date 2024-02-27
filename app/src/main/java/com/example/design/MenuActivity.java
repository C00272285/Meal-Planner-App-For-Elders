package com.example.design;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class MenuActivity extends AppCompatActivity
{
    //variables to use for connecting to the XML file
    private RecyclerView recyclerView;
    private MenuAdapter adapter;
    private List<RecipeSearchResponse.Recipe> recipeList;
    private Spinner intoleranceSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //connecting to the data on the menu_item_main.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_item_main);
        recyclerView = findViewById(R.id.recyclerView);
        recipeList = new ArrayList<>();
        adapter = new MenuAdapter(this, recipeList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        intoleranceSpinner = findViewById(R.id.spinner_dietary);

        //this is for the spinner which allows the user to select a food based off of diet.
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.dietary_options_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        intoleranceSpinner.setAdapter(adapter);


        intoleranceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            //loads the screen with the different meals available based off of of the users intolerances.
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                String selectedDiet = parentView.getItemAtPosition(position).toString();
                if (selectedDiet.equals("No Dietary Preference"))
                {
                    // Load recipes without any dietary filter
                    loadRecipes("");
                } else {
                    // Load recipes with the selected dietary filter
                    loadRecipes(selectedDiet);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView)
            {

            }
        });


        loadRecipes(""); //a food query, left black to not search for a specific food.
    }

    private void loadRecipes(String query)
    {
        RequestManager.getInstance().searchRecipesByIntolerance(query, "", "", new Callback<RecipeSearchResponse>() //will get the meals based off of the users intolerances.
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
