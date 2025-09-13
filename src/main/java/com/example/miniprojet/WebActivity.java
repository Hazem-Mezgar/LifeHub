package com.example.miniprojet;

import android.content.Intent;  // Importation de la classe Intent, utilisée pour récupérer l'URL passée par l'activité précédente
import android.os.Bundle;  // Importation de la classe Bundle, utilisée pour stocker les données de l'activité
import android.webkit.WebView;  // Importation de la classe WebView, utilisée pour afficher une page web dans l'application
import androidx.appcompat.app.AppCompatActivity;  // Importation de la classe AppCompatActivity, permettant de gérer les activités avec les composants modernes d'Android

public class WebActivity extends AppCompatActivity {  // Déclaration de la classe WebActivity qui hérite d'AppCompatActivity
    private WebView myWebView;  // Déclaration d'une variable pour notre WebView

    @Override
    protected void onCreate(Bundle savedInstanceState) {  // Méthode appelée lors de la création de l'activité
        super.onCreate(savedInstanceState);  // Appel du constructeur de la classe parent pour initialiser l'activité
        setContentView(R.layout.activity_web);  // Définition du layout de l'activité (fichier XML contenant les éléments visuels, ici le WebView)

        myWebView = findViewById(R.id.webview);  // Initialisation de la variable myWebView avec le WebView défini dans le layout XML
        Intent intent = getIntent();  // Récupération de l'intent qui a démarré l'activité, contenant les données passées

        if (intent != null) {  // Vérification que l'intent n'est pas nul (pour s'assurer que des données ont bien été envoyées)
            String url = intent.getStringExtra("url_key");  // Récupération de l'URL envoyée dans l'intent via la clé "url_key"
            if (url != null && !url.isEmpty()) {  // Vérification que l'URL n'est pas nulle ni vide
                myWebView.loadUrl(url);  // Si l'URL est valide, charger l'URL dans le WebView
            }
        }
    }
}
