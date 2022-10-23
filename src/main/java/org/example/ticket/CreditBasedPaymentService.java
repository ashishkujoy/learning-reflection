package org.example.ticket;

import org.example.di.Component;
import org.example.di.Primary;

@Component
@Primary
public class CreditBasedPaymentService implements PaymentService {

    @Override
    public void completePayment() {
        System.out.println("Doing payment via credit payment service...");
    }
    
}
