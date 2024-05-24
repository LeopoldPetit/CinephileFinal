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

    private Listener listener;
    private Blistener buttonListener;

    @FXML
    private TextField promoCodeTextField;

    private Double prix;

    @FXML
    private void initialize() {
        promoCodeTextField.setOnKeyReleased(event -> {
            prix = Double.valueOf(montantLabel.getText().substring(0, montantLabel.getText().length() - 1));
            OnCodeEnter(promoCodeTextField.getText());
        });

        acceptButton.setOnAction(event -> {
            prix = Double.valueOf(montantLabel.getText().substring(0, montantLabel.getText().length() - 1));
            OnAccepted( prix);
        });

        rejectButton.setOnAction(event -> {
            prix = Double.valueOf(montantLabel.getText().substring(0, montantLabel.getText().length() - 1));
            OnRejected(prix);
        });
    }

    public void setMontant(double montant) {
        Platform.runLater(() -> {
            montantLabel.setText(String.valueOf(montant));
        });
    }


    public void OnAccepted(Double prix) {
        if (buttonListener != null) {
            buttonListener.OnAccepted(prix);
        }
    }
    public void OnRejected(Double prix) {
        if (buttonListener != null) {
            buttonListener.OnRejected(prix);
        }
    }


    private void OnCodeEnter(String code) {
        if (listener != null) {
            listener.OnCodeEnter(code);
        }
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }
    public void setButtonListener(Blistener buttonListener) {
        this.buttonListener = buttonListener;
    }
    public interface Blistener{
        void OnAccepted( Double prix);
        void OnRejected( Double prix);
    }

    public interface Listener {
        void OnCodeEnter(String code);
    }
}
