package com.helha.java.q2.cinephile.patternFactory;
/**
 * Interface defining a payment method.
 */
public interface PaymentMethod {
    /**
     * Performs a payment transaction with the given amount.
     *
     * @param amount The amount to be paid.
     */
    void pay(double amount);
}



