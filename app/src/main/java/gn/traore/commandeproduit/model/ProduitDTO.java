package gn.traore.commandeproduit.model;

import java.io.Serializable;

public class ProduitDTO implements Serializable {
    private int id_produit;
    private Double prix;
    private int quantite;
    private String phone;
    private Double montant;

    public ProduitDTO() {
    }

    public ProduitDTO(int id_produit, Double prix, int quantite, String phone, Double montant) {
        this.id_produit = id_produit;
        this.prix = prix;
        this.quantite = quantite;
        this.phone = phone;
        this.montant = montant;
    }

    public ProduitDTO(Double prix, int quantite, String phone, Double montant) {
        this.prix = prix;
        this.quantite = quantite;
        this.phone = phone;
        this.montant = montant;
    }

    public int getId_produit() {
        return id_produit;
    }

    public void setId_produit(int id_produit) {
        this.id_produit = id_produit;
    }

    public Double getPrix() {
        return prix;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }
}
