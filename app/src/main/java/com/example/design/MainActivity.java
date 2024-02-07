package com.example.design;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    Button btnInsert;
    EditText foreName;
    EditText surName;

     DatabaseReference MealPlanner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//This connects to the XML File



        btnInsert = findViewById(R.id.insertdata);
        foreName = findViewById(R.id.userforename);
        surName = findViewById(R.id.usersurname);

        MealPlanner = FirebaseDatabase.getInstance().getReference().child("Users");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");


        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertUserData();
            }
        });
    }

    private void InsertUserData(){
        String ForeName = foreName.getText().toString();
        String SurName = surName.getText().toString();

        Users users = new Users(ForeName,SurName);

        MealPlanner.push().setValue(users);
        Toast.makeText(this, "Data Inserted", Toast.LENGTH_SHORT).show();

    }



}