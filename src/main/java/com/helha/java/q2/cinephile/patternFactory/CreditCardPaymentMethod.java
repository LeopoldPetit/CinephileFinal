package com.helha.java.q2.cinephile.patternFactory;
/**
 * Concrete implementation of the Credit Card payment method.
 */
public class CreditCardPaymentMethod implements PaymentMethod {
    /**
     * {@inheritDoc}
     */
    @Override
    public void pay(double amount) {
        // Implement payment logic here
        System.out.println("Paid " + amount + " using Credit Card");
    }
}


