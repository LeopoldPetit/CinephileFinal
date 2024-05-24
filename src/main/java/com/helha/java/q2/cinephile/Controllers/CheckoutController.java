package com.helha.java.q2.cinephile.Controllers;

import com.helha.java.q2.cinephile.Models.Film;
import com.helha.java.q2.cinephile.Models.FilmDb;
import com.helha.java.q2.cinephile.Models.Tiquet;
import com.helha.java.q2.cinephile.Models.TiquetDb;
import com.helha.java.q2.cinephile.Views.CheckoutViewController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;

public class CheckoutController {
    static CheckoutViewController checkoutViewController;

    public static void openCheckout(Film film) {
        try {
            FXMLLoader loader = new FXMLLoader(CheckoutController.class.getResource("/com/helha/java/q2/cinephile/checkout.fxml"));
            Parent root = loader.load();
            checkoutViewController = loader.getController();
            checkoutViewController.setListener(prix -> {
                System.out.println("sendToTerminal2");
                startClient(prix, film);
            });

            // Obtient la scène actuelle
            Scene newScene = new Scene(root);

            // Créez un nouveau stage pour la nouvelle scène
            Stage newStage = new Stage();
            newStage.setScene(newScene);
            newStage.setWidth(875);
            newStage.setHeight(800);
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void startClient(Double prix, Film film) {
        String serverAddress = "127.0.0.1"; // Adresse IP du serveur (localhost)
        int serverPort = 12345; // Port utilisé par le serveur
        try (
                Socket socket = new Socket(serverAddress, serverPort);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        ) {
            System.out.println("Client démarré, connecté au serveur " + serverAddress + ":" + serverPort);

            // Envoi du montant au serveur
            out.writeObject("SEND_PAYMENT " + prix);
            out.flush();
            System.out.println("Montant " + prix + " envoyé au serveur.");

            // Lecture de la réponse du serveur
            boolean responseReceived = false;
            while (!responseReceived) {
                Object response = in.readObject();
                if (response instanceof String) {
                    String command = (String) response;
                    if (command.startsWith("PaymentAccepted")) {
                        System.out.println("Réponse du serveur: " + response);
                        System.out.println("le client a accepté la commande");
                        double finalAmount = Double.parseDouble(command.split(" ")[1]); // Récupère le montant final
                        System.out.println("le client a accepté la commande " + finalAmount);
                        checkoutViewController.updateTotalPrice(finalAmount);
                        int nombreDeTiquet = checkoutViewController.getTotalTicketsChosen();
                        createNewTiquet(film.getId(), nombreDeTiquet, 3, "18:00", finalAmount, 1, 1, 1);
                        updateTiquetsRestants(film.getId(), 3, nombreDeTiquet);
                        System.out.println("Montant final restant: " + finalAmount);
                        System.out.println("Nombre de tiquet: " + nombreDeTiquet);

                        // Fermer la communication après avoir reçu la réponse attendue
                        responseReceived = true;
                    } else if (command.startsWith("PaymentRejected")) {
                        System.out.println("le client a refusé la commande");
                        responseReceived = true;
                    }

                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void createNewTiquet(int filmId, int nombreDeTiquet, int salle, String heure, double prix, int nombreDeTiquetEnfant, int nombreDeTiquetSenior, int nombreDeTiquetAdulte) {
        TiquetDb tiquetDb = new TiquetDb();
        Tiquet newTiquet = new Tiquet();
        newTiquet.setFilmId(filmId);
        newTiquet.setNombreDeTiquet(nombreDeTiquet);
        newTiquet.setSalle(salle);
        newTiquet.setHeure(heure);
        newTiquet.setPrix(String.valueOf(prix));
        newTiquet.setNombreDeTiquetEnfant(nombreDeTiquetEnfant);
        newTiquet.setNombreDeTiquetSenior(nombreDeTiquetSenior);
        newTiquet.setNombreDeTiquetAdulte(nombreDeTiquetAdulte);

        tiquetDb.insertTiquet(newTiquet);
        System.out.println("Nouveau tiquet créé avec succès.");
    }

    private static void updateTiquetsRestants(int filmId, int salle, int nombreDeTiquetAchetes) {
        FilmDb filmDb = new FilmDb();
        try {
            // Récupérer les informations actuelles sur les tiquets restants pour la salle choisie
            Film film = filmDb.getFilmById(filmId);

            // Mettre à jour les tiquets restants dans la salle choisie
            switch (salle) {
                case 1:
                    film.setTiquetsRestantsSalle1(film.getTiquetsRestantsSalle1() - nombreDeTiquetAchetes);
                    break;
                case 2:
                    film.setTiquetsRestantsSalle2(film.getTiquetsRestantsSalle2() - nombreDeTiquetAchetes);
                    break;
                case 3:
                    film.setTiquetsRestantsSalle3(film.getTiquetsRestantsSalle3() - nombreDeTiquetAchetes);
                    break;
                default:
                    System.out.println("Salle inconnue.");
                    return;
            }

            // Mettre à jour les informations dans la base de données
            filmDb.updateFilm(film);
            System.out.println("Mise à jour des tiquets restants pour la salle " + salle + " effectuée avec succès.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
