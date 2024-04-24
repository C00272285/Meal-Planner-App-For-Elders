package com.example.design;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    private Calendar currentWeekStart;
    private TextView weekDisplay;
    private Button shareButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        currentWeekStart = Calendar.getInstance();
        currentWeekStart.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        shareButton = findViewById(R.id.shareButton);
        weekDisplay = findViewById(R.id.weekDisplay);
        ImageView previousWeek = findViewById(R.id.previousWeekButton);
        ImageView nextWeek = findViewById(R.id.nextWeekButton);
        previousWeek.setOnClickListener(v -> navigateWeeks(-1));
        nextWeek.setOnClickListener(v -> navigateWeeks(1));
        updateDateDisplay();
        fetchWeekMealHistory();
        shareButton.setOnClickListener(view -> shareWeekMealHistory());
    }

    private void shareWeekMealHistory()
    {
        String shareBody = "My Meal Plan for the Week: " + weekDisplay.getText() + "\n\n" +
                "Monday: \nBreakfast: " + ((TextView) findViewById(R.id.breakfastMonday)).getText() +
                "\nLunch: " + ((TextView) findViewById(R.id.lunchMonday)).getText() +
                "\nDinner: " + ((TextView) findViewById(R.id.dinnerMonday)).getText() + "\n\n";
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My Meal Plan");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }
    private void navigateWeeks(int weeks)
    {
        currentWeekStart.add(Calendar.WEEK_OF_YEAR, weeks);
        updateDateDisplay();
        fetchWeekMealHistory();
    }

    private void updateDateDisplay()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd", Locale.getDefault());
        Calendar endWeek = (Calendar) currentWeekStart.clone();
        endWeek.add(Calendar.DAY_OF_WEEK, 6);

        String dateRange = dateFormat.format(currentWeekStart.getTime()) + " - " + dateFormat.format(endWeek.getTime());
        weekDisplay.setText(dateRange);
    }

    private void fetchWeekMealHistory()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null && currentUser.getEmail() != null)
        {
            String encodedEmail = encodeEmail(currentUser.getEmail());
            DatabaseReference userRef = FirebaseDatabase.getInstance("https://mealplanner-a23cb-default-rtdb.europe-west1.firebasedatabase.app/")
                    .getReference("Users").child(encodedEmail).child("Meals");

            for (int i = 0; i < 7; i++)
            {
                Calendar date = (Calendar) currentWeekStart.clone();
                date.add(Calendar.DAY_OF_MONTH, i);
                String dateKey = dateFormat.format(date.getTime());

                final int dayIndex = i;
                DatabaseReference dateRef = userRef.child(dateKey);
                dateRef.addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        String breakfast = dataSnapshot.child("breakfast").getValue(String.class);
                        String lunch = dataSnapshot.child("lunch").getValue(String.class);
                        String dinner = dataSnapshot.child("dinner").getValue(String.class);

                        updateMealDisplay(dayIndex, "Breakfast: " + (breakfast != null ? breakfast : "No breakfast logged"),
                                "Lunch: " + (lunch != null ? lunch : "No lunch logged"),
                                "Dinner: " + (dinner != null ? dinner : "No dinner logged"));
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

    private void updateMealDisplay(int dayIndex, String breakfast, String lunch, String dinner)
    {
        @SuppressLint("DiscouragedApi") int breakfastId = getResources().getIdentifier("breakfast" + getDayOfWeek(dayIndex), "id", getPackageName());
        @SuppressLint("DiscouragedApi") int lunchId = getResources().getIdentifier("lunch" + getDayOfWeek(dayIndex), "id", getPackageName());
        @SuppressLint("DiscouragedApi") int dinnerId = getResources().getIdentifier("dinner" + getDayOfWeek(dayIndex), "id", getPackageName());

        TextView breakfastView = findViewById(breakfastId);
        TextView lunchView = findViewById(lunchId);
        TextView dinnerView = findViewById(dinnerId);

        breakfastView.setText(breakfast);
        lunchView.setText(lunch);
        dinnerView.setText(dinner);
    }

    private String getDayOfWeek(int dayIndex)
    {
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        return days[dayIndex];
    }

    private String encodeEmail(String email)
    {
        return email != null ? email.replace(".", ",") : null;
    }
}
