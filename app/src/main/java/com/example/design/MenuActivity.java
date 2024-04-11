package com.example.design;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class MenuActivity extends AppCompatActivity implements MenuAdapter.OnMealClickListener
{
    private MenuAdapter adapter;
    private final ArrayList<RecipeSearchResponse.Recipe> recipeList = new ArrayList<>();
    private Spinner intoleranceSpinner;
    private EditText searchEditText;
    private Button scanButton, logoutButton, mealPlanButton;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_item_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        intoleranceSpinner = findViewById(R.id.spinner_dietary);
        searchEditText = findViewById(R.id.editText_search);
        scanButton = findViewById(R.id.scanButton);
        logoutButton = findViewById(R.id.signOutButton);
        mealPlanButton = findViewById(R.id.mealPlanButton);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MenuAdapter(this, recipeList, this);
        recyclerView.setAdapter(adapter);

        setupSpinner();
        setupSearchEditText();
        setupButtons();
        loadRecipes("");
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
                filterRecipes();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });
    }

    private void setupSearchEditText()
    {
        // This updated code will now only return searched items upon clicking enter (Saves api points)
        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE ||
                    (event != null && event.getAction() == KeyEvent.ACTION_DOWN &&
                            event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                filterRecipes();
                return true;
            }
            return false;
        });
    }


    private void setupButtons()
    {
        scanButton.setOnClickListener(v -> {
            Intent ScanIntent = new Intent(MenuActivity.this, ScanActivity.class); // Brings you to the Scan Activity class
            startActivity(ScanIntent);
            finish();
        });

        logoutButton.setOnClickListener(v -> {
            Intent logInIntent = new Intent(MenuActivity.this, Login.class); // Brings you to the Login Class
            startActivity(logInIntent);
            finish();
        });

        mealPlanButton.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, MainActivity.class); // Brings you to the Main Activity class (Meal Plan Class)
            startActivity(intent);
        });
    }

    private void filterRecipes()
    {
        String query = searchEditText.getText().toString().trim();
        loadRecipes(query);
    }

    private void loadRecipes(String query)
    {
        RequestManager.getInstance().searchRecipesByIntolerance(query, "", "", new Callback<RecipeSearchResponse>()
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
                } else {
                    Toast.makeText(MenuActivity.this, "Error fetching recipes!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<RecipeSearchResponse> call, @NonNull Throwable t)
            {
                Toast.makeText(MenuActivity.this, "There was an error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMealClick(RecipeSearchResponse.Recipe recipe)
    {
        Intent detailIntent = new Intent(MenuActivity.this, MealInfo.class);
        detailIntent.putExtra("MEAL_ID", recipe.id);
        detailIntent.putExtra("MEAL_TITLE", recipe.title);
        detailIntent.putExtra("IMAGE_URL", recipe.image);
        startActivity(detailIntent);
    }

    public void showMealTimeDialog(RecipeSearchResponse.Recipe recipe)
    {
        final String[] mealTimes = {"Breakfast", "Lunch", "Dinner"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Meal Time")
                .setItems(mealTimes, (dialog, which) -> {
                    String mealTime = mealTimes[which];

                    Intent data = new Intent();
                    data.putExtra("RECIPE_NAME", recipe.title);
                    data.putExtra("MEAL_TIME", mealTime);
                    setResult(Activity.RESULT_OK, data);
                    finish();
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
