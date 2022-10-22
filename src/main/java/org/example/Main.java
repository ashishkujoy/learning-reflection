package org.example;

import java.util.Optional;

import org.example.di.ApplicationContext;
import org.example.ticket.NotificationService;
import org.example.ticket.PaymentSerivce;
import org.example.ticket.TicketBookingService;

public class Main {
    public static void main(String[] args) {
        ApplicationContext applicationContext = ApplicationContext.init(Main.class);
        // Optional<NotificationService> notificationService = applicationContext.getBean(NotificationService.class);
        // Optional<PaymentSerivce> paymentService = applicationContext.getBean(PaymentSerivce.class);

        // notificationService.get().sendBookingNotification();
        // paymentService.get().completePayment();

        // Optional<TicketBookingService> ticketBookingService = applicationContext.getBean(TicketBookingService.class);
        // ticketBookingService.get().bookTicket();
    }
}
