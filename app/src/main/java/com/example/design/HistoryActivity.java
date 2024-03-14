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
public class HistoryActivity extends AppCompatActivity
{
    private TextView mealHistory;
    private Calendar currentDay;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        mealHistory = findViewById(R.id.mealHistory);
        currentDay = Calendar.getInstance();
        fetchMealHistory();
    }

    private void fetchMealHistory()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String dateKey = dateFormat.format(currentDay.getTime());
        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("users")
                .child(userId).child("mealHistory").child(dateKey);

        databaseRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                StringBuilder mealsForTheDay = new StringBuilder();
                for (DataSnapshot mealSnapshot: dataSnapshot.getChildren())
                {
                    String meal = mealSnapshot.getValue(String.class);
                    mealsForTheDay.append(meal).append("\n");
                }

                mealHistory.setText(mealsForTheDay.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }
}
