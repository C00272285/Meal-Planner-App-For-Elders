package com.example.design;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private EditText dinner, lunch, breakfast;
    private Calendar calendar;
    private DatabaseReference ref;
    private Button insert, Recipes;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        textView = findViewById(R.id.textView);
        ImageView previousButton = findViewById(R.id.previousButton);
        ImageView nextButton = findViewById(R.id.nextButton);
        dinner = findViewById(R.id.dinner);
        breakfast = findViewById(R.id.breakfast);
        lunch = findViewById(R.id.lunch);
        insert = findViewById(R.id.button);
        Recipes = findViewById(R.id.Recipes);


        Recipes.setOnClickListener(v -> RecipesView());

        // Initialize Calendar instance and set initial text in TextView
        calendar = Calendar.getInstance();
        updateTextView();

        // Firebase initialization
        user = new User();
        ref = FirebaseDatabase.getInstance("https://mealplanner-a23cb-default-rtdb.europe-west1.firebasedatabase.app/").getReference().child("Meals");

        // Previous and Next button listeners
        previousButton.setOnClickListener(v -> {
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

        insert.setOnClickListener(v -> {

            insertData();
        });
    }

    private void RecipesView() {
        Intent intent = new Intent(MainActivity.this, MenuActivity.class);
        startActivity(intent);
    }

    private void clearEditTexts() {
        dinner.setText("");
        lunch.setText("");
        breakfast.setText("");
    }

    private void insertData() {
        // Code to insert data to Firebase
        String breakfastText = breakfast.getText().toString().trim();
        String lunchText = lunch.getText().toString().trim();
        String dinnerText = dinner.getText().toString().trim();
        String dateText = textView.getText().toString().trim();

        user.setDate(dateText);
        user.setBreakfast(breakfastText);
        user.setLunch(lunchText);
        user.setDinner(dinnerText);

        ref.push().setValue(user);
        Toast.makeText(MainActivity.this, "Data has been sent", Toast.LENGTH_LONG).show();
    }

    private void updateTextView() {
        // Update the TextView with the current date
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(calendar.getTime());
        textView.setText(formattedDate);
    }
}
