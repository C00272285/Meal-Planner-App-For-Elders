package com.example.design;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

public class MenuActivity extends AppCompatActivity {
    private MenuAdapter adapter;
    private List<RecipeSearchResponse.Recipe> recipeList;
    private Spinner intoleranceSpinner;
    private EditText searchEditText;
    private Button mealPlanButtton;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_item_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recipeList = new ArrayList<>();
        adapter = new MenuAdapter(this, recipeList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        intoleranceSpinner = findViewById(R.id.spinner_dietary);
        searchEditText = findViewById(R.id.editText_search);
        mealPlanButtton = findViewById(R.id.mealPlanButton);
        setupSpinner();
        setupSearchEditText();

        // creating a button to bring the user to the mealplan screen
        mealPlanButtton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                MealPlan();
            }
        });


    }

    //the method that brings the user to the screen
    public void MealPlan()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }



    private void setupSpinner()
    {
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.dietary_options_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        intoleranceSpinner.setAdapter(spinnerAdapter);

        intoleranceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                String selectedIntolerance = parentView.getItemAtPosition(position).toString();
                loadRecipes("", "", selectedIntolerance);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView)
            {

            }
        });


    }



    private void setupSearchEditText()
    {
        searchEditText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                filterRecipes();
            }
        });
    }



    private void filterRecipes()
    {
        String query = searchEditText.getText().toString();
        String selectedDiet = intoleranceSpinner.getSelectedItem().toString();
        String dietQuery = selectedDiet.equals("No Dietary Preference") ? "" : selectedDiet;
        loadRecipes(query, dietQuery, selectedDiet);
    }



    private void loadRecipes(String query, String excludeIngredients, String intolerances)
    {
        RequestManager.getInstance().searchRecipesByIntolerance(query, excludeIngredients, intolerances, new Callback<RecipeSearchResponse>()

    {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<RecipeSearchResponse> call, @NonNull Response<RecipeSearchResponse> response)
            {
                if (response.isSuccessful() && response.body() != null)
                {
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
