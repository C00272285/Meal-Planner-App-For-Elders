package com.example.design;
import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
public class HistoryActivity extends AppCompatActivity {
    private TextView dateDisplay, breakfastHistory, lunchHistory, dinnerHistory;
    private Calendar currentDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // Initialize TextViews
        dateDisplay = findViewById(R.id.dateDisplay);
        breakfastHistory = findViewById(R.id.breakfastHistory);
        lunchHistory = findViewById(R.id.lunchHistory);
        dinnerHistory = findViewById(R.id.dinnerHistory);
        currentDay = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        dateDisplay.setText(dateFormat.format(currentDay.getTime())); // Display the current date

        fetchMealHistory();
    }

    private void fetchMealHistory() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String dateKey = dateFormat.format(currentDay.getTime());
        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("users")
                .child(userId).child("mealHistory").child(dateKey);

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Assuming data is structured under "breakfast", "lunch", and "dinner" child nodes
                String breakfast = dataSnapshot.child("breakfast").getValue(String.class);
                String lunch = dataSnapshot.child("lunch").getValue(String.class);
                String dinner = dataSnapshot.child("dinner").getValue(String.class);

                // Set text on TextViews. If meal is null, display a placeholder text.
                breakfastHistory.setText(breakfast != null ? breakfast : "No breakfast logged");
                lunchHistory.setText(lunch != null ? lunch : "No lunch logged");
                dinnerHistory.setText(dinner != null ? dinner : "No dinner logged");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }
}
