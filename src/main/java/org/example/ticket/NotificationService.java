package org.example.ticket;

import org.example.di.PostConstruct;

public class NotificationService {

    public NotificationService() {
        System.out.println("Creating instance of notification service....");
    }

    public void sendBookingNotification() {
        System.out.println("Sending booking notification...");
    }

    @PostConstruct
    public void onConstruct() {
        System.out.println("Post construct method is called...");
    }

}
