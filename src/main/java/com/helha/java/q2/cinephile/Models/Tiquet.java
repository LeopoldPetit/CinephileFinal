package com.helha.java.q2.cinephile.Models;

public class Tiquet {
    private int filmId;
    private int id;
    private int nombreDeTiquet;
    private int salle;
    private String heure;
    private String prix;
    private int nombreDeTiquetEnfant;
    private int nombreDeTiquetSenior;
    private int nombreDeTiquetAdulte;

    // Getters and setters
    public int getFilmId() {
        return filmId;
    }

    public void setFilmId(int filmId) {
        this.filmId = filmId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNombreDeTiquet() {
        return nombreDeTiquet;
    }

    public void setNombreDeTiquet(int nombreDeTiquet) {
        this.nombreDeTiquet = nombreDeTiquet;
    }

    public int getSalle() {
        return salle;
    }

    public void setSalle(int salle) {
        this.salle = salle;
    }

    public String getHeure() {
        return heure;
    }

    public void setHeure(String heure) {
        this.heure = heure;
    }

    public String getPrix() {
        return prix;
    }

    public void setPrix(String prix) {
        this.prix = prix;
    }

    public int getNombreDeTiquetEnfant() {
        return nombreDeTiquetEnfant;
    }

    public void setNombreDeTiquetEnfant(int nombreDeTiquetEnfant) {
        this.nombreDeTiquetEnfant = nombreDeTiquetEnfant;
    }

    public int getNombreDeTiquetSenior() {
        return nombreDeTiquetSenior;
    }

    public void setNombreDeTiquetSenior(int nombreDeTiquetSenior) {
        this.nombreDeTiquetSenior = nombreDeTiquetSenior;
    }

    public int getNombreDeTiquetAdulte() {
        return nombreDeTiquetAdulte;
    }

    public void setNombreDeTiquetAdulte(int nombreDeTiquetAdulte) {
        this.nombreDeTiquetAdulte = nombreDeTiquetAdulte;
    }
}
