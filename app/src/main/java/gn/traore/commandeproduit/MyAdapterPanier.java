package gn.traore.commandeproduit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    public void onBindViewHolder(@NonNull ViewHolderPanier holder, int position) {
        if (listProduitsPanier.size() > 0) {
            Toast.makeText(context, "Pas vide !", Toast.LENGTH_SHORT).show();
        }
        final ProduitPanier produitPanier = listProduitsPanier.get(position);
        //On remplie le recyclerView
        holder.bind(produitPanier.getProduit_panier());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolderPanier extends RecyclerView.ViewHolder {

        private ImageView imgProduitPanier;
        private TextView nomProduitPanier, prixProduitPanier, descProduitPanier;

        public ViewHolderPanier(@NonNull View itemView) {
            super(itemView);

            //On récupère les éléments de la card
            imgProduitPanier = itemView.findViewById(R.id.img_produit_panier);
            nomProduitPanier = itemView.findViewById(R.id.nom_produit);
            prixProduitPanier = itemView.findViewById(R.id.prix_produit);
            descProduitPanier = itemView.findViewById(R.id.description_produit);
        }

        /**
         * Fonction permettant de remplir la cardView du Panier
         */
        public void bind(Produit p) {
            nomProduitPanier.setText(p.getTitre());
            prixProduitPanier.setText(String.valueOf(p.getPrix()));
            imgProduitPanier.setImageResource(R.drawable.parisguidetower);
            descProduitPanier.setText(p.getDescription());
        }
    }
}
