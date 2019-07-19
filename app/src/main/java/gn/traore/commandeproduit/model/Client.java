package gn.traore.commandeproduit.model;

import java.io.Serializable;

public class Client implements Serializable {
    private String nom;
    private String prenom;
    private String email;
    private String phone;
    private String adresse;
    private String agent;

    public Client() {
    }

    public Client(String nom, String prenom, String email, String phone, String adresse, String agent) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.phone = phone;
        this.adresse = adresse;
        this.agent = agent;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }
}
