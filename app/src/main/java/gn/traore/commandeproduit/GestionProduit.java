package gn.traore.commandeproduit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import gn.traore.commandeproduit.apis.ApiCommande;
import gn.traore.commandeproduit.model.Produit;
import gn.traore.commandeproduit.model.ProduitDTO;
import gn.traore.commandeproduit.model.ProduitPanier;

public class GestionProduit extends AppCompatActivity {

    private RecyclerView recyclerView;
    public static Button btnNbreProdSelect;
    private Button btnCommande;
    private String phone, token;

    public List<Produit> produits = new ArrayList<>();
    private int typeOperation = 0;

    private MyAdapter myAdapter;
    private Double mntTotalPanier = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_produit);

        //On récupère la liste des produits reçu en argument
        recupArguments();

        //On crée initialise l'adpter avec les produits
        myAdapter = new MyAdapter(produits, this);

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

    /**
     * Permet de récupérer les arguments reçu des autres activités/Fragments
     */
    private void recupArguments() {
        //On récupère la liste des produits
        if (getIntent().hasExtra("LISTE_PRODUITS")) {
            String produitsJson = getIntent().getStringExtra("LISTE_PRODUITS");
            //On désérialize la liste des produits
            JSONObject jsonObject;
            try {
                JSONArray jsonArray = new JSONArray(produitsJson);
                for (int i=0; i<jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    produits.add(new Produit(
                            jsonObject.getInt("id"),
                            jsonObject.getString("matriculation"),
                            jsonObject.getString("nom"),
                            jsonObject.getString("categorie"),
                            jsonObject.getInt("stock"),
                            jsonObject.getString("description"),
                            jsonObject.getDouble("prix"),
                            jsonObject.getString("image")
                    ));
                }
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        //On récupère les identifiants du client
        if (getIntent().hasExtra("TOKEN") && getIntent().hasExtra("PHONE")) {
            token = getIntent().getStringExtra("TOKEN");
            phone = getIntent().getStringExtra("PHONE");
        }
    }

    /**
     * Permet d'ajouter des évenements sur les boutons de l'interface
     */
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
                    intent.putExtra("PHONE", phone);
                    intent.putExtra("TOKEN", token);
                    startActivity(intent);
                } else {
                    Toast.makeText(GestionProduit.this, "Votre panier est vide !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCommande.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //On vérifie qu'il a des produits dans le panier
                if (MyAdapter.paniers.size() > 0) {
                    //On calcul le montant total du Panier
                    calculMontantTotal(MyAdapter.paniers);

                    //Ensuite on forme les données à envoyer à l'API
                    ArrayList<ProduitDTO> produitDtos = new ArrayList<>();
                    for (ProduitPanier p : MyAdapter.paniers) {
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
                    new ApiCommande(GestionProduit.this, phone, 1)
                            .execute(produitDtoJson, token);
                }else {
                    Toast.makeText(GestionProduit.this, "Votre panier est vide !", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
}
