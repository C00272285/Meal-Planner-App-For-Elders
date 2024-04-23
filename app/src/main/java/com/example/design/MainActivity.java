package com.example.design;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class MainActivity extends AppCompatActivity
{
    private TextView textView, totalCaloriesText, caloriesUsedText;
    private EditText dinnerEditText, lunchEditText, breakfastEditText;
    private Calendar calendar;
    private CalorieBarView calorieBarView;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private double totalConsumedCalories = 0;
    private final ActivityResultLauncher<Intent> menuActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result ->
            {
                if (result.getResultCode() == RESULT_OK && result.getData() != null)
                {
                    String recipeName = result.getData().getStringExtra("RECIPE_NAME");
                    String mealTime = result.getData().getStringExtra("MEAL_TIME");
                    assert mealTime != null;
                    updateMealFields(recipeName, mealTime); // Update UI based on the selection
                }
            }
    );
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        initializeViews();
        updateCalorieDisplay(0);
        fetchUserData();
    }
    @SuppressLint("SetTextI18n")
    private void initializeViews()
    {
        textView = findViewById(R.id.textView);
        ImageView previousButton = findViewById(R.id.previousButton);
        ImageView nextButton = findViewById(R.id.nextButton);
        dinnerEditText = findViewById(R.id.dinner);
        lunchEditText = findViewById(R.id.lunch);
        breakfastEditText = findViewById(R.id.breakfast);
        totalCaloriesText = findViewById(R.id.totalCaloriesText);
        caloriesUsedText = findViewById(R.id.caloriesUsedText);
        calorieBarView = findViewById(R.id.calorieBarView);
        Button insertButton = findViewById(R.id.button);
        Button recipesButton = findViewById(R.id.Recipes);
        Button shoppingButton = findViewById(R.id.shoppinglist);

        calendar = Calendar.getInstance();
        updateDateTextView();
        previousButton.setOnClickListener(v -> navigateDays(-1));
        nextButton.setOnClickListener(v -> navigateDays(1));
        insertButton.setOnClickListener(v -> saveMealPlanToDatabase());
        recipesButton.setOnClickListener(v -> openRecipesView());

        Button clearMealsButton = findViewById(R.id.clearMealsButton);
        clearMealsButton.setOnClickListener(v -> clearMealEntries());

        shoppingButton.setOnClickListener(v -> {
            Intent historyIntent = new Intent(MainActivity.this, ShoppingListActivity.class);
            startActivity(historyIntent);
        });

        Button viewHistoryButton = findViewById(R.id.viewHistoryButton);
        viewHistoryButton.setOnClickListener(v -> {
            Intent historyIntent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(historyIntent);
        });
    }

    public void fetchUserData() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userKey = Objects.requireNonNull(currentUser.getEmail()).replace(".", ",");
            DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://mealplanner-a23cb-default-rtdb.europe-west1.firebasedatabase.app/")
                    .getReference("Users").child(userKey);

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        try {
                            String weightString = dataSnapshot.child("Weight").getValue(String.class);
                            String heightString = dataSnapshot.child("Height").getValue(String.class);
                            String ageString = dataSnapshot.child("Age").getValue(String.class);

                            double weight = weightString != null ? Double.parseDouble(weightString) : 0;
                            double height = heightString != null ? Double.parseDouble(heightString) : 0;
                            int age = ageString != null ? Integer.parseInt(ageString) : 0;

                            String gender = dataSnapshot.child("Gender").getValue(String.class);
                            String activityLevel = dataSnapshot.child("ActivityLevel").getValue(String.class);

                            double totalCalories = BMRCalculator.calculateBMR(gender, weight, height, age, activityLevel);
                            updateCalorieDisplay(totalCalories); // Update UI with default consumed calories as 0
                        } catch (NumberFormatException e) {
                            Toast.makeText(MainActivity.this, "Error parsing user data: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "User data not found.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(MainActivity.this, "Failed to load user data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show();
        }
    }

        @SuppressLint("SetTextI18n")
        private void updateCalorieDisplay(double totalCalories) {
            totalCaloriesText.setText("Total Calories: " + totalCalories);
            caloriesUsedText.setText(String.format(Locale.getDefault(), "Calories Used: %.2f", totalConsumedCalories));
            if (calorieBarView != null) {
                calorieBarView.setCalories(totalCalories, totalConsumedCalories);
            }
        }


    @SuppressLint("SetTextI18n")
    private void clearMealEntries()
    {
        breakfastEditText.setText("");
        lunchEditText.setText("");
        dinnerEditText.setText("");
        TextView breakfastCalories = findViewById(R.id.totalCaloriesBreakfast);
        TextView lunchCalories = findViewById(R.id.totalCaloriesLunch);
        TextView dinnerCalories = findViewById(R.id.totalCaloriesDinner);
        breakfastCalories.setText("Calories: 0");
        lunchCalories.setText("Calories: 0");
        dinnerCalories.setText("Calories: 0");
        clearMealPreferences();
    }

    private void clearMealPreferences() {
        SharedPreferences prefs = getSharedPreferences("MealSelections", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("Breakfast");
        editor.remove("Lunch");
        editor.remove("Dinner");
        editor.apply();
    }



    private void navigateDays(int days) {
        calendar.add(Calendar.DAY_OF_YEAR, days);
        updateDateTextView();
        loadMealsForSelectedDate();
    }

    private void loadMealsForSelectedDate() {
        clearMealEntries(); // Clear existing entries to prevent duplication
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String date = sdf.format(calendar.getTime());

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String encodedEmail = encodeEmail(currentUser.getEmail());
            assert encodedEmail != null;
            DatabaseReference dbRef = FirebaseDatabase.getInstance("https://mealplanner-a23cb-default-rtdb.europe-west1.firebasedatabase.app/")
                    .getReference("Users").child(encodedEmail).child("Meals").child(date);

            dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String breakfast = dataSnapshot.child("breakfast").getValue(String.class);
                        String lunch = dataSnapshot.child("lunch").getValue(String.class);
                        String dinner = dataSnapshot.child("dinner").getValue(String.class);

                        breakfastEditText.setText(breakfast != null ? breakfast : "");
                        lunchEditText.setText(lunch != null ? lunch : "");
                        dinnerEditText.setText(dinner != null ? dinner : "");
                    } else {
                        clearMealEntries();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(MainActivity.this, "Failed to load meals: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void updateDateTextView() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault());
        textView.setText(dateFormat.format(calendar.getTime()));
    }


    private void openRecipesView()
    {
        Intent intent = new Intent(MainActivity.this, MenuActivity.class);
        menuActivityResultLauncher.launch(intent);
    }


    private String encodeEmail(String email)
    {
        if (email == null)
        {
            return null;
        }
        return email.replace(".", ",");
    }


    private void saveMealPlanToDatabase() {
        String breakfast = breakfastEditText.getText().toString();
        String lunch = lunchEditText.getText().toString();
        String dinner = dinnerEditText.getText().toString();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String date = sdf.format(calendar.getTime());

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null && currentUser.getEmail() != null)
        {
            String encodedEmail = encodeEmail(currentUser.getEmail());
            DatabaseReference dbRef = FirebaseDatabase.getInstance("https://mealplanner-a23cb-default-rtdb.europe-west1.firebasedatabase.app/")
                    .getReference("Users").child(encodedEmail).child("Meals").child(date);

            Map<String, Object> mealPlanUpdates = new HashMap<>();
            mealPlanUpdates.put("breakfast", breakfast);
            mealPlanUpdates.put("lunch", lunch);
            mealPlanUpdates.put("dinner", dinner);

            dbRef.setValue(mealPlanUpdates)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(MainActivity.this, "Meal plan saved successfully.", Toast.LENGTH_SHORT).show();
                        showShareMealDialog(breakfast, lunch, dinner);
                    })
                    .addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Failed to save meal plan: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
        else
        {
            Toast.makeText(MainActivity.this, "User not signed in.", Toast.LENGTH_SHORT).show();
        }
    }
    private void showShareMealDialog(String breakfast, String lunch, String dinner)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Share Meal Plan");
        builder.setMessage("Do you want to share your meal plan?");

        builder.setPositiveButton("Share", (dialog, which) -> shareMealPlan(breakfast, lunch, dinner));
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void shareMealPlan(String breakfast, String lunch, String dinner)
    {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Here's my meal plan for today: \nBreakfast: " + breakfast + "\nLunch: " + lunch + "\nDinner: " + dinner);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Share with:"));
    }
    private void updateMealFields(String recipeName, String mealTime)
    {
        EditText targetField;
        TextView calorieField;
        switch (mealTime)
        {
            case "Breakfast":
                targetField = breakfastEditText;
                calorieField = findViewById(R.id.totalCaloriesBreakfast);
                break;
            case "Lunch":
                targetField = lunchEditText;
                calorieField = findViewById(R.id.totalCaloriesLunch);
                break;
            case "Dinner":
                targetField = dinnerEditText;
                calorieField = findViewById(R.id.totalCaloriesDinner);
                break;
            default:
                Toast.makeText(this, "Invalid meal time selected.", Toast.LENGTH_SHORT).show();
                return;
        }
        fetchCalories(recipeName, calorieField, mealTime);
        appendMeal(targetField, recipeName);
    }

    private void fetchCalories(String recipeName, TextView calorieField, String mealTime)
    {
        RequestManager.getInstance().searchRecipesByNameCustom(recipeName, new Callback<CustomRecipeSearchResponse>()
        {
            @Override
            public void onResponse(@NonNull Call<CustomRecipeSearchResponse> call, @NonNull Response<CustomRecipeSearchResponse> response)
            {
                if (response.isSuccessful() && response.body() != null && !response.body().getResults().isEmpty())
                {
                    int recipeId = response.body().getResults().get(0).getId();
                    RequestManager.getInstance().getRecipeDetails(recipeId, true, new Callback<RecipeDetailResponse>()
                    {
                        @Override
                        public void onResponse(@NonNull Call<RecipeDetailResponse> call, @NonNull Response<RecipeDetailResponse> response)
                        {
                            if (response.isSuccessful() && response.body() != null)
                            {
                                double calories = response.body().getNutrition().getNutrientAmount("Calories");
                                double currentCalories = getCurrentCalories(calorieField.getText().toString());
                                double totalCalories = currentCalories + calories;
                                totalConsumedCalories += calories;
                                runOnUiThread(() -> calorieField.setText(String.format(Locale.getDefault(), "Calories: %.2f", totalCalories)));
                                updateConsumedCaloriesDisplay();
                                totalConsumedCalories += calories;
                                updateCalorieBarView();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<RecipeDetailResponse> call, @NonNull Throwable t)
                        {
                            Toast.makeText(MainActivity.this, "Error fetching calories", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call<CustomRecipeSearchResponse> call, @NonNull Throwable t)
            {
                Toast.makeText(MainActivity.this, "Error fetching recipe details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateCalorieBarView()
    {
        double totalCalories = Double.parseDouble(totalCaloriesText.getText().toString().replace("Total Calories: ", ""));
        if (calorieBarView != null)
        {
            calorieBarView.setCalories(totalCalories, totalConsumedCalories);
        }
    }
    private void updateConsumedCaloriesDisplay()
    {
        caloriesUsedText.setText(String.format(Locale.getDefault(), "Calories Used: %.2f", totalConsumedCalories));
        updateCalorieBarView();
    }
    private double getCurrentCalories(String calorieText)
    {
        if (calorieText.startsWith("Calories: "))
        {
            try
            {
              return Double.parseDouble(calorieText.substring("Calories: ".length()).trim());
            } catch (NumberFormatException e) {
                Toast.makeText(MainActivity.this, "Error parsing current calories", Toast.LENGTH_SHORT).show();
                return 0;
            }
        }
        return 0;
    }
    private void appendMeal(EditText targetField, String recipeName)
    {
        String currentContent = targetField.getText().toString();
        if (!currentContent.isEmpty())
        {
            currentContent += "\n------------------------\n";
        }
        currentContent += recipeName;
        targetField.setText(currentContent);
    }
    private void loadMealSelections() {
        SharedPreferences prefs = getSharedPreferences("MealSelections", MODE_PRIVATE);
        String breakfast = prefs.getString("Breakfast", "");
        String lunch = prefs.getString("Lunch", "");
        String dinner = prefs.getString("Dinner", "");

        breakfastEditText.setText(breakfast);
        lunchEditText.setText(lunch);
        dinnerEditText.setText(dinner);
    }
    @Override
    protected void onResume()
    {
        super.onResume();
        loadMealSelections();
    }

}