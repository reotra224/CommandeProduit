package gn.traore.commandeproduit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import gn.traore.commandeproduit.apis.ApiCommande;
import gn.traore.commandeproduit.model.ProduitDTO;
import gn.traore.commandeproduit.model.ProduitPanier;

import static gn.traore.commandeproduit.MainActivity.fragmentManager;

public class Panier extends AppCompatActivity {

    private static TextView txViewMntTotal;
    private MyAdapterPanier myAdapterPanier;
    private static List<ProduitPanier> produitPaniers = new ArrayList<>();
    private static Double mntTotalPanier = 0.0;
    private Button btnCommande, btnAnnuler;
    private static String phone, token;

    private RecyclerView recyclerViewPanier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panier);

        recyclerViewPanier = findViewById(R.id.recyclerViewPanier);
        txViewMntTotal = findViewById(R.id.mntTotalPanier);
        btnCommande = findViewById(R.id.btnCommandePanier);
        btnAnnuler = findViewById(R.id.btnAnnulerPanier);

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

        //Ajout des évènements sur les boutons
        ajouterEvenementSurBouton();

    }

    private void ajouterEvenementSurBouton() {

        //Annulation de la commande
        btnAnnuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // On crée la fenetre de confirmation avant de vider le panier
                AlertDialog.Builder builder = new AlertDialog.Builder(Panier.this);
                builder.setTitle("Confirmation d'annulation de commande")
                        .setMessage("Êtes vous sûre de vouloir annnuler votre commande ?")
                        .setPositiveButton("OUI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(Panier.this, "Commande annulée avec succèss ",
                                        Toast.LENGTH_SHORT).show();
                                //On néttoie le panier
                                Panier.nettoyerPanier(Panier.this);
                            }
                        })
                        .setNegativeButton("NON", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .create().show();
            }
        });

        //Validation de la commande
        btnCommande.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //On vérifie qu'il a des produits dans le panier
                if (produitPaniers.size() > 0) {
                    //On récupère
                    //Ensuite on forme les données à envoyer à l'API
                    ArrayList<ProduitDTO> produitDtos = new ArrayList<>();
                    for (ProduitPanier p : produitPaniers) {
                        //On crée une instance du DTO et on l'ajoute à la liste
                        produitDtos.add(new ProduitDTO(
                                p.getProduit_panier().getId(),
                                p.getProduit_panier().getPrix(),
                                p.getQuantite_produit_panier(),
                                phone, mntTotalPanier
                        ));
                    }
                    //On converti les données en JSON
                    Gson gson = new Gson();
                    String produitDtoJson = gson.toJson(produitDtos);
                    //On envoie les commandes à l'API
                    new ApiCommande(Panier.this, phone, 1)
                            .execute(produitDtoJson, token);
                }
            }
        });
    }

    @Override
    protected void onRestart() {
        //Si le panier est vide, on retourne dans la boutique
        if (produitPaniers.size() == 0) {
            Toast.makeText(this, "Votre panier est vide !", Toast.LENGTH_SHORT).show();
            // S'il n'y a plus de produit dans le panier
            // On retourne dans la boutique :)
            MyAdapter.nbreProduit = 0;

            //On lance le fragment Accueil
            Intent intent = new Intent(this, MainActivity.class);
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
        //On récupère les identifiants du client
        if (getIntent().hasExtra("TOKEN") && getIntent().hasExtra("PHONE")) {
            token = getIntent().getStringExtra("TOKEN");
            phone = getIntent().getStringExtra("PHONE");
        }
    }
    /**
     * Permet de calculer le montant total du panier
     * @param produitPaniers les produits du panier
     */
    private void calculMontantTotal(@NonNull List<ProduitPanier> produitPaniers) {
        mntTotalPanier = 0.0;
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

    /**
     * Methode permettant de nettoyer le panier.
     * @param context le context à partir duquel elle sera appellée.
     */
    public static void nettoyerPanier(Context context) {
        //On vide la liste des produits du panier et de l'adapter
        produitPaniers.clear();
        MyAdapter.paniers.clear();
        //On initialise le montant total du panier
        mntTotalPanier = 0.0;
        afficheMontantTotalDuPanier(mntTotalPanier);
        //On initialise le nombre de produit sélectionné
        MyAdapter.nbreProduit = 0;
        GestionProduit.btnNbreProdSelect.setText(String.valueOf(MyAdapter.nbreProduit));
        //On récupère les produits dépuis le BACK et on lance
        // l'activité GestionProduit.
        new ApiCommande(context, phone, 0)
                .execute(token);
    }
}
