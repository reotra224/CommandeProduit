package gn.traore.commandeproduit.model;

import java.io.Serializable;

public class ProduitPanier implements Serializable {

    private Produit produit_panier;
    private int quantite_produit_panier;

    public ProduitPanier() {}

    public ProduitPanier(Produit produit_panier, int quantite_produit_panier) {
        this.produit_panier = produit_panier;
        this.quantite_produit_panier = quantite_produit_panier;
    }

    public Produit getProduit_panier() {
        return produit_panier;
    }

    public void setProduit_panier(Produit produit_panier) {
        this.produit_panier = produit_panier;
    }

    public int getQuantite_produit_panier() {
        return quantite_produit_panier;
    }

    public void setQuantite_produit_panier(int quantite_produit_panier) {
        this.quantite_produit_panier = quantite_produit_panier;
    }
}
