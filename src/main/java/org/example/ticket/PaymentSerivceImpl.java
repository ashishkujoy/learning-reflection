package org.example.ticket;

import org.example.di.Component;

@Component
public class PaymentSerivceImpl implements PaymentService {

    public void completePayment() {
        System.out.println("Completing payment...");
    }

}
