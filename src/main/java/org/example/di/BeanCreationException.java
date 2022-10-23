package org.example.di;

public class BeanCreationException extends RuntimeException {

    public BeanCreationException(String beanClassName, Throwable throwable) {
        super("Error created bean: " + beanClassName, throwable);
    }
}
