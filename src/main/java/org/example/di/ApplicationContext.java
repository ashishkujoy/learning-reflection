package org.example.di;

import java.util.HashMap;

public class ApplicationContext implements BeanReadCache {
    private static ApplicationContext applicationContext;
    private final ReflectionUtil reflectionUtil;
    private final HashMap<Class<?>, Object> beanCache;

    private ApplicationContext() {
        this.beanCache = new HashMap<>();
        this.reflectionUtil = new ReflectionUtil(this);
    }

    public static ApplicationContext init() {
        if(ApplicationContext.applicationContext == null) {
            ApplicationContext.applicationContext = new ApplicationContext();
        }
        return ApplicationContext.applicationContext;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getBean(Class<T> beanClass) {
        if(beanCache.containsKey(beanClass)) {
            return (T) beanCache.get(beanClass);
        }

        try {
            T newInstance = this.reflectionUtil.createInstanceOf(beanClass);
            this.reflectionUtil.invokePostConstruct(newInstance);
            beanCache.put(beanClass, newInstance);

            return newInstance;
        } catch (Throwable e) {
            throw new BeanCreationException(this.reflectionUtil.getName(beanClass),  e);
        }
    }

}
