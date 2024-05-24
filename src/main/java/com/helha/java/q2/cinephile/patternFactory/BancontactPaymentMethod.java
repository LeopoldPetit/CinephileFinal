package com.helha.java.q2.cinephile.patternFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Concrete implementation of the Bancontact payment method.
 */
public class BancontactPaymentMethod implements PaymentMethod {
    /**
     * {@inheritDoc}
     */
    @Override
    public void pay(double amount) {
        // Implement payment logic here
        //System.out.println("Paid " + amount + " using Bancontact");

        // Logique de paiement par Bancontact
        String message = "Paid " + amount + "€ using Bancontact";
        writeToFile(message);
    }


    private void writeToFile(String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("transactions.txt", true))) {
            writer.write(message);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
