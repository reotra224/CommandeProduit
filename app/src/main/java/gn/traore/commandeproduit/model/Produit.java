package gn.traore.commandeproduit.model;

import java.io.Serializable;

public class Produit implements Serializable {

    private String image;
    private String titre;
    private Double prix;
    private String description;

    public Produit() { }

    public Produit(String titre, String image, Double prix) {
        this.image = image;
        this.titre = titre;
        this.prix = prix;
    }

    public Produit(String titre, String image, Double prix, String description) {
        this.image = image;
        this.titre = titre;
        this.prix = prix;
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public Double getPrix() {
        return prix;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
