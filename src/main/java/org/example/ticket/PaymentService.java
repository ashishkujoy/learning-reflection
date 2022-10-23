package org.example.ticket;

import org.example.di.Component;

@Component
public class PaymentService {

    public void completePayment() {
        System.out.println("Completing payment...");
    }

}
