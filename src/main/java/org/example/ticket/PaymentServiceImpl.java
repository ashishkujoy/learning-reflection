package org.example.ticket;

import org.example.di.Component;

@Component
public class PaymentServiceImpl implements PaymentService {

    @Override
    public void completePayment() {
        System.out.println("Completing payment...");
    }

}
