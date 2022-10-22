package org.example.ticket;

import org.example.di.Autowired;

public class TicketBookingService {
    private final PaymentSerivce paymentSerivce;
    private final NotificationService notificationService;


    public TicketBookingService(PaymentSerivce paymentSerivce) {
        this.paymentSerivce = paymentSerivce;
        this.notificationService = null;
    }

    @Autowired
    public TicketBookingService(
            PaymentSerivce paymentSerivce,
            NotificationService notificationService) {
        this.paymentSerivce = paymentSerivce;
        this.notificationService = notificationService;
    }

    public void bookTicket() {
        this.paymentSerivce.completePayment();
        this.notificationService.sendBookingNotification();
    }

}
