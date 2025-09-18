package com.example.miniprojet;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;//Volley : Utilisé pour effectuer des requêtes HTTP et récupérer les articles au format JSON.
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity {
    // Déclaration de la clé API pour l'accès à l'API des actualités
    private static final String API_KEY = "secret code"; // Replace with a valid NewsAPI key
    // Déclaration des variables pour la vue et les données
    private ProgressBar mProgressBar;// ProgressBar pour afficher l'état de chargement
    private RecyclerView mRecyclerView;// RecyclerView pour afficher la liste des articles
    private ArrayList<NewsArticle> mArticleList;// Liste des articles à afficher
    private ArticleAdapter mArticleAdapter;// Adaptateur pour lier les articles au RecyclerView
    private com.android.volley.RequestQueue mRequestQueue;// File d'attente pour les requêtes Volley une bibliothèque de requêtes HTTP  pour gérer les requêtes réseau.
    // C’est elle qui envoie les requêtes HTTP et gère les réponses
    private NewsDatabase mDatabase;// Base de données pour stocker les articles

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);// Définit le layout pour l'activité principale
// Initialisation des vues (ProgressBar, RecyclerView)
        mProgressBar = findViewById(R.id.progressbar_id);
        mRecyclerView = findViewById(R.id.recyclerview_id);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));// Définit un layout manager pour le RecyclerView

        mArticleList = new ArrayList<>();// Initialisation de la liste des articles
        mArticleAdapter = new ArticleAdapter(getApplicationContext(), mArticleList);
        mRecyclerView.setAdapter(mArticleAdapter);

        mRequestQueue = Volley.newRequestQueue(this);// Création d'une nouvelle file d'attente pour les requêtes Volley
        mDatabase = NewsDatabase.getInstance(this);// Initialisation de la base de données
// Bouton pour récupérer les articles à partir de l'API
        Button fetchNewsButton = findViewById(R.id.fetchNewsButton);
        fetchNewsButton.setOnClickListener(v -> getNewsFromApi());// Lors du clic, appeler la méthode pour récupérer les articles

        loadArticlesFromDatabase();// Charger les articles depuis la base de données
    }
    // Méthode pour récupérer les articles de l'API
    private void getNewsFromApi() {
        mArticleList.clear();// Vider la liste des articles avant de la remplir à nouveau
        mArticleAdapter.notifyDataSetChanged();
        mProgressBar.setVisibility(View.VISIBLE);// Afficher la ProgressBar pendant le chargement
// URL de l'API avec les paramètres nécessaires
        String url = "https://newsapi.org/v2/everything?q=tesla&sortBy=publishedAt&pageSize=50&apiKey=" + API_KEY;

// Création d'une requête JSON avec Volley pour obtenir des articlespar l api
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,// on utilise la méthode HTTP GET (pour lire des données). l’URL de l’API à appeler. on n’envoie pas de corps JSON (car GET ne le nécessite pas).
                response -> {
                    mProgressBar.setVisibility(View.GONE); // Cacher la ProgressBar après la réponse

                    try {
                        int totalResults = response.getInt("totalResults");// Obtenir le nombre total de résultats
                        if (totalResults == 0) {// Si aucun article n'est trouvé
                            Toast.makeText(this, "No articles found", Toast.LENGTH_LONG).show();
                            return;
                        }
                        JSONArray articles = response.getJSONArray("articles");// Obtenir le tableau des articles de la réponse JSON
                        Log.d("NewsActivity", "Articles fetched: " + articles.length());
                        // Exécuter le traitement des articles dans un thread séparé
                        new Thread(() -> {
                            mDatabase.newsArticleDao().deleteAll();// Supprimer tous les articles existants dans la base de données  Avant d’ajouter les nouveaux articles
                            for (int j = 0; j < articles.length(); j++) {
                                try {
                                    JSONObject article = articles.getJSONObject(j);// Récupérer chaque article JSON
                                    NewsArticle currentArticle = new NewsArticle();// Créer un nouvel objet NewsArticle

                                    // Extraire les informations de chaque article en gérant les valeurs nulles avec optString
                                    currentArticle.setAuthor(article.optString("author", "Unknown"));
                                    currentArticle.setTitle(article.optString("title", ""));
                                    currentArticle.setDescription(article.optString("description", ""));
                                    currentArticle.setUrl(article.optString("url", ""));
                                    currentArticle.setUrlToImage(article.optString("urlToImage", ""));
                                    currentArticle.setPublishedAt(article.optString("publishedAt", ""));
                                    currentArticle.setContent(article.optString("content", ""));
                                    mDatabase.newsArticleDao().insert(currentArticle);// Insérer l'article dans la base de données
                                    mArticleList.add(currentArticle); // Ajouter l'article à la liste
                                } catch (JSONException e) {
                                    e.printStackTrace();// Gérer l'exception JSON si un article ne peut pas être traité
                                }
                            }
                            // Mettre à jour l'UI dans le thread principal après l'ajout des articles
                            runOnUiThread(() -> mArticleAdapter.notifyDataSetChanged());// Notifier que les données ont changé
                        }).start();
                    } catch (JSONException e) {
                        e.printStackTrace();// Gérer les erreurs JSON
                        Toast.makeText(this, "Error parsing data", Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    mProgressBar.setVisibility(View.GONE);// Cacher la ProgressBar en cas d'erreur
                    String errorMsg = "Network error: " + error.toString();
                    Log.e("NewsActivity", errorMsg, error);
                    Toast.makeText(this, "Failed to fetch news", Toast.LENGTH_LONG).show(); // Afficher un message d'erreur
                }
        );
        // Configuration de la politique de réessai pour la requête (par exemple, délai d'attente de 30 secondes)
        //7aja ta3tihelna volley pour la réessai automatique,probleme(La connexion internet soit lente,//Le serveur mette du temps à répondre,//Il y ait une erreur temporaire...)
        jsonObjectRequest.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(
                30000,// Timeout en millisecondes
                com.android.volley.DefaultRetryPolicy.DEFAULT_MAX_RETRIES, // Nombre de tentatives de réessai
                com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));// Facteur de multiplication pour les réessais

        mRequestQueue.add(jsonObjectRequest);// Ajouter la requête à la file d'attente pour l'exécuter
    }
    // Méthode pour charger les articles depuis la base de données locale
    private void loadArticlesFromDatabase() {
        new Thread(() -> {
            mArticleList.clear();// Vider la liste des articles
            List<NewsArticle> articles = mDatabase.newsArticleDao().getAllArticles();// Charger les articles depuis la base de données
            // Mettre à jour l'UI dans le thread principal
            Log.d("NewsActivity", "Articles loaded from DB: " + articles.size());
            mArticleList.addAll(articles);
            runOnUiThread(() -> {
                mArticleAdapter.notifyDataSetChanged();// Notifier que les données ont changé
                if (mArticleList.isEmpty()) {
                    Toast.makeText(NewsActivity.this, "No articles available. Tap 'Fetch News' to load articles.", Toast.LENGTH_LONG).show();
                }
            });
        }).start();// Exécuter la tâche dans un thread séparé
    }
}

//Ton application a besoin de récupérer des articles depuis une API (NewsAPI) à travers Internet.
//Mais Android n’autorise pas de faire une requête réseau directement sur le thread principal
// (celui de l’interface utilisateur), car ça bloquerait l'application si la réponse est lente ou si
// la connexion est mauvaise.

//Dans ce fichier, tu récupères des données au format JSON depuis une API (https://newsapi.org).
//Le JSON, c’est juste un format de données léger et structuré, très utilisé pour échanger des données entre un serveur et une app.
