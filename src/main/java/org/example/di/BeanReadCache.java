package org.example.di;

import java.util.Optional;

interface BeanReadCache {
    <T> Optional<T> getBean(Class<T> beanClass);
}
