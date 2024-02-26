package com.example.design;

import android.annotation.SuppressLint;
import android.os.Bundle;
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

        loadRecipes("pasta"); //an food query
    }

    private void loadRecipes(String query)
    {
        RequestManager.getInstance().searchRecipes(query, "vegetarian", "", "", new Callback<RecipeSearchResponse>() //will get the meals based off of the diet which in this case is vegetarian.
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
