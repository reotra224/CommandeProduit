package gn.traore.commandeproduit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import gn.traore.commandeproduit.model.ProduitPanier;

public class Panier extends AppCompatActivity {

    private static TextView txViewMntTotal;
    private MyAdapterPanier myAdapterPanier;
    private static List<ProduitPanier> produitPaniers = new ArrayList<>();
    private static Double mntTotalPanier = 0.0;

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
        afficheMontantTotalDuPanier(mntTotalPanier);

        //On créer une instance de l'adapter
        myAdapterPanier = new MyAdapterPanier(produitPaniers, this);

        //définit l'agencement des cellules, ici de façon verticale, comme une ListView
        recyclerViewPanier.setLayoutManager(new LinearLayoutManager(this));

        //Puis on spécifit l'adapter du recyclerView
        recyclerViewPanier.setAdapter(myAdapterPanier);
    }

    @Override
    protected void onRestart() {
        //Si le panier est vide, on retourne dans la boutique
        if (produitPaniers.size() == 0) {
            Toast.makeText(this, "Votre panier est vide !", Toast.LENGTH_SHORT).show();
            // S'il n'y a plus de produit dans le panier
            // On retourne dans la boutique :)
            MyAdapter.nbreProduit = 0;
            Intent intent = new Intent(this, GestionProduit.class);
            startActivity(intent);
        }
        super.onRestart();
    }

    private static void afficheMontantTotalDuPanier(Double mntTotal) {
        txViewMntTotal.setText(String.valueOf(mntTotal) + " CFA");
    }

    /**
     * Permet de récupérer les produits du panier
     */
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

    /**
     * Permet de calculer le montant total du panier
     * @param produitPaniers les produits du panier
     */
    private void calculMontantTotal(@NonNull List<ProduitPanier> produitPaniers) {
        for (ProduitPanier prod : produitPaniers) {
            // On calcul le total de chaque produit et on l'ajoute au Montant
            // total du panier
            mntTotalPanier += prod.getQuantite_produit_panier() * prod.getProduit_panier().getPrix();
        }
    }

    /**
     * Permet de supprimer un produit du panier
     * @param position la position du produit a supprimer
     */
    public static void finirSuppression(int position) {
        if (produitPaniers != null || produitPaniers.size() > 0) {
            //Calcul du montant total du produit à supprimer
            ProduitPanier prod = produitPaniers.get(position);
            Double mntProduitASupprimer = prod.getProduit_panier().getPrix() *
                    prod.getQuantite_produit_panier();
            //Déduction du montant calculé du montant total du panier
            mntTotalPanier -= mntProduitASupprimer;

            //On supprime le produit de la liste des produits du panier
            produitPaniers.remove(position);
        }

        //On décrémente le nombre de produit sélectionné
        GestionProduit.btnNbreProdSelect.setText(String.valueOf(--MyAdapter.nbreProduit));

        //On modifie le montant total affiché
        afficheMontantTotalDuPanier(mntTotalPanier);

        //On supprime le produit de la liste des produits du panier
        if (MyAdapter.paniers != null || MyAdapter.paniers.size() > 0) {
            MyAdapter.paniers.remove(position);
        }

    }
}
