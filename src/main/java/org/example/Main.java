package org.example;

import java.util.Optional;

import org.example.client.RandomNumberGenerator;
import org.example.client.TicketBookingService;
import org.example.di.ApplicationContext;

public class Main {
    public static void main(String[] args) throws Throwable {
        ApplicationContext applicationContext = ApplicationContext.init();
        Optional<RandomNumberGenerator> randomNumberGenerator = applicationContext.getBean(RandomNumberGenerator.class);
        
        if(randomNumberGenerator.isPresent()) {
            System.out.println("Successfully got bean: RandomNumberGenerator");
        } else {
            System.out.println("Unable to get the bean: RandomNumberGenerator");
        }

        Optional<TicketBookingService> ticketBookingService = applicationContext.getBean(TicketBookingService.class);

        if(ticketBookingService.isPresent()) {
            System.out.println("Successfully got bean: TicketBookingService");
            ticketBookingService.get().doSomething();
        } else {
            System.out.println("Unable to get the bean: TicketBookingService");
        }
        
    }
}