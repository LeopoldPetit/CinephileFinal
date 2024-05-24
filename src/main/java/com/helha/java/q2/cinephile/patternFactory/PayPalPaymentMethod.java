package com.helha.java.q2.cinephile.patternFactory;
/**
 * Concrete implementation of the PayPal payment method.
 */
public class PayPalPaymentMethod implements PaymentMethod {
    /**
     * {@inheritDoc}
     */
    @Override
    public void pay(double amount) {
        // Implement payment logic here
        System.out.println("Paid " + amount + " using PayPal");
    }
}


