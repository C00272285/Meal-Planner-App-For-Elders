package com.example.design;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import android.os.Bundle;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity
{
    private Button SubmitBTN;
    FirebaseAuth mAuth;
    ProgressBar ProgressBar;
    TextView LoginNow, Register;
    EditText EmailAddress, Password;

    Button Login;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            // User is not logged in, stay on the login screen.
        } else {
            // User is already logged in, redirect to MenuActivity.
            Intent intent = new Intent(Login.this, MenuActivity.class);
            startActivity(intent);
            finish(); // Close the login activity
        }
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mAuth = FirebaseAuth.getInstance();

        // Initialize UI components
        EmailAddress = findViewById(R.id.emailLogin);
        Password = findViewById(R.id.passwordLogin);
        ProgressBar = findViewById(R.id.loginProgressBar);
        Login = findViewById(R.id.loginBTN);
        Register = findViewById(R.id.register); // Make sure

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the Register activity
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });

        Login.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view) {
                ProgressBar.setVisibility(View.VISIBLE);
                String email = EmailAddress.getText().toString();
                String password = Password.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Login.this, "Enter in a EmailAddress", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(Login.this, "Enter in a Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task)
                            {
                                if (task.isSuccessful())
                                {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null) {
                                        String userId = user.getUid();
                                        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);

                                        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                // Here you retrieve the intolerance information from the database
                                                String intolerance = dataSnapshot.child("intolerance").getValue(String.class);

                                                // After successfully retrieving user data, move to the next activity
                                                ProgressBar.setVisibility(View.GONE);
                                                Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();

                                                // You could pass the intolerance information to the next activity as an extra
                                                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                                                intent.putExtra("USER_INTOLERANCE", intolerance);
                                                startActivity(intent);
                                                finish();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                // Handle possible errors
                                            }
                                        });
                                    }
                                }
                                else
                                {
                                    ProgressBar.setVisibility(View.GONE);
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });


    }
}