package com.example.design;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
public class HistoryActivity extends AppCompatActivity
{
    private TextView dateDisplay, breakfastHistory, lunchHistory, dinnerHistory;
    private Calendar currentDay;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        dateDisplay = findViewById(R.id.dateDisplay);
        breakfastHistory = findViewById(R.id.breakfastHistory);
        lunchHistory = findViewById(R.id.lunchHistory);
        dinnerHistory = findViewById(R.id.dinnerHistory);
        ImageView previousDay = findViewById(R.id.previousDay);
        ImageView nextDay = findViewById(R.id.nextDay);
        currentDay = Calendar.getInstance();

        updateDateDisplay();

        previousDay.setOnClickListener(v -> {
            navigateDays(-1);
            fetchMealHistory();
        });

        nextDay.setOnClickListener(v -> {
            navigateDays(1);
            fetchMealHistory();
        });

        fetchMealHistory();
    }
    private String encodeEmail(String email)
    {
        if (email == null)
        {
            return null;
        }
        return email.replace(".", ",");
    }

    private void navigateDays(int days)
    {
        currentDay.add(Calendar.DAY_OF_MONTH, days);
        updateDateDisplay();
        fetchMealHistory();
    }


    private String getFormattedDateString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(currentDay.getTime());
    }


    private void updateDateDisplay() {
        dateDisplay.setText(getFormattedDateString());
    }


    private void fetchMealHistory()
    {
        String dateKey = getFormattedDateString();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null && currentUser.getEmail() != null)
        {
            String encodedEmail = encodeEmail(currentUser.getEmail());
            DatabaseReference databaseRef = FirebaseDatabase.getInstance("https://mealplanner-a23cb-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users").child(encodedEmail).child("Meals").child(dateKey);

            databaseRef.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    String breakfast = dataSnapshot.child("breakfast").getValue(String.class);
                    String lunch = dataSnapshot.child("lunch").getValue(String.class);
                    String dinner = dataSnapshot.child("dinner").getValue(String.class);

                    breakfastHistory.setText(breakfast != null ? "Breakfast: " + breakfast : "No breakfast logged");
                    lunchHistory.setText(lunch != null ? "Lunch: " + lunch : "No lunch logged");
                    dinnerHistory.setText(dinner != null ? "Dinner: " + dinner : "No dinner logged");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });
        }
    }

}
