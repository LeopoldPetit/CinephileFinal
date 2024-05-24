package com.helha.java.q2.cinephile.Views;

import com.helha.java.q2.cinephile.patternFactory.PaymentMethod;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Contrôleur pour la vue de la page de paiement.
 */
public class CheckoutViewController {

    @FXML
    private TextField adultTextField;

    @FXML
    private TextField childTextField;

    @FXML
    private TextField seniorTextField;


    @FXML
    private Button checkoutbtn;

    @FXML
    private Button resetbtn;

    @FXML
    private Button backButton;

    @FXML
    private Label ticketPriceLabel;
    private NavListener listener;


    private double adultPrice = 8.50;
    private double childPrice = 5.00;
    private double seniorPrice = 7.50;


    private PaymentMethod paymentMethod;

    @FXML
    private ComboBox<String> paymentMethodComboBox;


    /**
     * Initialise le contrôleur.
     */
    @FXML
    private void initialize() {

        paymentMethodComboBox.getItems().addAll("Credit Card", "Bancontact");
        paymentMethodComboBox.setValue("Credit Card"); // Set a default value

        // Ajout d'écouteurs aux TextField pour mettre à jour le prix total
        adultTextField.textProperty().addListener((observable, oldValue, newValue) -> updateTotalPrice());
        childTextField.textProperty().addListener((observable, oldValue, newValue) -> updateTotalPrice());
        seniorTextField.textProperty().addListener((observable, oldValue, newValue) -> updateTotalPrice());


        // Mettre à jour le prix total au démarrage
        updateTotalPrice();

        checkoutbtn.setOnAction(event -> {
            // Récupérer le prix à nouveau au moment du clic
            String labelText = ticketPriceLabel.getText();
            double ticketPrice = Double.parseDouble(labelText);
            System.out.println("Prix du ticket : " + ticketPrice);
            openBancontactPage(ticketPrice);
        });

    }

    /**
     * Sets the payment method to be used for the transaction.
     *
     * @param paymentMethod The payment method to be set.
     */
    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }


    private void openBancontactPage(Double prix) {
        if (listener != null) {
            listener.sendToTerminal(prix);
        }

    }



    public void setListener(NavListener listener) {
        this.listener = listener;
    }

    public interface NavListener {
        void sendToTerminal(Double prix);
    }

    @FXML
    private void handleResetButton(ActionEvent event) {
        adultTextField.clear();
        childTextField.clear();
        seniorTextField.clear();
    }


    @FXML
    private void decrementAdult() {
        int count = parseTextFieldValue(adultTextField.getText());
        if (count > 0) {
            adultTextField.setText(String.valueOf(count - 1));
            updateTotalPrice();
        }
    }


    @FXML
    private void incrementAdult() {
        int count = parseTextFieldValue(adultTextField.getText());
        adultTextField.setText(String.valueOf(count + 1));
        updateTotalPrice();
    }


    @FXML
    private void decrementChild() {
        int count = parseTextFieldValue(childTextField.getText());
        if (count > 0) {
            childTextField.setText(String.valueOf(count - 1));
            updateTotalPrice();
        }
    }


    @FXML
    private void incrementChild() {
        int count = parseTextFieldValue(childTextField.getText());
        childTextField.setText(String.valueOf(count + 1));
        updateTotalPrice();
    }


    @FXML
    private void decrementSenior() {
        int count = parseTextFieldValue(seniorTextField.getText());
        if (count > 0) {
            seniorTextField.setText(String.valueOf(count - 1));
            updateTotalPrice();
        }
    }


    @FXML
    private void incrementSenior() {
        int count = parseTextFieldValue(seniorTextField.getText());
        seniorTextField.setText(String.valueOf(count + 1));
        updateTotalPrice();
    }


    @FXML


    public int getTotalTicketsChosen() {
        int adultCount = parseTextFieldValue(adultTextField.getText());
        int childCount = parseTextFieldValue(childTextField.getText());
        int seniorCount = parseTextFieldValue(seniorTextField.getText());


        return adultCount + childCount + seniorCount ;
    }
    public int getChildTiquet(){
        return parseTextFieldValue(childTextField.getText());
    }
    public int getSeniorTiquet(){
        return parseTextFieldValue(seniorTextField.getText());
    }
    public int getAdultTiquet(){
        return parseTextFieldValue(adultTextField.getText());
    }


    private void updateTotalPrice() {
        int adultCount = parseTextFieldValue(adultTextField.getText());
        int childCount = parseTextFieldValue(childTextField.getText());
        int seniorCount = parseTextFieldValue(seniorTextField.getText());


        double totalPrice = (adultCount * adultPrice) + (childCount * childPrice) + (seniorCount * seniorPrice);
        ticketPriceLabel.setText(String.valueOf(totalPrice));
    }

    public void updateTotalPrice(Double prix) {
        ticketPriceLabel.setText(String.valueOf(prix));
    }


    private int parseTextFieldValue(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}



