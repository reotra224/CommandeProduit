package gn.traore.commandeproduit;

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

public class GestionProduit extends AppCompatActivity {

    private RecyclerView recyclerView;
    public static Button btnNbreProdSelect;
    private Button btnCommande;

    public List<Produit> produits = new ArrayList<>();
    private int typeOperation = 0;

    private MyAdapter myAdapter;

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
                            jsonObject.getString("stock")
                    ));
                }
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
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
    /*
    private void reccupProduits(Context context) {
        //On récupère le token reçu
        if (getIntent().hasExtra("IDENTIFIANTS")) {
            ArrayList<String> identifiants = getIntent().getStringArrayListExtra("IDENTIFIANTS");
            //Toast.makeText(context, identifiants.get(1), Toast.LENGTH_LONG).show();
            new ApiCommande(context).execute(identifiants.get(1));
        }
    }*/
}
