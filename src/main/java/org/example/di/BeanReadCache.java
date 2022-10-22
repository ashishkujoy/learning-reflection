package org.example.di;

import java.util.Optional;

public interface BeanReadCache {
    <T> Optional<T> getBean(Class<T> beanClass);
}
