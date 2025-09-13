package com.example.miniprojet;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
// Import de classes pour les RecyclerView
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
// Classe personnalisée ArticleAdapter qui gère l'affichage d'une liste d'articles dans une RecyclerView
public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {
    private static final String TAG = "ArticleAdapter";

    // Liste des articles à afficher
    private ArrayList<NewsArticle> mArrayList;

    // Contexte utilisé pour accéder aux ressources, layouts, etc.
    private Context mContext;


    // Constructeur qui reçoit le contexte et la liste des articles
    public ArticleAdapter(Context context, ArrayList<NewsArticle> list) {
        this.mContext = context; // On stocke le contexte
        this.mArrayList = list; // On stocke la liste des articles
    }


    // Méthode appelée pour créer une nouvelle vue (appelée quand un nouvel item est nécessaire)
    @NonNull
    @Override

    //C’est une méthode du RecyclerView.Adapter. Elle est appelée quand Android a besoin de créer une nouvelle case/item
    // dans la liste (par exemple quand tu scrolles vers le bas).
    // Elle retourne un ViewHolder, c’est-à-dire un objet qui contient les éléments visuels de chaque article (titre, image, etc.).
    // Le paramètre parent est le conteneur qui va contenir cet item (en général c’est ta RecyclerView).

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // On "inflate" (gonfle) le layout XML qui représente un article
        //LayoutInflater.from(mContext) → crée un "gonfleur" de layout avec le contexte actuel.
        //.inflate(...) → il prend le fichier article_item.xml (design XML d’un article) et le transforme en objet View.
        //parent → c’est la vue parente, ici la RecyclerView.
        //false → on indique qu’on n’attache pas encore cette vue au parent tout de suite
        // (c’est fait automatiquement ensuite).
        View view = LayoutInflater.from(mContext).inflate(R.layout.article_item, parent, false);
        // On retourne un nouveau ViewHolder contenant cette vue
        return new ViewHolder(view);
    }

    @Override
    // Méthode appelée pour remplir les données dans une vue spécifique
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // On récupère l'article à la position indiquée
        //position → c’est l’index de la ligne actuelle (par exemple : 0 pour la 1ère, 1 pour la 2ème…).
        //Donc ici, on récupère l’article correspondant
        // à cette position pour afficher ses infos (titre, description, etc).
        NewsArticle currentArticle = mArrayList.get(position);

        // On met à jour le texte du titre dans la vue
        holder.title.setText(currentArticle.getTitle());
        // On met à jour la description
        holder.description.setText(currentArticle.getDescription());
        // On affiche l’auteur et la date (on extrait uniquement la date avec substring)
        holder.contributordate.setText(currentArticle.getAuthor() + " | " + currentArticle.getPublishedAt().substring(0, 10));
        /////////////
        Glide.with(mContext)
                .load(currentArticle.getUrlToImage())
                .placeholder(R.drawable.ic_news)
                .error(R.drawable.ic_news)
                .into(holder.image);
        holder.image.setContentDescription(currentArticle.getContent()); // This line should work


        // On définit ce qui se passe quand l’utilisateur clique sur un article
        holder.itemView.setOnClickListener(v -> {
            // On crée une nouvelle intention pour ouvrir WebActivity
            Intent intent = new Intent(mContext, WebActivity.class);


            // On passe l’URL de l’article en extra
            //put extra permet de passer des données d’une activité à une autre.
            intent.putExtra("url_key", currentArticle.getUrl());
            // On indique que cette activité doit être lancée dans une nouvelle tâche
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // On lance l’activité
            mContext.startActivity(intent);
        });
    }
    // Retourne le nombre total d’éléments à afficher dans la RecyclerView
    @Override
    public int getItemCount() {
        return mArrayList.size();
    }


    // Classe interne ViewHolder : contient les vues de chaque item/article
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Déclaration des vues pour le titre, la description, l’image et l’auteur/date
        private TextView title, description, contributordate;

        private ImageView image; // Ensure this is ImageView


        // Constructeur du ViewHolder qui lie les vues XML à leurs variables
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title_id);// Titre de l’article
            description = itemView.findViewById(R.id.description_id);// Description de l’article
            image = itemView.findViewById(R.id.image_id); // Ensure ID matches article_item.xml
            contributordate = itemView.findViewById(R.id.contributordate_id); // Auteur + date
        }
    }
}