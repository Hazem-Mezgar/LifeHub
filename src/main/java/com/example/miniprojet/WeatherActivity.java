package com.example.miniprojet; // Déclare le package de l'application

// Importation des bibliothèques nécessaires
import android.Manifest; // Pour accéder aux constantes des permissions
import android.annotation.SuppressLint; // Pour supprimer certains avertissements du compilateur
import android.content.pm.PackageManager; // Pour gérer les permissions de l'application
import android.os.Bundle; // Pour gérer les données d'activité Android
import android.widget.Button; // Pour utiliser le composant bouton
import android.widget.TextView; // Pour afficher du texte
import android.widget.Toast; // Pour afficher de petits messages à l’utilisateur

import androidx.appcompat.app.AppCompatActivity; // Classe de base pour une activité
import androidx.core.app.ActivityCompat; // Pour vérifier et demander les permissions à l’utilisateur

import com.android.volley.Request; // Pour faire des requêtes HTTP avec Volley
import com.android.volley.toolbox.StringRequest; // Requête qui attend une réponse sous forme de String
import com.android.volley.toolbox.Volley; // File d'attente de requêtes réseau avec Volley
import com.google.android.gms.location.FusedLocationProviderClient; // Pour obtenir la localisation de l'utilisateur
import com.google.android.gms.location.LocationServices; // Fournit le service de localisation

import org.json.JSONObject; // Pour traiter les données JSON de l’API météo

public class WeatherActivity extends AppCompatActivity { // Début de la classe principale

    // Code utilisé pour identifier la demande de permission
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;

    // Clé API OpenWeatherMap (à remplacer par la tienne si besoin)
    private static final String API_KEY = "secret code";

    // Déclaration des vues de l'interface utilisateur
    private TextView textView; // Affichera les infos météo
    private Button button; // Bouton pour déclencher la météo

    // Fournisseur de localisation (fusionnée entre GPS, Wi-Fi, etc.)
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) { // Méthode appelée lors de la création de l’activité
        super.onCreate(savedInstanceState); // Appelle la méthode de la classe parente
        setContentView(R.layout.activity_weather); // Charge le layout XML associé à cette activité

        // Lien entre les composants du layout et le code Java
        textView = findViewById(R.id.textView); // Récupère la zone de texte
        button = findViewById(R.id.button); // Récupère le bouton

        // Initialise le client de localisation fusionnée
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Événement au clic sur le bouton : vérifie les permissions
        button.setOnClickListener(v -> checkLocationPermission());
    }

    // Vérifie si l'autorisation de localisation est accordée
    private void checkLocationPermission() {
        // Si la permission n’est pas encore accordée
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Demande la permission à l’utilisateur
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE
            );
        } else {
            // Sinon, permission déjà accordée → on récupère la localisation
            fetchLocation();
        }
    }

    // Résultat de la demande de permission
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Si la permission est accordée
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            fetchLocation(); // Récupère la localisation
        } else {
            // Sinon, permission refusée → message à l’utilisateur
            Toast.makeText(this, "Permission refusée", Toast.LENGTH_SHORT).show();
        }
    }

    // Récupère la localisation GPS actuelle de l'utilisateur
    @SuppressLint("MissingPermission") // Ignore l'avertissement car on vérifie les permissions avant
    private void fetchLocation() {
        fusedLocationClient.getLastLocation() // Demande la dernière localisation connue
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        // Récupère la latitude et la longitude
                        double lat = location.getLatitude();
                        double lon = location.getLongitude();

                        // Crée l’URL de requête à l’API météo
                        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&units=metric&appid=" + API_KEY;

                        // Lance la requête HTTP
                        fetchWeatherData(url);
                    } else {
                        // Si localisation non trouvée
                        textView.setText("Impossible de récupérer la localisation");
                    }
                })
                .addOnFailureListener(e -> {
                    // En cas d’échec de localisation
                    Toast.makeText(this, "Erreur de localisation", Toast.LENGTH_SHORT).show();
                });
    }

    // Récupère et traite les données météo via HTTP
    private void fetchWeatherData(String url) {
        // Crée une requête GET avec Volley
        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        // Convertit la réponse JSON
                        JSONObject json = new JSONObject(response);
                        // Récupère la température et le nom de la ville
                        String temp = json.getJSONObject("main").getString("temp");
                        String city = json.getString("name");

                        // Affiche la météo dans le TextView
                        textView.setText(temp + "°C à " + city);
                    } catch (Exception e) {
                        // En cas d’erreur dans le traitement des données
                        textView.setText("Erreur de lecture météo");
                        e.printStackTrace(); // Affiche l'erreur dans la console
                    }
                },
                error -> {
                    // En cas d’erreur réseau
                    textView.setText("Erreur réseau");
                    error.printStackTrace();
                });

        // Ajoute la requête à la file de requêtes Volley pour exécution
        // Exécute la requête HTTP (vers l'API météo) en utilisant Volley
        Volley.newRequestQueue(this).add(request);
    }
}

//volley Une bibliothèque Android qui permet de faire facilement des requêtes HTTP (GET, POST...) sans tout coder soi-même.

//json Format de données (comme un dictionnaire) utilisé par les API pour envoyer des données lisibles
