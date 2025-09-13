    package com.example.miniprojet;

    import android.content.Context; // Représente le contexte de l’application
    import android.view.LayoutInflater; // Pour créer une vue à partir d’un fichier XML
    import android.view.View; // Composant d’interface
    import android.view.ViewGroup; // Conteneur de vues
    import android.widget.ArrayAdapter; // Adapter de base pour les listes simples
    import android.widget.TextView; // Affiche une ligne de texte

    import java.util.ArrayList; // Liste dynamique

    // Adapter personnalisé pour afficher chaque événement dans la ListView
    public class EventListAdapter extends ArrayAdapter<String> {

        // Constructeur de l’adapter
        public EventListAdapter(Context context, ArrayList<String> events) {
            super(context, 0, events); // 0 indique qu’on n’utilise pas de layout par défaut
        }


//La fonction getView() dans un ArrayAdapter (comme EventListAdapter dans votre cas) est utilisée
// pour créer et configurer une vue (généralement un élément de liste) qui représente chaque élément de données
// dans une ListView.
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Récupérer l'élément de données (chaîne de texte) à la position actuelle dans la liste
            String event = getItem(position);

            // Si la vue convertView est null, cela signifie qu'il n'y a pas de vue réutilisable,
            // donc on doit en créer une nouvelle
            if (convertView == null) {
                // Créer une nouvelle vue en utilisant le fichier XML de l'élément (item_event.xml)
                // LayoutInflater est utilisé pour "gonfler" le fichier XML en une vue concrète
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_event, parent, false);
            }

            // Récupère la TextView à l'intérieur de convertView pour pouvoir afficher l'événement
            TextView eventText = convertView.findViewById(R.id.eventText);

            // Affiche le texte de l'événement dans la TextView
            eventText.setText(event);

            // Retourne la vue convertView qui sera affichée dans la ListView
            return convertView;
        }
    }
//L'EventListAdapter est une classe importante dans ton application car elle permet de lier les données de ton application
// (les événements) à la vue (l'interface utilisateur) dans ListView.