package gn.traore.commandeproduit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import gn.traore.commandeproduit.model.Produit;

public class GestionProduit extends AppCompatActivity {

    private RecyclerView recyclerView;
    public static Button btnNbreProdSelect;
    private Button btnCommande;

    private List<Produit> cities = new ArrayList<>();

    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_produit);

        //remplir la ville
        ajouterVilles();

        //On crée initialise l'adpter avec les produits
        myAdapter = new MyAdapter(cities, this);

        recyclerView = findViewById(R.id.recyclerView);
        btnNbreProdSelect = findViewById(R.id.btnNbreProd);
        btnNbreProdSelect.setText(String.valueOf(MyAdapter.nbreProduit));
        btnCommande = findViewById(R.id.btnCommande);

        //On ajoute un OnClickListener sur les boutons
        ajoutOnClickListenerSurLesBoutons();

        //définit l'agencement des cellules, ici de façon verticale, comme une ListView
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //pour adapter en grille comme une RecyclerView, avec 2 cellules par ligne
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        //cet adapter servira à remplir notre recyclerview
        recyclerView.setAdapter(myAdapter);
    }

    private void ajoutOnClickListenerSurLesBoutons() {
        btnNbreProdSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (MyAdapter.paniers.size() > 0) {
                    Intent intent = new Intent(GestionProduit.this, Panier.class);
                    //On converti le produit en json
                    Gson gson = new Gson();
                    String produitsPanier = gson.toJson(MyAdapter.paniers);
                    intent.putExtra("LISTE_PRODUITS_PANIER", produitsPanier);
                    startActivity(intent);
                } else {
                    Toast.makeText(GestionProduit.this, "Votre panier est vide !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCommande.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(GestionProduit.this, "Passer la commande !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ajouterVilles() {
        cities.add(new Produit("Crême Lavante", R.drawable.creme_lavante_hypoallergenique_pour_les_mains, 5500.0));
        cities.add(new Produit("Léssive liquide", R.drawable.lessive_liquide_ecocert_5l, 6600.0));
        cities.add(new Produit("Désinfectant", R.drawable.nettoyant_multi_surfaces_desinfectant, 7800.0));
        cities.add(new Produit("Savon antiseptique", R.drawable.savon_antiseptique, 9000.0));
        cities.add(new Produit("Savon doux", R.drawable.savon_doux_anios, 1500.0));
        cities.add(new Produit("Savon main",R.drawable.savon_main_antiseptique, 3500.0));
    }

}
