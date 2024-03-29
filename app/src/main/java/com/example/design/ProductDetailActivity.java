package com.example.design;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
//  This class will act as a information screen for the scanned item, displaying the information of it
public class ProductDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail);

        //  Display the Scanned item details
        TextView productNameTextView = findViewById(R.id.product_name_text_view);
        TextView brandTextView = findViewById(R.id.brand_text_view);
        TextView energyTextView = findViewById(R.id.energy_text_view);
        TextView proteinTextView = findViewById(R.id.protein_text_view);
        TextView sugarsTextView = findViewById(R.id.sugars_text_view);
        TextView fatTextView = findViewById(R.id.fat_text_view);
        TextView fiberTextView = findViewById(R.id.fiber_text_view);

        //  Get the Scanned items details that are passed from ScanActivity class
        Intent intent = getIntent();
        String productName = intent.getStringExtra("productName");
        String brand = intent.getStringExtra("brand");
        double energy = intent.getDoubleExtra("energy", 0);
        double protein = intent.getDoubleExtra("protein", 0);
        double sugars = intent.getDoubleExtra("sugars", 0);
        double fat = intent.getDoubleExtra("fat", 0);
        double fiber = intent.getDoubleExtra("fiber", 0);

        //  Display the information
        productNameTextView.setText(productName);
        brandTextView.setText(brand);
        energyTextView.setText(getString(R.string.energy_format, energy));
        proteinTextView.setText(getString(R.string.protein_format, protein));
        sugarsTextView.setText(getString(R.string.sugars_format, sugars));
        fatTextView.setText(getString(R.string.fat_format, fat));
        fiberTextView.setText(getString(R.string.fiber_format, fiber));
    }
}
