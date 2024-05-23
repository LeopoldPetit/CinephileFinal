package com.helha.java.q2.cinephile.Views;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

public class BancontactViewController {

    @FXML
    private Label montantLabel;

    @FXML
    private Button acceptButton;

    @FXML
    private Button rejectButton;

    private Socket clientSocket;
    private PrintWriter writer;
    private CountDownLatch latch;

    private Listener listener;
    @FXML
    private TextField promoCodeTextField;
    Double prix;

    @FXML
    private void initialize() {

        if (montantLabel == null) {
            System.out.println("montantLabel est null. Assurez-vous que le FXML est correctement chargé.");
        }
        promoCodeTextField.setOnAction(event -> {
            prix = Double.valueOf(montantLabel.getText().substring(0, montantLabel.getText().length() - 1));
            OnCodeEnter(promoCodeTextField.getText(), prix);
        });

        acceptButton.setOnAction(event -> {
            sendResponseToClient("Accepter", prix);
        });

        rejectButton.setOnAction(event -> {
            sendResponseToClient("Refuser",prix);
        });

    }

    public void setMontant(double montant) {
        Platform.runLater(() -> {
            if (montantLabel != null) {
                prix=montant;
                montantLabel.setText(String.format("%.2f €", montant));
            } else {
                System.out.println("montantLabel est null. Assurez-vous que le FXML est correctement chargé.");
            }
        });
    }

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
        try {
            writer = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setLatch(CountDownLatch latch) {
        this.latch = latch;
    }

    private void sendResponseToClient(String response, Double prix) {
        try {
            if (writer != null) {
                writer.println(response);
                writer.println(prix); // Envoyer le prix au client
                System.out.println("Réponse envoyée au client: " + response);
                System.out.println("Prix envoyé au client: " + prix);
            } else {
                System.out.println("Erreur: Le writer vers le socket client est null.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (latch != null) {
                latch.countDown(); // Décrémente le CountDownLatch
            }
        }
    }
    private void OnCodeEnter(String code, Double prix) {
        if (listener != null){
            listener.OnCodeEnter(code, prix);
        }

    }



    public void setListener(Listener listener) {
        this.listener = listener;
    }
    public interface Listener {
        void OnCodeEnter(String code, Double prix);

    }
}
