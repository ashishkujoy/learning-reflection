package org.example.di;

import java.util.Optional;

public class ApplicationContext {
    public static ApplicationContext init() {
        return new ApplicationContext();
    }

    public <T> Optional<T> getBean(Class<T> clazz) {
        return ReflectionUtil.constructNewInstance(clazz);
    }
}
