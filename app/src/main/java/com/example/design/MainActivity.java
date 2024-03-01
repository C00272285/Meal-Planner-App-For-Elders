package com.example.design;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private EditText dinner, lunch, breakfast;
    private Calendar calendar;
    private DatabaseReference ref;
    private Button insert;
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

        // Initialize Calendar instance
        calendar = Calendar.getInstance();

        // Set initial text in TextView
        updateTextView();

        user = new User();
        ref = FirebaseDatabase.getInstance("https://mealplanner-a23cb-default-rtdb.europe-west1.firebasedatabase.app/").getReference().child("Meals");

        // Set click listeners for arrow buttons
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear text in EditText
                dinner.setText("");
                lunch.setText("");
                breakfast.setText("");
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                updateTextView();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear text in EditText
                dinner.setText("");
                lunch.setText("");
                breakfast.setText("");
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                updateTextView();
            }
        });

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String breakfastText = breakfast.getText().toString().trim();
                String lunchText = lunch.getText().toString().trim();
                String dinnerText = dinner.getText().toString().trim();
                String dateText = textView.getText().toString().trim();

                user.setDate(dateText);
                user.setBreakfast(breakfastText);
                user.setLunch(lunchText);
                user.setDinner(dinnerText);

                ref.push().setValue(user); // Pushing user object instead of meal

                Toast.makeText(MainActivity.this, "Data has been sent", Toast.LENGTH_LONG).show();
            }
        });
    }

    // Method to update TextView with current day
    private void updateTextView() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(calendar.getTime());
        textView.setText(formattedDate);
    }
}