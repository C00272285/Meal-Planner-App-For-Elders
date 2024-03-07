package com.example.design;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

public class Register extends AppCompatActivity
{

    private Button SubmitBTN;
    FirebaseAuth mAuth;
    ProgressBar ProgressBar;
    TextView LoginNow;
    EditText EmailAddress, Password;




    public void onStart()
    {
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

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        mAuth = FirebaseAuth.getInstance();

//        Name = findViewById(R.id.name);
//        Surname = findViewById(R.id.surname);
        EmailAddress = findViewById(R.id.emailregister);
        Password = findViewById(R.id.password);

//        Date = findViewById(R.id.dob);
        SubmitBTN = findViewById(R.id.submitBtn);
        ProgressBar = findViewById(R.id.progressBar);
        LoginNow = findViewById(R.id.loginNow);

        LoginNow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)
            {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        SubmitBTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ProgressBar.setVisibility(View.VISIBLE);
                String email, password;
                email = EmailAddress.getText().toString();
                password = Password.getText().toString();

                if(TextUtils.isEmpty(email))
                {
                    Toast.makeText(Register.this, "Enter in a EmailAddress", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(password))
                {
                    Toast.makeText(Register.this, "Enter in a Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task)
                            {
                                ProgressBar.setVisibility(View.GONE);
                                if (task.isSuccessful())
                                {

                                    Toast.makeText(Register.this, "Authentication Created.",
                                            Toast.LENGTH_SHORT).show();

                                } else
                                {


                                    Toast.makeText(Register.this, task.getException().getLocalizedMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });

    }
}
