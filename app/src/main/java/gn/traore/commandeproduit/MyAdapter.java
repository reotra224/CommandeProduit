package gn.traore.commandeproduit;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import gn.traore.commandeproduit.model.Produit;
import gn.traore.commandeproduit.model.ProduitPanier;

import static android.content.Context.MODE_PRIVATE;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<Produit> listProduits;
    public static List<ProduitPanier> paniers = new ArrayList<>();
    private Context context;
    private int nbreProduit;

    public MyAdapter(List<Produit> listProduits, Context context) {
        this.listProduits = listProduits;
        this.context = context;
        this.nbreProduit = 0;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.produit_card_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Produit produit = listProduits.get(position);
        holder.bind(produit);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //On ajoute le produit sélectionné dans le panier
                ajoutProduitAuPanier(produit);

                /*// On enregistre le produit dans un fichier partagé servant de panier
                SharedPreferences mPreferences = context.getSharedPreferences("PANIER_PRODUITS", MODE_PRIVATE);
                SharedPreferences.Editor editor = mPreferences.edit();

                //On converti le produit en json
                Gson gson = new Gson();
                String jsonProduit = gson.toJson(produit);

                //On ajoute dans le panier
                editor.putString("PRODUIT", jsonProduit);
                editor.commit();*/

                // On incrémente le nombre de produit à commander
                GestionProduit.btnNbreProdSelect.setText(String.valueOf(++nbreProduit));
            }
        });
    }

    /**
     * Permet d'ajouter un produit dans le panier
     * @param produit
     */
    private void ajoutProduitAuPanier(@NonNull Produit produit) {
        boolean verif = false;
        //On vérifi que le panier n'est pas null
        if (paniers != null || paniers.size() > 0) {
            //On recherche une occurence du produit sélectionné dans le panier
            for (ProduitPanier pp: paniers) {
                //Si on n'en trouve
                if (pp.getProduit_panier().getTitre().equals(produit.getTitre())) {
                    //On augmente la quantité
                    pp.setQuantite_produit_panier(pp.getQuantite_produit_panier() + 1);
                    verif = true;
                    Toast.makeText(context, "Existe déjà dans le panier", Toast.LENGTH_SHORT).show();
                }
            }
            //Si le produit n'existe pas encore dans le panier, on l'ajoute
            if (!verif) {
                //On crée une instance du Produit panier
                ProduitPanier pp = new ProduitPanier(produit, 1);
                //On l'ajoute au panier
                paniers.add(pp);
                Toast.makeText(context, "N'existe pas dans le panier !" + verif, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Pas de panier", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public int getItemCount() {
        return listProduits.size();
    }

    public int getNbreProduit() {
        return nbreProduit;
    }

    /**
     * Classe représentant nos items
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        private ImageView imageView;
        private CardView cardView;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);

            //On reccupère les élements de la cardView
            //textView = itemView.findViewById(R.id.card_text);
            imageView = itemView.findViewById(R.id.card_image);
            cardView = itemView.findViewById(R.id.card_layout);
        }

        /**
         * Fonction permettant de remplir la cardView
         */
        public void bind(Produit produit) {
            //textView.setText(produit.getTitre());
            imageView.setImageResource(R.drawable.parisguidetower);
            //Toast.makeText(itemView.getContext(), "", Toast.LENGTH_LONG).show();
            //imageView.setImageURI(Uri.parse(produit.getImage()));

            //Picasso.with(imageView.getContext()).load(R.drawable.parisguidetower);
            //Picasso.with(imageView.getContext()).load(produit.getImage());
        }

    }
}
