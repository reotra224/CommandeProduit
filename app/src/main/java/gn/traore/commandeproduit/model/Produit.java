package gn.traore.commandeproduit.model;

import java.io.Serializable;

public class Produit implements Serializable {

    private int id;
    private String matricule;
    private String nom;
    private String categorie;
    private int stock;
    private String description;
    private Double prix;
    private String image;

    public Produit() { }

    public Produit(int id, String matricule, String nom, String categorie, int stock, String description,
                   Double prix, String image) {
        this.id = id;
        this.matricule = matricule;
        this.nom = nom;
        this.stock = stock;
        this.categorie = categorie;
        this.description = description;
        this.prix = prix;
        this.image = image;
    }

    public Produit(String matricule, String nom, String categorie, int stock, String description, Double prix, String image) {
        this.matricule = matricule;
        this.nom = nom;
        this.categorie = categorie;
        this.stock = stock;
        this.description = description;
        this.prix = prix;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrix() {
        return prix;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
