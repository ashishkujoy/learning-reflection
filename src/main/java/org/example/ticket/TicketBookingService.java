package org.example.ticket;

import org.example.di.Autowired;
import org.example.di.Component;

@Component
public class TicketBookingService {
    private final PaymentService paymentService;
    private final NotificationService notificationService;


    public TicketBookingService(PaymentService paymentSerivce) {
        this.paymentService = paymentSerivce;
        this.notificationService = null;
    }

    @Autowired
    public TicketBookingService(
            PaymentService paymentSerivce,
            NotificationService notificationService) {
        this.paymentService = paymentSerivce;
        this.notificationService = notificationService;
    }

    public void bookTicket() {
        this.paymentService.completePayment();
        this.notificationService.sendBookingNotification();
    }

}
