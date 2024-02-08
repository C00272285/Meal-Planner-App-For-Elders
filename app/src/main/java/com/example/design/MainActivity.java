package com.example.design;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
//import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText firstname, surname;
    Button submit;

    DatabaseReference ref;
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstname = findViewById(R.id.userforename);
        surname = findViewById(R.id.usersurname);
        submit = findViewById(R.id.insertdata);
        user = new User();

        // Connect to Firebase Realtime Database in the Belgium region
        ref = FirebaseDatabase.getInstance("https://mealplanner-a23cb-default-rtdb.europe-west1.firebasedatabase.app/").getReference().child("User");

        submit.setOnClickListener(v -> {
            user.setFirstname(firstname.getText().toString().trim());
            user.setSurname(surname.getText().toString().trim());
            ref.push().setValue(user);
            Toast.makeText(MainActivity.this, "Data has been sent", Toast.LENGTH_LONG).show();
        });
    }






}