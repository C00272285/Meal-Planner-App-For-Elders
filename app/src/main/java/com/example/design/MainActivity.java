package com.example.design;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements MenuAdapter.RecipeListener
{
    //variables to hold the id's from the XML files
    private TextView textView;
    private EditText dinner, lunch, breakfast;
    private Calendar calendar;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the Ui
        textView = findViewById(R.id.textView);
        ImageView previousButton = findViewById(R.id.previousButton);
        ImageView nextButton = findViewById(R.id.nextButton);
        dinner = findViewById(R.id.dinner);
        breakfast = findViewById(R.id.breakfast);
        lunch = findViewById(R.id.lunch);
        Button insert = findViewById(R.id.button);
        Button recipes = findViewById(R.id.Recipes);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        TextView loggedInUserTextView = findViewById(R.id.loggedInUserTextView);

        // Check if the user is logged in
        if (currentUser != null && currentUser.getEmail() != null) {
            loggedInUserTextView.setText("User: " + currentUser.getEmail());
        } else {
            loggedInUserTextView.setText("User: Not Logged In");
        }

        handleIntent(getIntent());  // handles the intents coming from the Main Activity


        recipes.setOnClickListener(v -> RecipesView()); // go to MenuActivity Screen when the button is clicked

        //Initialize Calendar instance and set initial text in TextView
        calendar = Calendar.getInstance();
        updateTextView();

        // Firebase initialization
        // used for the Firestore database names the database user
        new User();
        FirebaseDatabase.getInstance("https://mealplanner-a23cb-default-rtdb.europe-west1.firebasedatabase.app/").getReference().child("Users");


        previousButton.setOnClickListener(v ->
        {
            // Clear text in EditTexts and update the calendar
            clearEditTexts();
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            updateTextView();
        });

        nextButton.setOnClickListener(v -> {
            // Clear text in EditTexts and update the calendar
            clearEditTexts();
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            updateTextView();
        });

        insert.setOnClickListener(v -> insertData());
    }

    @Override
    // handles any intent when the activity is resumed
    protected void onResume()
    {
        super.onResume();
        Intent intent = getIntent();
        handleIntent(intent);
    }


    private void handleIntent(Intent intent)
    {
        // checking if the intent contains Meal information
        if (intent != null && intent.hasExtra("RECIPE_NAME") && intent.hasExtra("MEAL_TIME"))
        {
            //these two lines needed to be connected to the MenuActivity in order to send data selected from MenuActivity to the MainActivity
            String recipeName = intent.getStringExtra("RECIPE_NAME");
            String mealTime = intent.getStringExtra("MEAL_TIME");
            // updating the Meal plan with the selected meals from the Menu Activity
            assert mealTime != null;
            updateMealSlots(recipeName, mealTime);
        }
    }
    ActivityResultLauncher<Intent> menuActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result ->
            {
                // update the UI
                if (result.getResultCode() == Activity.RESULT_OK)
                {
                    Intent data = result.getData();
                    if (data != null) {
                        String recipeName = data.getStringExtra("RECIPE_NAME");
                        String mealTime = data.getStringExtra("MEAL_TIME");
                        assert mealTime != null;
                        updateMealSlots(recipeName, mealTime); // Method to update UI
                    }
                }
            });




    private void RecipesView()
    {
        // launch Menu Activity to select a meal
        Intent intent = new Intent(MainActivity.this, MenuActivity.class);
        menuActivityResultLauncher.launch(intent);
    }


    private void clearEditTexts()
    {
        // clears the fields (might remove)
        dinner.setText("");
        lunch.setText("");
        breakfast.setText("");
    }

    private void insertData() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null)
        {
            String userEmail = currentUser.getEmail();
            assert userEmail != null;
            String sanitizedEmail = userEmail.replace(".", ",");

            // Prepare the meal data
            String breakfastText = breakfast.getText().toString().trim();
            String lunchText = lunch.getText().toString().trim();
            String dinnerText = dinner.getText().toString().trim();
            String dateText = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

            // Reference to user's meals for the specific date
            DatabaseReference userMealsRef = FirebaseDatabase.getInstance("https://mealplanner-a23cb-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users").child(sanitizedEmail).child("Meals").child(dateText);

            // Creating a HashMap to store the user's meals
            Map<String, String> meals = new HashMap<>();
            meals.put("breakfast", breakfastText);
            meals.put("lunch", lunchText);
            meals.put("dinner", dinnerText);

            // Saving the meal data under the user's email and specific date
            userMealsRef.setValue(meals).addOnCompleteListener(task -> {
                if (task.isSuccessful())
                {
                    Toast.makeText(MainActivity.this, "Meal data has been saved", Toast.LENGTH_SHORT).show();
                } else
                {
                    Toast.makeText(MainActivity.this, "Failed to save meal data", Toast.LENGTH_SHORT).show();
                }
            });
        } else
        {
            Toast.makeText(MainActivity.this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }


    private void updateTextView()
    {
        // Update the TextView with the current date
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(calendar.getTime());
        textView.setText(formattedDate);
    }

    @Override
    public void onRecipeSelected(String recipeName, String mealTime)
    {
        // Updates the UI
        updateMealSlots(recipeName, mealTime);
    }
    private void updateMealSlots(String recipeName, String mealTime)
    {
        // updates the selected meal slot with the name of the meal
        switch (mealTime)
        {
            case "Breakfast":
                breakfast.setText(recipeName);
                break;
            case "Lunch":
                lunch.setText(recipeName);
                break;
            case "Dinner":
                dinner.setText(recipeName);
                break;
            default:
                Toast.makeText(this, "Invalid meal", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}