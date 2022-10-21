package org.example.client;

import org.example.annotations.Component;

@Component
public class RandomNumberGenerator {
    public int generate() {
        return (int) (Math.random() * 1000.0);
    }
}
