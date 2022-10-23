package org.example.di;

public interface BeanReadCache {
    <T> T getBean(Class<T> beanClass);
}
