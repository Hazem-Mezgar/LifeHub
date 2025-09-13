package com.example.miniprojet;
// Déclare le package de cette activité. Cela organise le code dans l'application.

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;
// Importe les classes nécessaires : gestion du cycle de vie, widgets UI, générateur aléatoire, etc.

public class WellnessActivity extends AppCompatActivity {
    // Déclare la classe de l’activité, qui hérite de AppCompatActivity (activité avec compatibilité étendue).

    private String[] tips = {
            // Tableau contenant plusieurs astuces bien-être que l'on affiche aléatoirement.
            "Take 5 deep breaths to relax.",
            "Drink a glass of water mindfully.",
            "Do a 5-minute stretch.",
            "Write down something you’re grateful for.",
            "Listen to calming music."
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Méthode appelée lorsque l’activité est créée.
        super.onCreate(savedInstanceState); // Appelle la méthode parent pour initialiser l'activité.
        setContentView(R.layout.activity_wellness); // Lie cette activité au layout XML `activity_wellness.xml`.

        TextView wellnessTip = findViewById(R.id.wellnessTip);
        // Récupère la zone de texte (TextView) dans laquelle on affichera l'astuce.

        Button refreshButton = findViewById(R.id.refreshTipButton);
        // Récupère le bouton qui permet de changer l'astuce affichée.

        // ---------------- Affichage initial ----------------
        wellnessTip.setText(tips[new Random().nextInt(tips.length)]);
        // Affiche une astuce aléatoire dès que l'activité démarre.

        // ---------------- Action quand on clique sur le bouton ----------------
        refreshButton.setOnClickListener(v -> {
            // Quand l'utilisateur clique sur le bouton...
            wellnessTip.setText(tips[new Random().nextInt(tips.length)]);
            // ... on sélectionne et affiche une nouvelle astuce aléatoire.
        });
    }
}
