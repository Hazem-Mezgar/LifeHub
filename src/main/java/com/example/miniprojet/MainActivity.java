package com.example.miniprojet;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Initialize BottomNavigationView
        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setSelectedItemId(R.id.nav_home);
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                // Rien à faire, Home est déjà affiché
                return true;
            } else if (itemId == R.id.nav_profile) {
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_settings) {
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });

        // Click listener for news ImageView
        ImageView newsImage = findViewById(R.id.newsImage);
        newsImage.setOnClickListener(v -> {
            Intent intent = new Intent(this, NewsActivity.class);
            startActivity(intent);
        });

        // Click listener for weather ImageView
        ImageView weatherImage = findViewById(R.id.weatherImage); // Fixed typo
        weatherImage.setOnClickListener(v -> {
            Intent intent = new Intent(this, WeatherActivity.class);
            startActivity(intent);
        });

        // Click listener for calendar ImageView
        ImageView calendarImage = findViewById(R.id.calendarImage);
        calendarImage.setOnClickListener(v -> {
            Intent intent = new Intent(this, AgendaActivity.class);
            startActivity(intent);
        });

        // Click listener for wellness ImageView
        ImageView wellnessImage = findViewById(R.id.fitnessImage);
        wellnessImage.setOnClickListener(v -> {
            Intent intent = new Intent(this, WellnessActivity.class);
            startActivity(intent);
        });

        // Click listener for music ImageView
        ImageView musicImage = findViewById(R.id.musicImage);
        musicImage.setOnClickListener(v -> {
            Intent intent = new Intent(this, MusicActivity.class);
            startActivity(intent);
        });

        // Click listener for quote ImageView
        ImageView quoteImage = findViewById(R.id.quoteImage);
        quoteImage.setOnClickListener(v -> {
            Intent intent = new Intent(this, QuoteActivity.class);
            startActivity(intent);
        });
    }
}