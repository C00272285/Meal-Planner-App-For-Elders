package com.example.design;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Objects;

public class Register extends AppCompatActivity {

    private EditText EmailAddress, Password, Height, Weight, Age;
    private Spinner intoleranceSpinner, GenderSpinner;
    private ProgressBar ProgressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        // Initialize FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase.getInstance().getReference("users");

        // Initialize UI components
        EmailAddress = findViewById(R.id.emailregister);
        Password = findViewById(R.id.password);
        intoleranceSpinner = findViewById(R.id.intoleranceSpinner);
        Height = findViewById(R.id.height);
        Weight = findViewById(R.id.weight);
        Age = findViewById(R.id.age);
        GenderSpinner = findViewById(R.id.genderSpinner);
        Button submitBTN = findViewById(R.id.submitBtn);
        ProgressBar = findViewById(R.id.progressBar);
        TextView loginNow = findViewById(R.id.loginNow);

        // Setting click listener to navigate to the Login activity
        loginNow.setOnClickListener(v ->
        {
            // Intent to navigate from Register to Login activity
            Intent intent = new Intent(Register.this, Login.class);
            startActivity(intent);
            finish();
        });

        // Setup Spinner for intolerance
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.dietary_options_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        intoleranceSpinner.setAdapter(adapter);

        // Setup the Spinner for Gender
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(this, R.array.genderSpinner, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        GenderSpinner.setAdapter(genderAdapter);

        submitBTN.setOnClickListener(v ->
        {
            ProgressBar.setVisibility(View.VISIBLE);
            String email = EmailAddress.getText().toString().trim();
            String password = Password.getText().toString().trim();
            String intolerance = intoleranceSpinner.getSelectedItem().toString();
            String weight = Weight.getText().toString().trim();
            String height = Height.getText().toString().trim();
            String age = Age.getText().toString().trim();
            String gender = GenderSpinner.getSelectedItem().toString();

            if (!validateForm(email, password, weight, height, age)) {
                ProgressBar.setVisibility(View.INVISIBLE);
                return;
            }
            registerUser(email, password, intolerance, weight, height, age, gender);
        });
    }

    private boolean validateForm(String email, String password, String weight, String age, String height)
    {
        if (email.isEmpty())
        {
            Toast.makeText(Register.this, "Email is required.", Toast.LENGTH_SHORT).show();
        }
        if (password.isEmpty())
        {
            Toast.makeText(Register.this, "Password is required.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (weight.isEmpty())
        {
            Toast.makeText(Register.this, "Weight is required.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (height.isEmpty())
        {
            Toast.makeText(Register.this, "Height is required.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (age.isEmpty())
        {
            Toast.makeText(Register.this, "Age is required.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else
        {
            return true;
        }
    }

    private void registerUser(String email, String password, String intolerance, String weight, String height, String age, String gender)
    {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task ->
        {
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    // Encode the email address
                    String encodedEmail = email.replace(".", ",");
                    DatabaseReference userRefWithEmail = FirebaseDatabase.getInstance("https://mealplanner-a23cb-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users").child(encodedEmail);
                    HashMap<String, Object> userData = new HashMap<>();
                    userData.put("Email", email); // Storing the email
                    userData.put("Password", password); // Storing intolerance preference
                    userData.put("Weight", weight); // Storing Users Height
                    userData.put("Height", height); // Storing Users Weight
                    userData.put("Age", age); // Storing Users Age
                    userData.put("Gender", gender); // Storing Users Gender
                    userData.put("Intolerance", intolerance); // Storing Users Intolerance



                    userRefWithEmail.setValue(userData).addOnCompleteListener(task1 ->
                    {
                        if (task1.isSuccessful())
                        {
                            Toast.makeText(Register.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Register.this, Login.class);
                            startActivity(intent);
                            finish(); // Finish the current activity
                        } else
                        {
                            Toast.makeText(Register.this, "Failed to store user information. Error: " + Objects.requireNonNull(task1.getException()).getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } else
            {
                Toast.makeText(Register.this, "Registration failed. Error: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }



}