package org.example.client;

import org.example.annotations.PostContruct;

public class NotificationService {

    @PostContruct
    public void doSomething() {
        System.out.println("This is a post construct method for notification service");
    }
}
