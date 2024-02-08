package com.example.design;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity
{
    //  Variables to get the id's from the XML file
    EditText breakfast, lunch, dinner;  //TextFields
    Button submit;  //Button used for inserting
    CalendarView calendarView;  //Calendar to select the date for the meal
    DatabaseReference ref;  //Reference to the Firebase database
    User user;  //Java class User that holds the setters and getters

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //The XML id's are set to the variables
        breakfast = findViewById(R.id.breakfast);
        lunch = findViewById(R.id.lunch);
        dinner = findViewById(R.id.dinner);
        calendarView = findViewById(R.id.calendarView2);
        user = new User();

        // Connect to Firebase Realtime Database in the Belgium region
        ref = FirebaseDatabase.getInstance("https://mealplanner-a23cb-default-rtdb.europe-west1.firebasedatabase.app/").getReference().child("User");

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener()
        {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth)
            {
                //Format the selected date
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String selectedDate = dateFormat.format(view.getDate());

                //Set the selected date in the User object
                user.setDate(selectedDate);
            }
        });

        submit = findViewById(R.id.insertdata);
        submit.setOnClickListener(v ->
        {
            //Get the entered meal details
            String breakfastText = breakfast.getText().toString().trim();
            String lunchText = lunch.getText().toString().trim();
            String dinnerText = dinner.getText().toString().trim();

            //Set the meal details in the User object
            user.setBreakfast(breakfastText);
            user.setLunch(lunchText);
            user.setDinner(dinnerText);

            //Create a HashMap to specify the order of fields
            //  "Still not in order as of the moment"
            Map<String, Object> userData = new HashMap<>();
            userData.put("Date", user.getDate());
            userData.put("Breakfast", user.getBreakfast());
            userData.put("Lunch", user.getLunch());
            userData.put("Dinner", user.getDinner());

            //Push the User object to the Firebase Realtime Database with specified order
            ref.push().setValue(userData);

            //Show a toast message indicating that the data has been sent
            Toast.makeText(MainActivity.this, "Data has been sent", Toast.LENGTH_LONG).show();
        });
    }
}
