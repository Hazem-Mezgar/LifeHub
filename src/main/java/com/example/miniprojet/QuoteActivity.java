package com.example.miniprojet;

import android.app.NotificationChannel;  // Importation de la classe NotificationChannel pour créer un canal de notification
import android.app.NotificationManager;  // Importation de la classe NotificationManager pour gérer les notifications
import android.app.PendingIntent;  // Importation de la classe PendingIntent pour permettre l'ouverture d'une activité à partir de la notification
import android.content.Intent;  // Importation de la classe Intent pour lancer une activité
import android.content.pm.PackageManager;  // Importation de la classe PackageManager pour gérer les permissions
import android.os.Build;  // Importation de la classe Build pour gérer les versions d'Android
import android.os.Bundle;  // Importation de la classe Bundle pour récupérer les données d'activité
import android.os.Handler;  // Importation de la classe Handler pour exécuter des tâches après un délai
import android.os.Looper;  // Importation de la classe Looper pour s'assurer que le Handler fonctionne sur le thread principal
import android.widget.Button;  // Importation de la classe Button pour utiliser les boutons dans l'interface
import android.widget.TextView;  // Importation de la classe TextView pour afficher du texte
import androidx.appcompat.app.AppCompatActivity;  // Importation de la classe AppCompatActivity pour gérer les activités avec compatibilité
import androidx.core.app.ActivityCompat;  // Importation de la classe ActivityCompat pour gérer les permissions
import androidx.core.app.NotificationCompat;  // Importation de la classe NotificationCompat pour créer des notifications compatibles avec différentes versions d'Android
import androidx.core.app.NotificationManagerCompat;  // Importation de la classe NotificationManagerCompat pour gérer les notifications
import java.util.Random;  // Importation de la classe Random pour générer des nombres aléatoires

public class QuoteActivity extends AppCompatActivity {  // Déclaration de l'activité QuoteActivity qui hérite de AppCompatActivity
    private static final String CHANNEL_ID = "quote_channel";  // ID du canal de notification
    private int notificationId = 0;  // ID de la notification, utilisé pour la gestion de plusieurs notifications
    private Handler handler;  // Déclaration de Handler pour exécuter des tâches différées///permet d’exécuter du code
    // (comme des tâches ou des messages) de manière différée ou répétée dans un thread, souvent le thread principal (UI).
    private Runnable notificationRunnable;  // Déclaration de Runnable pour exécuter des tâches répétées//
    // /c’est une tâche à exécuter, définie par une méthode run().
    private String[] quotes = {  // Tableau contenant des citations inspirantes
            "“The only way to do great work is to love what you do.” — Steve Jobs",
            "“Stay hungry, stay foolish.” — Steve Jobs",
            "“Believe you can and you're halfway there.” — Theodore Roosevelt",
            "“The best way to predict the future is to create it.” — Peter Drucker",
            "“You miss 100% of the shots you don’t take.” — Wayne Gretzky"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {  // Méthode appelée lors de la création de l'activité
        super.onCreate(savedInstanceState);  // Appel du constructeur parent pour initialiser l'activité
        setContentView(R.layout.activity_quote);  // Définir le layout de l'activité avec le fichier XML correspondant
        createNotificationChannel();  // Appeler la méthode pour créer un canal de notification

        // Initialisation des éléments d'interface utilisateur
        TextView quoteText = findViewById(R.id.quoteText);  // Récupérer la référence de TextView pour afficher la citation
        TextView authorText = findViewById(R.id.authorText);  // Récupérer la référence de TextView pour afficher l'auteur
        Button refreshButton = findViewById(R.id.refreshQuoteButton);  // Récupérer la référence du bouton pour actualiser la citation

        // Afficher une citation aléatoire au démarrage
        updateQuote(quoteText, authorText);

        // Configurer l'action pour actualiser la citation lorsque l'utilisateur clique sur le bouton
        refreshButton.setOnClickListener(v -> updateQuote(quoteText, authorText));  // Appeler la méthode pour changer la citation

        // Configurer un gestionnaire pour envoyer une notification toutes les 3 secondes
        handler = new Handler(Looper.getMainLooper());  // Initialiser le Handler avec le thread principal
        notificationRunnable = new Runnable() {  // Définir le Runnable qui envoie une notification
            @Override
            public void run() {
                showNotification();  // Appeler la méthode pour afficher la notification
                handler.postDelayed(this, 3000);  // Exécuter le Runnable toutes les 3 secondes
            }
        };
        handler.postDelayed(notificationRunnable, 3000);  // Démarrer l'exécution du Runnable après 3 secondes
    }

    // Méthode pour mettre à jour le texte de la citation et de l'auteur
    private void updateQuote(TextView quoteText, TextView authorText) {
        // Sélectionner une citation aléatoire et séparer la citation de l'auteur
        String[] quoteParts = quotes[new Random().nextInt(quotes.length)].split(" — ");
        quoteText.setText(quoteParts[0]);  // Afficher la citation
        authorText.setText("— " + quoteParts[1]);  // Afficher le nom de l'auteur
    }

    // Méthode pour créer un canal de notification (nécessaire pour les versions d'Android 8.0 et supérieures)
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {  // Vérifier si la version d'Android est supérieure ou égale à 8.0
            CharSequence name = "Quote Channel";  // Nom du canal de notification
            String description = "Daily inspirational quotes";  // Description du canal
            int importance = NotificationManager.IMPORTANCE_DEFAULT;  // Niveau de priorité des notifications

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);  // Créer le canal de notification
            channel.setDescription(description);  // Ajouter une description au canal

            NotificationManager notificationManager = getSystemService(NotificationManager.class);  // Récupérer le gestionnaire de notifications
            notificationManager.createNotificationChannel(channel);  // Créer le canal dans le système
        }
    }

    // Méthode pour afficher une notification avec une citation aléatoire
    private void showNotification() {
        // Créer une intention pour ouvrir l'activité de citation lors du clic sur la notification
        Intent intent = new Intent(this, QuoteActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        //FLAG_UPDATE_CURRENT	Réutilise un PendingIntent existant en mettant à jour ses extras
        //FLAG_IMMUTABLE	Rend le PendingIntent non modifiable une fois créé

        // Sélectionner une citation aléatoire
        String[] quoteParts = quotes[new Random().nextInt(quotes.length)].split(" — ");

        // Créer la notification avec les détails
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)  // Icône de la notification
                .setContentTitle("Daily Quote")  // Titre de la notification
                .setContentText(quoteParts[0])  // Contenu de la notification (citation)
                .setContentIntent(pendingIntent)  // L'intention à exécuter lorsqu'on clique sur la notification
                .setAutoCancel(true)  // Fermer la notification après qu'elle a été cliquée
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);  // Priorité de la notification

        // Vérifier la permission pour afficher des notifications et demander la permission si nécessaire
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
            return;
        }
        notificationManager.notify(notificationId++, builder.build());  // Afficher la notification avec un ID unique
    }

    @Override
    protected void onDestroy() {  // Méthode appelée lors de la destruction de l'activité
        super.onDestroy();  // Appel du constructeur parent
        handler.removeCallbacks(notificationRunnable);  // Supprimer les appels différés du Runnable pour arrêter les notifications
    }
}


//Handler	Planifie l’exécution différée et répétée des notifications
//Runnable	Définit ce qu’il faut faire : ici, appeler showNotification()
//postDelayed	Lance le Runnable après 3 secondes, puis le relance encore
