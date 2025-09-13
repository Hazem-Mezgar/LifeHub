package com.example.miniprojet;

import android.media.MediaPlayer;  // Importation de la classe MediaPlayer pour gérer la lecture de fichiers audio
import android.os.Bundle;  // Importation de la classe Bundle pour gérer les données de l'activité
import android.widget.ImageButton;  // Importation de la classe ImageButton pour les boutons d'interface graphique
import android.widget.TextView;  // Importation de la classe TextView pour afficher des textes
import android.widget.Toast;  // Importation de la classe Toast pour afficher des messages temporaires à l'utilisateur
import androidx.appcompat.app.AppCompatActivity;  // Importation de la classe AppCompatActivity pour la compatibilité avec les anciennes versions d'Android

public class MusicActivity extends AppCompatActivity {  // Déclaration de la classe MusicActivity qui hérite d'AppCompatActivity
    private MediaPlayer mediaPlayer;  // Déclaration d'une variable MediaPlayer pour lire les fichiers audio
    private boolean isPlaying = false;  // Déclaration d'une variable pour suivre l'état de la lecture (si la musique est en cours de lecture ou en pause)
    private int[] songs = {R.raw.song1, R.raw.song2};  // Tableau contenant les ressources audio des chansons (ici song1 et song2)
    private String[] songTitles = {"ESS", "sidi ali azouz"};  // Tableau contenant les titres des chansons
    private String[] artistNames = {"BRO1", "hedi donya"};  // Tableau contenant les noms des artistes des chansons
    private int currentSongIndex = 0;  // Indice de la chanson en cours de lecture (commence par la première chanson)

    @Override
    protected void onCreate(Bundle savedInstanceState) {  // Méthode appelée lors de la création de l'activité
        super.onCreate(savedInstanceState);  // Appel du constructeur parent pour initialiser l'activité
        setContentView(R.layout.activity_music);  // Définir le layout de l'activité (UI) à partir du fichier XML

        // Initialisation des boutons et des vues pour afficher les informations
        ImageButton playButton = findViewById(R.id.playButton);  // Bouton pour jouer/mettre en pause la musique
        ImageButton stopButton = findViewById(R.id.stopButton);  // Bouton pour arrêter la musique
        ImageButton nextButton = findViewById(R.id.nextButton);  // Bouton pour passer à la chanson suivante
        TextView songTitle = findViewById(R.id.songTitle);  // TextView pour afficher le titre de la chanson
        TextView artistName = findViewById(R.id.artistName);  // TextView pour afficher le nom de l'artiste

        // Initialisation du lecteur audio (MediaPlayer) avec la première chanson du tableau
        mediaPlayer = MediaPlayer.create(this, songs[currentSongIndex]);
        updateSongInfo(songTitle, artistName);  // Mise à jour des informations de la chanson (titre et artiste)

        // Action du bouton Play/Pause
        playButton.setOnClickListener(v -> {  // Lorsque le bouton Play/Pause est cliqué
            if (isPlaying) {  // Si la musique est déjà en train de jouer
                mediaPlayer.pause();  // Mettre la musique en pause
                playButton.setBackgroundResource(R.drawable.play_arrow);  // Changer l'icône du bouton pour l'icône de lecture
                Toast.makeText(this, "Paused", Toast.LENGTH_SHORT).show();  // Afficher un message "Paused"
            } else {  // Si la musique est en pause
                mediaPlayer.start();  // Démarrer la lecture de la musique
                playButton.setBackgroundResource(R.drawable.pause);  // Changer l'icône du bouton pour l'icône de pause
                Toast.makeText(this, "Playing", Toast.LENGTH_SHORT).show();  // Afficher un message "Playing"
            }
            isPlaying = !isPlaying;  // Inverser l'état de la lecture (si la musique est en cours de lecture ou en pause)
        });

        // Action du bouton Stop
        stopButton.setOnClickListener(v -> {  // Lorsque le bouton Stop est cliqué
            if (mediaPlayer != null) {  // Si le MediaPlayer est initialisé
                mediaPlayer.stop();  // Arrêter la musique
                mediaPlayer = MediaPlayer.create(this, songs[currentSongIndex]);  // Réinitialiser le MediaPlayer avec la chanson en cours
                playButton.setBackgroundResource(R.drawable.play_arrow);  // Changer l'icône du bouton pour l'icône de lecture
                isPlaying = false;  // Marquer que la musique est arrêtée
                Toast.makeText(this, "Stopped", Toast.LENGTH_SHORT).show();  // Afficher un message "Stopped"
            }
        });

        // Action du bouton Next (pour passer à la chanson suivante)
        nextButton.setOnClickListener(v -> {  // Lorsque le bouton Next est cliqué
            if (mediaPlayer != null) {  // Si le MediaPlayer est initialisé
                mediaPlayer.stop();  // Arrêter la musique en cours
                mediaPlayer.release();  // Libérer les ressources du MediaPlayer
                currentSongIndex = (currentSongIndex + 1) % songs.length;  // Passer à la chanson suivante (en boucle)
                mediaPlayer = MediaPlayer.create(this, songs[currentSongIndex]);  // Initialiser le MediaPlayer avec la nouvelle chanson
                updateSongInfo(songTitle, artistName);  // Mettre à jour les informations sur la chanson et l'artiste
                mediaPlayer.start();  // Démarrer la lecture de la nouvelle chanson
                playButton.setBackgroundResource(R.drawable.pause);  // Changer l'icône du bouton pour l'icône de pause
                isPlaying = true;  // Marquer que la musique est en cours de lecture
                Toast.makeText(this, "Next Song", Toast.LENGTH_SHORT).show();  // Afficher un message "Next Song"
            }
        });
    }

    // Méthode pour mettre à jour les informations de la chanson (titre et artiste)
    private void updateSongInfo(TextView songTitle, TextView artistName) {
        songTitle.setText(songTitles[currentSongIndex]);  // Mettre à jour le titre de la chanson
        artistName.setText(artistNames[currentSongIndex]);  // Mettre à jour le nom de l'artiste
    }

    @Override
    protected void onDestroy() {  // Méthode appelée lors de la destruction de l'activité
        super.onDestroy();  // Appel du constructeur parent pour gérer la destruction de l'activité
        if (mediaPlayer != null) {  // Si le MediaPlayer est initialisé
            mediaPlayer.release();  // Libérer les ressources utilisées par le MediaPlayer
            mediaPlayer = null;  // Mettre le MediaPlayer à null pour éviter les fuites de mémoire
        }
    }
}
