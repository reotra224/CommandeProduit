package gn.traore.commandeproduit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    public static Button btnNbreProdSelect;
    private Button btnCommande;

    private List<Produit> cities = new ArrayList<>();

    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //remplir la ville
        ajouterVilles();

        //On crée initialise l'adpter avec les produits
        myAdapter = new MyAdapter(cities, this);

        recyclerView = findViewById(R.id.recyclerView);
        btnNbreProdSelect = findViewById(R.id.btnNbreProd);
        btnNbreProdSelect.setText(String.valueOf(myAdapter.getNbreProduit()));

        //définit l'agencement des cellules, ici de façon verticale, comme une ListView
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //pour adapter en grille comme une RecyclerView, avec 2 cellules par ligne
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        //puis créer un MyAdapter, lui fournir notre liste de villes.
        //cet adapter servira à remplir notre recyclerview
        recyclerView.setAdapter(myAdapter);
    }

    @Override
    protected void onPostResume() {
        btnNbreProdSelect.setText(String.valueOf(myAdapter.getNbreProduit()));
        super.onPostResume();
    }

    private void ajouterVilles() {
        cities.add(new Produit("France","http://www.telegraph.co.uk/travel/destination/article130148.ece/ALTERNATES/w620/parisguidetower.jpg"));
        cities.add(new Produit("Angleterre","http://www.traditours.com/images/Photos%20Angleterre/ForumLondonBridge.jpg"));
        cities.add(new Produit("Allemagne","http://tanned-allemagne.com/wp-content/uploads/2012/10/pano_rathaus_1280.jpg"));
        cities.add(new Produit("Espagne","http://www.sejour-linguistique-lec.fr/wp-content/uploads/espagne-02.jpg"));
        cities.add(new Produit("Italie","http://retouralinnocence.com/wp-content/uploads/2013/05/Hotel-en-Italie-pour-les-Vacances2.jpg"));
        cities.add(new Produit("Russie","http://www.choisir-ma-destination.com/uploads/_large_russie-moscou2.jpg"));
    }

}
