package gn.traore.commandeproduit;

public class Produit {

    private String image;
    private String titre;

    public Produit() { }

    public Produit(String titre, String image) {
        this.image = image;
        this.titre = titre;
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
}
