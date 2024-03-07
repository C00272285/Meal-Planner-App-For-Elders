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
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity
{
    private Button SubmitBTN;
    FirebaseAuth mAuth;
    ProgressBar ProgressBar;
    TextView LoginNow;
    EditText EmailAddress, Password;

    Button Login;

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        // Check if the user is already logged in
        if(currentUser != null)
        {
            Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
            startActivity(intent);
            finish();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mAuth = FirebaseAuth.getInstance();
        EmailAddress = findViewById(R.id.emailLogin);
        Password = findViewById(R.id.passwordLogin);
        ProgressBar = findViewById(R.id.loginProgressBar);
        LoginNow = findViewById(R.id.register);
        Login = findViewById(R.id.loginBTN);

        LoginNow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)
            {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        Login.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view) {
                ProgressBar.setVisibility(View.VISIBLE);
                String email, password;
                email = EmailAddress.getText().toString();
                password = Password.getText().toString();

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
                                ProgressBar.setVisibility(View.GONE);
                                if (task.isSuccessful())
                                {
                                    Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else
                                {
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(Login.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });

    }
}
