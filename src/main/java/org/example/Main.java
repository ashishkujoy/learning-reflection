package org.example;

import java.util.Optional;

import org.example.di.ApplicationContext;
import org.example.ticket.NotificationService;
import org.example.ticket.PaymentSerivce;
import org.example.ticket.TicketBookingService;

public class Main {
    public static void main(String[] args) {
        ApplicationContext applicationContext = ApplicationContext.init();
        NotificationService notificationService = applicationContext.getBean(NotificationService.class);
        PaymentSerivce paymentService = applicationContext.getBean(PaymentSerivce.class);

        notificationService.sendBookingNotification();
        paymentService.completePayment();

        TicketBookingService ticketBookingService = applicationContext.getBean(TicketBookingService.class);
        ticketBookingService.bookTicket();
    }
}
