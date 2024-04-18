package com.example.design;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Objects;
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
    private String selectedMealTime;


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
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        assert currentUser != null;
        fetchUserIntolerance();

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

    private void fetchUserIntolerance() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users")
                    .child(Objects.requireNonNull(currentUser.getEmail()).replace(".", ","));

            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String intolerance = dataSnapshot.child("Intolerance").getValue(String.class);
                    filterRecipesBasedOnIntolerance(intolerance);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(MenuActivity.this, "Failed to load intolerance.",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void filterRecipesBasedOnIntolerance(String intolerance) {
        String query = searchEditText.getText().toString().trim();
        RequestManager.getInstance().searchRecipesByIntolerance(query, "", intolerance, new Callback<RecipeSearchResponse>() {
            @Override
            public void onResponse(@NonNull Call<RecipeSearchResponse> call, @NonNull Response<RecipeSearchResponse> response) {

            }

            @Override
            public void onFailure(@NonNull Call<RecipeSearchResponse> call, @NonNull Throwable t) {

            }
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
            //  Prevents different users from getting other users logged meals popping up in there meal plan
            SharedPreferences prefs = getSharedPreferences("MealSelections", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.apply();

            FirebaseAuth.getInstance().signOut();   //  Logs the current user out allowing them to go to the login screen
            Intent logInIntent = new Intent(MenuActivity.this, Login.class); // Brings you to the Login Class
            logInIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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

    private void saveMealSelection(RecipeSearchResponse.Recipe recipe, String mealTime)
    {
        SharedPreferences prefs = getSharedPreferences("MealSelections", MODE_PRIVATE);
        String existingMeals = prefs.getString(mealTime, "");
        if (!existingMeals.isEmpty()) {
            existingMeals += "\n"; // Add a newline to separate meals
        }
        existingMeals += recipe.title; // Append the new meal

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(mealTime, existingMeals);
        editor.apply();
    }




    public void showMealTimeDialog(RecipeSearchResponse.Recipe recipe)
    {
        final String[] mealTimes = {"Breakfast", "Lunch", "Dinner"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Meal Time")
                .setItems(mealTimes, (dialog, which) -> {
                    selectedMealTime = mealTimes[which];
                    saveMealSelection(recipe, selectedMealTime);
                    Intent data = new Intent();
                    data.putExtra("RECIPE_NAME", recipe.title);
                    data.putExtra("MEAL_TIME", selectedMealTime);
                    setResult(Activity.RESULT_OK, data);
                    finish();
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}