package com.helha.java.q2.cinephile.Models;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TiquetDb {
    private Connection conn;

    /**
     * Constructeur de la classe TiquetDAO. Initialise la connexion à la base de données.
     */
    public TiquetDb() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:resources/data.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Récupère tous les tiquets à partir de la base de données.
     *
     * @return Une liste contenant tous les tiquets récupérés de la base de données.
     */
    public List<Tiquet> getAllTiquets() {
        List<Tiquet> tiquets = new ArrayList<>();
        String query = "SELECT * FROM Tiquets";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Tiquet tiquet = new Tiquet();
                tiquet.setFilmId(rs.getInt("FilmId"));
                tiquet.setId(rs.getInt("Id"));
                tiquet.setNombreDeTiquet(rs.getInt("NombreDeTiquet"));
                tiquet.setSalle(rs.getInt("Salle"));
                tiquet.setHeure(rs.getString("Heure"));
                tiquet.setPrix(rs.getString("Prix"));
                tiquet.setNombreDeTiquetEnfant(rs.getInt("NombreDeTiquetEnfant"));
                tiquet.setNombreDeTiquetSenior(rs.getInt("NombreDeTiquetSenior"));
                tiquet.setNombreDeTiquetAdulte(rs.getInt("NombreDeTiquetAdulte"));
                tiquet.setNomFilm(rs.getString("NomFilm"));


                tiquets.add(tiquet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tiquets;
    }

    /**
     * Récupère un tiquet par son identifiant à partir de la base de données.
     *
     * @param id L'identifiant du tiquet à récupérer.
     * @return Le tiquet correspondant à l'identifiant donné, ou null s'il n'existe pas.
     */
    public Tiquet getTiquetById(int id) {
        Tiquet tiquet = null;
        String query = "SELECT * FROM Tiquets WHERE Id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    tiquet = new Tiquet();
                    tiquet.setFilmId(rs.getInt("FilmId"));
                    tiquet.setId(rs.getInt("Id"));
                    tiquet.setNombreDeTiquet(rs.getInt("NombreDeTiquet"));
                    tiquet.setSalle(rs.getInt("Salle"));
                    tiquet.setHeure(rs.getString("Heure"));
                    tiquet.setPrix(rs.getString("Prix"));
                    tiquet.setNombreDeTiquetEnfant(rs.getInt("NombreDeTiquetEnfant"));
                    tiquet.setNombreDeTiquetSenior(rs.getInt("NombreDeTiquetSenior"));
                    tiquet.setNombreDeTiquetAdulte(rs.getInt("NombreDeTiquetAdulte"));
                    tiquet.setNomFilm(rs.getString("NomFilm"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tiquet;
    }
    public void insertTiquet(Tiquet tiquet) {
        String query = "INSERT INTO Tiquets (FilmId, NombreDeTiquet, Salle, Heure, Prix, NombreDeTiquetEnfant, NombreDeTiquetSenior, NombreDeTiquetAdulte, NomFilm) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, tiquet.getFilmId());
            pstmt.setInt(2, tiquet.getNombreDeTiquet());
            pstmt.setInt(3, tiquet.getSalle());
            pstmt.setString(4, tiquet.getHeure());
            pstmt.setString(5, tiquet.getPrix());
            pstmt.setInt(6, tiquet.getNombreDeTiquetEnfant());
            pstmt.setInt(7, tiquet.getNombreDeTiquetSenior());
            pstmt.setInt(8, tiquet.getNombreDeTiquetAdulte());
            pstmt.setString(9, tiquet.getNomFilm());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
