package gn.traore.commandeproduit;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import gn.traore.commandeproduit.model.Produit;
import gn.traore.commandeproduit.model.ProduitPanier;

public class MyAdapterPanier extends RecyclerView.Adapter<MyAdapterPanier.ViewHolderPanier> {

    private List<ProduitPanier> listProduitsPanier = new ArrayList<>();
    private Context context;

    public MyAdapterPanier(List<ProduitPanier> list, Context context) {
        this.listProduitsPanier = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolderPanier onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.produit_card_panier, parent, false);
        return new ViewHolderPanier(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPanier holder, final int position) {
        final ProduitPanier produitPanier = listProduitsPanier.get(position);
        //On remplie le recyclerView
        holder.bind(produitPanier);
        holder.btnSupprimerProduitPanier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Notifier le recyclerView pour qu'il actualise la liste
                notifyItemRemoved(position);
                notifyItemChanged(position, getItemCount());

                Panier.finirSuppression(position);

                /*Toast.makeText(context,produitPanier.getProduit_panier().getTitre() + ": Position = " + position + " Et Size = " + getItemCount(),
                        Toast.LENGTH_SHORT).show();*/
                //Si le panier est vide, on retourne dans la boutique
                if (getItemCount() == 0) {
                    // S'il n'y a plus de produit dans le panier
                    // On retourne dans la boutique :)
                    MyAdapter.nbreProduit = 0;
                    Intent intent = new Intent(context, GestionProduit.class);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listProduitsPanier.size();
    }

    public class ViewHolderPanier extends RecyclerView.ViewHolder {

        private ImageView imgProduitPanier;
        private TextView nomProduitPanier, prixProduitPanier, qteProduitPanier;
        private Button btnSupprimerProduitPanier;

        public ViewHolderPanier(@NonNull View itemView) {
            super(itemView);

            //On récupère les éléments de la card
            imgProduitPanier = itemView.findViewById(R.id.img_produit_panier);
            nomProduitPanier = itemView.findViewById(R.id.nom_produit);
            prixProduitPanier = itemView.findViewById(R.id.prix_produit);
            qteProduitPanier = itemView.findViewById(R.id.qte_produit);
            btnSupprimerProduitPanier = itemView.findViewById(R.id.btnSupProduitPanier);
        }

        /**
         * Fonction permettant de remplir la cardView du Panier
         * @param p Le produit à afficher
         */
        public void bind(ProduitPanier p) {
            nomProduitPanier.setText(p.getProduit_panier().getTitre());
            prixProduitPanier.setText("Prix: " + String.valueOf(p.getProduit_panier().getPrix()) + " CFA");
            imgProduitPanier.setImageResource(p.getProduit_panier().getImage());
            qteProduitPanier.setText("Quantité: " + String.valueOf(p.getQuantite_produit_panier()));
        }
    }
}
