package com.example.design;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements MenuAdapter.RecipeListener {
    private TextView textView;
    private EditText dinnerEditText, lunchEditText, breakfastEditText;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();

    }

    @SuppressLint("SetTextI18n")
    private void initializeViews() {
        textView = findViewById(R.id.textView);
        ImageView previousButton = findViewById(R.id.previousButton);
        ImageView nextButton = findViewById(R.id.nextButton);
        dinnerEditText = findViewById(R.id.dinner);
        breakfastEditText = findViewById(R.id.breakfast);
        lunchEditText = findViewById(R.id.lunch);
        Button insertButton = findViewById(R.id.button);
        Button recipesButton = findViewById(R.id.Recipes);
        TextView loggedInUserTextView = findViewById(R.id.loggedInUserTextView);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null && currentUser.getEmail() != null) {
            loggedInUserTextView.setText("User: " + currentUser.getEmail());
        } else {
            loggedInUserTextView.setText("User: Not Logged In");
        }

        calendar = Calendar.getInstance();
        updateDateTextView();

        previousButton.setOnClickListener(v -> navigateDays(-1));
        nextButton.setOnClickListener(v -> navigateDays(1));
        insertButton.setOnClickListener(v -> saveMealPlanToDatabase());
        recipesButton.setOnClickListener(v -> openRecipesView());
    }

    private String encodeEmail(String email) {
        return email.replace(".", ",");
    }


    private void navigateDays(int days) {
        calendar.add(Calendar.DAY_OF_YEAR, days);
        updateDateTextView();
    }

    private void updateDateTextView() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault());
        textView.setText(dateFormat.format(calendar.getTime()));
    }

    private void openRecipesView() {
        Intent intent = new Intent(MainActivity.this, MenuActivity.class);
        startActivity(intent);
    }

    private void saveMealPlanToDatabase() {
        String breakfast = breakfastEditText.getText().toString();
        String lunch = lunchEditText.getText().toString();
        String dinner = dinnerEditText.getText().toString();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String date = sdf.format(calendar.getTime());

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null && currentUser.getEmail() != null) {
            String encodedEmail = encodeEmail(currentUser.getEmail());

            DatabaseReference dbRef = FirebaseDatabase.getInstance("https://mealplanner-a23cb-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users").child(encodedEmail);

            Map<String, Object> mealPlan = new HashMap<>();
            mealPlan.put("Meals/" + date + "/breakfast", breakfast);
            mealPlan.put("Meals/" + date + "/lunch", lunch);
            mealPlan.put("Meals/" + date + "/dinner", dinner);

            dbRef.updateChildren(mealPlan).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Meal plan saved successfully.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Failed to save meal plan.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(MainActivity.this, "User not signed in.", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onRecipeSelected(String recipeName, String mealTime) {
        // Update the appropriate meal slot based on the selection
        switch (mealTime) {
            case "Breakfast":
                breakfastEditText.setText(recipeName);
                break;
            case "Lunch":
                lunchEditText.setText(recipeName);
                break;
            case "Dinner":
                dinnerEditText.setText(recipeName);
                break;
            default:
                Toast.makeText(this, "Invalid meal time selected.", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
