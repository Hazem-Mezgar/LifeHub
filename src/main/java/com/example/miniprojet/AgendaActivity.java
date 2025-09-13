package com.example.miniprojet; // Déclare le package de l’application

// Importation des classes nécessaires
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog; // Permet de créer des boîtes de dialogue modernes
import androidx.appcompat.app.AppCompatActivity; // Classe de base pour toutes les activités
import androidx.room.Room; // Fournit un accès à la base de données Room

import com.google.android.material.floatingactionbutton.FloatingActionButton; // Bouton flottant

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AgendaActivity extends AppCompatActivity {

    // Déclaration des composants de l’interface utilisateur
    private CalendarView calendarView; // Vue calendrier
    private ListView eventsListView;   // Liste des événements
    private FloatingActionButton fabAddEvent; // Bouton pour ajouter un événement

    private AppDatabase db;        // Référence à la base de données Room
    private String selectedDate;   // Date actuellement sélectionnée

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda); // Lier l’activité à son layout XML

        // Lier les composants de l’interface aux éléments du layout
        calendarView = findViewById(R.id.calendarView);
        eventsListView = findViewById(R.id.eventsListView);
        fabAddEvent = findViewById(R.id.fabAddEvent);

        // Initialiser la base de données Room
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "events-db")
                .allowMainThreadQueries() // Permet d’exécuter les requêtes DB dans le thread principal (pas conseillé en production)
                .build();

        // Initialiser la date sélectionnée avec la date du jour
        Calendar calendar = Calendar.getInstance();
        selectedDate = calendar.get(Calendar.DAY_OF_MONTH) + "/" +
                (calendar.get(Calendar.MONTH) + 1) + "/" +
                calendar.get(Calendar.YEAR);

        // Afficher les événements pour la date sélectionnée
        displayEventsForDate(selectedDate);

        // Gérer le changement de date dans le calendrier
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
            displayEventsForDate(selectedDate); // Mettre à jour la liste
        });

        // Action lorsque l'utilisateur clique sur le bouton +
        fabAddEvent.setOnClickListener(view -> { // Lorsque le bouton pour ajouter un événement est cliqué
            EditText input = new EditText(AgendaActivity.this); // Créer un champ de texte pour que l'utilisateur saisisse le titre de l'événement
            input.setHint("Titre de l’événement"); // Ajouter un texte d'indice pour guider l'utilisateur (ex: "Titre de l'événement")

            // Créer et afficher un AlertDialog pour saisir un événement
            new AlertDialog.Builder(AgendaActivity.this) // Créer un builder pour construire la boîte de dialogue
                    .setTitle("Ajouter un événement") // Définir le titre de la boîte de dialogue
                    .setView(input) // Ajouter le champ de texte 'input' dans le dialogue
                    .setPositiveButton("Ajouter", (dialog, which) -> { // Ajouter un bouton "Ajouter" dans la boîte de dialogue
                        String title = input.getText().toString().trim(); // Récupérer le texte saisi par l'utilisateur et le nettoyer (enlever les espaces inutiles)

                        if (!title.isEmpty()) { // Vérifier si le titre n'est pas vide
                            Event event = new Event(); // Créer un nouvel objet événement
                            event.date = selectedDate; // Assigner la date sélectionnée à l'événement
                            event.title = title; // Assigner le titre saisi à l'événement
                            event.time = "14:00"; // Définir une heure fixe (peut être modifié pour être dynamique plus tard)

                            db.eventDao().insert(event); // Insérer l'événement dans la base de données via Room
                            Toast.makeText(this, "Événement ajouté !", Toast.LENGTH_SHORT).show(); // Afficher un message de confirmation (toast)
                            displayEventsForDate(selectedDate); // Rafraîchir la liste des événements pour la date sélectionnée
                        } else { // Si le titre est vide
                            Toast.makeText(this, "Le titre ne peut pas être vide.", Toast.LENGTH_SHORT).show(); // Afficher un message d'erreur
                        }
                    })
                    .setNegativeButton("Annuler", null) // Ajouter un bouton "Annuler" pour fermer la boîte de dialogue sans rien faire
                    .show(); // Afficher la boîte de dialogue à l'écran
        });

    }

    // Méthode pour afficher les événements d’une date spécifique
    private void displayEventsForDate(String date) {
        List<Event> eventList = db.eventDao().getEventsForDate(date); // Charger depuis la base
        ArrayList<String> eventStrings = new ArrayList<>();

        if (!eventList.isEmpty()) {
            for (Event e : eventList) {
                eventStrings.add(e.time + " - " + e.title); // Formater chaque événement
            }
        } else {
            eventStrings.add("Aucun événement ce jour"); // Aucun événement trouvé
        }

        // Créer l’adapter personnalisé et l’associer à la ListView
        EventListAdapter adapter = new EventListAdapter(this, eventStrings);
        eventsListView.setAdapter(adapter);
    }
}
