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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Register extends AppCompatActivity {

    private EditText EmailAddress, Password;
    private Spinner intoleranceSpinner;
    private Button SubmitBTN;
    private ProgressBar ProgressBar;

    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    private TextView LoginNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        // Initialize FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();
        // Reference to your Firebase Realtime Database
        userRef = FirebaseDatabase.getInstance().getReference("users");

        // Initialize UI components
        EmailAddress = findViewById(R.id.emailregister);
        Password = findViewById(R.id.password);
        intoleranceSpinner = findViewById(R.id.intoleranceSpinner);
        SubmitBTN = findViewById(R.id.submitBtn);
        ProgressBar = findViewById(R.id.progressBar);



        LoginNow = findViewById(R.id.loginNow);

// Setting click listener to navigate to the Login activity
        LoginNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to navigate from Register to Login activity
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });

        // Setup Spinner for intolerance
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.dietary_options_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        intoleranceSpinner.setAdapter(adapter);

        SubmitBTN.setOnClickListener(v -> {
            ProgressBar.setVisibility(View.VISIBLE);
            String email = EmailAddress.getText().toString().trim();
            String password = Password.getText().toString().trim();
            String intolerance = intoleranceSpinner.getSelectedItem().toString();

            if (!validateForm(email, password)) {
                ProgressBar.setVisibility(View.INVISIBLE);
                return;
            }

            registerUser(email, password, intolerance);
        });
    }

    private boolean validateForm(String email, String password) {
        if (email.isEmpty()) {
            Toast.makeText(Register.this, "Email is required.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.isEmpty()) {
            Toast.makeText(Register.this, "Password is required.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void registerUser(String email, String password, String intolerance) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                // Sign in success
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    updateUserInformation(user.getUid(), email, intolerance);
                }
            } else {
                // If sign in fails, display a message to the user.
                Toast.makeText(Register.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                ProgressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void updateUserInformation(String userId, String email, String intolerance) {
        HashMap<String, Object> userData = new HashMap<>();
        userData.put("email", email);
        userData.put("intolerance", intolerance);

        userRef.child(userId).setValue(userData).addOnCompleteListener(task -> {
            ProgressBar.setVisibility(View.INVISIBLE);
            Intent intent = new Intent(Register.this, Login.class);
            startActivity(intent);
            finish(); // Finish the current activity
            if (task.isSuccessful()) {
                Toast.makeText(Register.this, "User registration successful.", Toast.LENGTH_SHORT).show();
                // Intent to navigate to the Login screen

            } else {
                Toast.makeText(Register.this, "Failed to save user data.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
