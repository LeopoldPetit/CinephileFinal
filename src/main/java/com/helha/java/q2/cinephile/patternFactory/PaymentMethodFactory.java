package com.helha.java.q2.cinephile.patternFactory;
/**
 * Factory class to create instances of PaymentMethod.
 */
public abstract class PaymentMethodFactory {
    /**
     * Creates a payment method instance based on the provided type.
     *
     * @param type The type of payment method to create.
     * @return An instance of PaymentMethod corresponding to the given type.
     * @throws IllegalArgumentException if the provided type is invalid.
     */
    public static PaymentMethod createPaymentMethod(String type) {
        if (type.equals("Bancontact")) {
            return new BancontactPaymentMethod();
        } else if (type.equals("PayPal")) {
            return new PayPalPaymentMethod();
        } else if (type.equals("CreditCard")) {
            return new CreditCardPaymentMethod();
        }
        throw new IllegalArgumentException("Invalid payment method type");
    }
}


