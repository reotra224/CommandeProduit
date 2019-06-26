package gn.traore.commandeproduit;

import java.io.Serializable;

public class Produit implements Serializable {

    private String image;
    private String titre;
    private Double prix;

    public Produit() { }

    public Produit(String titre, String image, Double prix) {
        this.image = image;
        this.titre = titre;
        this.prix = prix;
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
}
