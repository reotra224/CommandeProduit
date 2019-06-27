package gn.traore.commandeproduit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import gn.traore.commandeproduit.model.ProduitPanier;

public class Panier extends AppCompatActivity {

    private static TextView txViewMntTotal;
    private MyAdapterPanier myAdapterPanier;
    private List<ProduitPanier> produitPaniers = new ArrayList<>();
    private Double mntTotal = 0.0;

    private RecyclerView recyclerViewPanier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panier);

        recyclerViewPanier = findViewById(R.id.recyclerViewPanier);
        txViewMntTotal = findViewById(R.id.mntTotalPanier);

        //On récupère les produits du panier
        recupProduitPanier();

        //On affiche le montant total du panier
        txViewMntTotal.setText(String.valueOf(mntTotal) + " FCFA");

        //On créer une instance de l'adapter
        myAdapterPanier = new MyAdapterPanier(produitPaniers, this);

        //définit l'agencement des cellules, ici de façon verticale, comme une ListView
        recyclerViewPanier.setLayoutManager(new LinearLayoutManager(this));

        //Puis on spécifit l'adapter du recyclerView
        recyclerViewPanier.setAdapter(myAdapterPanier);
    }

    private void recupProduitPanier() {
        if (getIntent().hasExtra("LISTE_PRODUITS_PANIER")) {
            String json = getIntent().getStringExtra("LISTE_PRODUITS_PANIER");
            //On désérialize la liste des produits du panier
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<ProduitPanier>>() {}.getType();
            produitPaniers = gson.fromJson(json, type);
            //On calcul le montant total du panier
            calculMontantTotal(produitPaniers);
        }
    }

    // Calcul du Montant total du panier
    private void calculMontantTotal(@NonNull List<ProduitPanier> produitPaniers) {
        for (ProduitPanier prod : produitPaniers) {
            // On calcul le total de chaque produit et on l'ajoute au Montant
            // total du panier
            mntTotal += prod.getQuantite_produit_panier() * prod.getProduit_panier().getPrix();
        }
    }
}
