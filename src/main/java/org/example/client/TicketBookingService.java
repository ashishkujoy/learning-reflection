package org.example.client;

import org.example.annotations.Autowired;

public class TicketBookingService {
    private final RandomNumberGenerator randomNumberGenerator;
    private final ReportingService reportingService;
    
    @Autowired
    public TicketBookingService(RandomNumberGenerator randomNumberGenerator, ReportingService reportingService) {
        this.randomNumberGenerator = randomNumberGenerator;
        this.reportingService = reportingService;
    }

    public TicketBookingService(RandomNumberGenerator randomNumberGenerator) {
        this.randomNumberGenerator = randomNumberGenerator;
        this.reportingService = null;    
    }

    public void doSomething() {
        if(reportingService == null) {
            throw new RuntimeException("Reporting service is mandatory...");
        }
        System.out.println("Generating a random number...");
        System.out.println("The random number is " + randomNumberGenerator.generate());
    }
}
