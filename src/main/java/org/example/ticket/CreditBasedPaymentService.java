package org.example.ticket;

import org.example.di.Component;

@Component
@Primary
public class CreditBasedPaymentService implements PaymentService {

    @Override
    public void completePayment() {
        System.out.println("Doing payment using credit...");
    }
    
}
