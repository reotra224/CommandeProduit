package gn.traore.commandeproduit;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import gn.traore.commandeproduit.apis.ApiCommande;
import gn.traore.commandeproduit.model.Client;

import static gn.traore.commandeproduit.MainActivity.fragmentManager;

public class AccueilFragment extends Fragment {

    private ArrayList<String> identifiants;
    private Button btnListProduits, btnReglages, btnPanier, btnYoutube, btnSupport, btnSante;

    public AccueilFragment() {
        // Required empty public constructor
    }

    public static AccueilFragment getInstance(ArrayList<String> identifiants) {
        AccueilFragment accueilFragment = new AccueilFragment();
        Bundle args = new Bundle();
        args.putStringArrayList("IDENTIFIANTS", identifiants);
        accueilFragment.setArguments(args);
        return accueilFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_accueil, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //On récupère les éléments du fragment
        btnListProduits = view.findViewById(R.id.btnListeProduits);
        btnPanier = view.findViewById(R.id.btnPanier);
        btnReglages = view.findViewById(R.id.btnReglages);
        btnSante = view.findViewById(R.id.btnSante);
        btnSupport = view.findViewById(R.id.btnSupport);
        btnYoutube = view.findViewById(R.id.btnYoutube);

        //On récupère les identifiants
        Bundle args = getArguments();
        if (args != null && args.containsKey("IDENTIFIANTS")) {
            identifiants = args.getStringArrayList("IDENTIFIANTS");
            //Toast.makeText(view.getContext(), "Phone= " + identifiants.get(0) + " Token= " + identifiants.get(1), Toast.LENGTH_LONG).show();
        }

        //On ajoute les évenements sur les boutons
        ajouterEventSurBoutons(view);
    }

    /**
     * Permet d'ajouter des évenements sur les boutons de l'accueil
     * @param view le fragment Accueil
     */
    private void ajouterEventSurBoutons(View view) {
        //Le bouton liste des produits
        btnListProduits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //On récupère les produits dépuis le BACK et on lance
                // l'activité GestionProduit.
                new ApiCommande(view.getContext(), identifiants.get(0), 0)
                        .execute(identifiants.get(1));
            }
        });

        //Le bouton Panier
        btnPanier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyAdapter.paniers.size() > 0) {
                    Intent intent = new Intent(view.getContext(), Panier.class);
                    //On converti le produit en json
                    Gson gson = new Gson();
                    String produitsPanier = gson.toJson(MyAdapter.paniers);
                    intent.putExtra("LISTE_PRODUITS_PANIER", produitsPanier);
                    startActivity(intent);
                } else {
                    Toast.makeText(view.getContext(), "Votre panier est vide !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Le bouton Reglages
        btnReglages.setOnClickListener(new View.OnClickListener() {
            Client client;
            @Override
            public void onClick(View view) {
                //On récupère les informations du clients
                client = recupInfoClient(view);
                if (client != null) {
                    //On converti l'instance en json pour pouvoir le passer au fragment Login
                    Gson gson = new Gson();
                    String clientJson = gson.toJson(client);
                    //On récupère une instance du fragment Login en lui passant les informations du client
                    LoginFragment loginFragment = LoginFragment.getInstance(clientJson);
                    //Ensuite on lance le fragment Login
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainer, loginFragment)
                            .commit();
                }
            }
        });

        //Le support
        btnSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), SupportActivity.class);
                view.getContext().startActivity(intent);
            }
        });

    }

    /**
     * Permet de récupérer les information du client.
     * @param view le fragment Accueil.
     * @return une instance du client.
     */
    private Client recupInfoClient(View view) {
        String[] result;
        Client client = null;
        try {
            //On ouvre le fichier identifiant
            InputStream inputStream = view.getContext().openFileInput("identifiants");

            //Si le fichier existe
            if (inputStream != null) {
                //On crée le lecteur du fichier
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                //Variable stockant les lignes du fichier
                String ligne;
                StringBuilder buf = new StringBuilder();
                //On lit le fichier
                while ((ligne = reader.readLine()) != null) {
                    buf.append(ligne + "\r\n");
                }
                //On ferme le flux
                reader.close();
                inputStream.close();
                //On récupère le contenu du fichier lu
                result = buf.toString().split(";");
                //Si on a bien récupéré les infos
                if (result.length > 0) {
                    //On crée une instance du client à retourner
                    client = new Client(
                            result[0],
                            result[1],
                            result[2],
                            result[3],
                            result[4],
                            result[5]
                    );
                }
            }
        }catch (FileNotFoundException e) {
            Toast.makeText(view.getContext(), "FileNotFoundException", Toast.LENGTH_LONG);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return client;
    }

}
