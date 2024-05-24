package com.helha.java.q2.cinephile.Models;

import java.io.Serializable;

public class Film implements Serializable {
    private static final long serialVersionUID = 1L;
    private String titre;
    private String texte;
    private String duree;
    private String bandeAnnonce;
    private String image;
    private String dateSortie;
    private String jourDisponible;
    private String heureDisponible;
    private String debut;
    private String fin;
    private int tiquetsRestantsSalle1;
    private int id;
    private int tiquetsRestantsSalle2;
    private int tiquetsRestantsSalle3;

    // Constructeur
    public Film(String titre, String texte, String duree, String bandeAnnonce, String image, String dateSortie,
                String jourDisponible, String heureDisponible, String debut, String fin, int tiquetsRestantsSalle1, int id,
                int tiquetsRestantsSalle2, int tiquetsRestantsSalle3) {
        this.titre = titre;
        this.texte = texte;
        this.duree = duree;
        this.bandeAnnonce = bandeAnnonce;
        this.image = image;
        this.dateSortie = dateSortie;
        this.jourDisponible = jourDisponible;
        this.heureDisponible = heureDisponible;
        this.debut = debut;
        this.fin = fin;
        this.tiquetsRestantsSalle1 = tiquetsRestantsSalle1;
        this.id = id;
        this.tiquetsRestantsSalle2 = tiquetsRestantsSalle2;
        this.tiquetsRestantsSalle3 = tiquetsRestantsSalle3;
    }

    // Getters
    public String getTitre() {
        return titre;
    }

    public String getTexte() {
        return texte;
    }

    public String getDuree() {
        return duree;
    }

    public String getBandeAnnonce() {
        return bandeAnnonce;
    }

    public String getImage() {
        return image;
    }

    public String getDateSortie() {
        return dateSortie;
    }

    public String getJourDisponible() {
        return jourDisponible;
    }

    public String getHeureDisponible() {
        return heureDisponible;
    }

    public String getDebut() {
        return debut;
    }

    public String getFin() {
        return fin;
    }

    public int getTiquetsRestantsSalle1() {
        return tiquetsRestantsSalle1;
    }

    public int getId() {
        return id;
    }

    public int getTiquetsRestantsSalle2() {
        return tiquetsRestantsSalle2;
    }

    public int getTiquetsRestantsSalle3() {
        return tiquetsRestantsSalle3;
    }

    // Setters
    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setTexte(String texte) {
        this.texte = texte;
    }

    public void setDuree(String duree) {
        this.duree = duree;
    }

    public void setBandeAnnonce(String bandeAnnonce) {
        this.bandeAnnonce = bandeAnnonce;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setDateSortie(String dateSortie) {
        this.dateSortie = dateSortie;
    }

    public void setJourDisponible(String jourDisponible) {
        this.jourDisponible = jourDisponible;
    }

    public void setHeureDisponible(String heureDisponible) {
        this.heureDisponible = heureDisponible;
    }

    public void setDebut(String debut) {
        this.debut = debut;
    }

    public void setFin(String fin) {
        this.fin = fin;
    }

    public void setTiquetsRestantsSalle1(int tiquetsRestantsSalle1) {
        this.tiquetsRestantsSalle1 = tiquetsRestantsSalle1;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTiquetsRestantsSalle2(int tiquetsRestantsSalle2) {
        this.tiquetsRestantsSalle2 = tiquetsRestantsSalle2;
    }

    public void setTiquetsRestantsSalle3(int tiquetsRestantsSalle3) {
        this.tiquetsRestantsSalle3 = tiquetsRestantsSalle3;
    }
}
