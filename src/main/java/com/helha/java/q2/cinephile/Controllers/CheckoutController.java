package com.helha.java.q2.cinephile.Controllers;

import com.helha.java.q2.cinephile.Models.Film;
import com.helha.java.q2.cinephile.Models.FilmDb;
import com.helha.java.q2.cinephile.Models.Tiquet;
import com.helha.java.q2.cinephile.Models.TiquetDb;
import com.helha.java.q2.cinephile.Views.CheckoutViewController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;

public class CheckoutController {
    static CheckoutViewController checkoutViewController;

    public static void openCheckout(Film film, String room, String hour,Stage shedulePageStage) {
        try {
            FXMLLoader loader = new FXMLLoader(CheckoutController.class.getResource("/com/helha/java/q2/cinephile/checkout.fxml"));
            Parent root = loader.load();


            // Obtient la scène actuelle
            Scene newScene = new Scene(root);

            // Créez un nouveau stage pour la nouvelle scène
            Stage checkoutPageStage = new Stage();
            checkoutPageStage.setScene(newScene);
            checkoutPageStage.setWidth(875);
            checkoutPageStage.setHeight(800);
            checkoutPageStage.show();
            checkoutViewController = loader.getController();
            checkoutViewController.setListener(prix -> {
                System.out.println("room: "+room + " hour: "+hour);
                startClient(prix, film, room, hour, checkoutPageStage, shedulePageStage);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void startClient(Double prix, Film film, String room, String hour, Stage checkoutPageStage, Stage shedulePageStage) {
        String serverAddress = "127.0.0.1"; // Adresse IP du serveur (localhost)
        int serverPort = 12345; // Port utilisé par le serveur
        try (
                Socket socket = new Socket(serverAddress, serverPort);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        ) {
            System.out.println("Client démarré, connecté au serveur " + serverAddress + ":" + serverPort);

            // Envoi du montant au serveur
            int nombreDeTiquet = checkoutViewController.getTotalTicketsChosen();
            int nombreDeTiquetEnfant= checkoutViewController.getChildTiquet();
            int nombreDeTiquetAdulte= checkoutViewController.getAdultTiquet();
            int nombreDeTiquetSenior= checkoutViewController.getSeniorTiquet();
            out.writeObject("SEND_PAYMENT " + prix + " "+ nombreDeTiquet+" "+film.getId()+" "+nombreDeTiquetEnfant+" "+nombreDeTiquetAdulte+" "+nombreDeTiquetSenior+" "+room+" "+hour);
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
                        System.out.println("Montant final restant: " + finalAmount);
                        System.out.println("Nombre de tiquet: " + nombreDeTiquet);
                        FilmController.connectToServer();
                        FilmController.loadTiquets();
                        FilmController.connectToServer();
                        FilmController.loadFilms();
                        System.out.println("rechargement historique");
                        // Fermer la communication après avoir reçu la réponse attendue
                        responseReceived = true;
                        checkoutPageStage.close();
                        shedulePageStage.close();
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
}

