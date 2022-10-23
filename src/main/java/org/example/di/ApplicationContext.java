package org.example.di;

import java.util.Collection;
import java.util.HashMap;

import org.burningwave.core.assembler.ComponentContainer;
import org.burningwave.core.classes.ClassCriteria;
import org.burningwave.core.classes.ClassHunter;
import org.burningwave.core.classes.ClassHunter.SearchResult;
import org.burningwave.core.classes.SearchConfig;

public class ApplicationContext implements BeanReadCache {
    private static ApplicationContext applicationContext;
    private final ReflectionUtil reflectionUtil;
    private final HashMap<Class<?>, Object> beanCache;

    private ApplicationContext() {
        this.beanCache = new HashMap<>();
        this.reflectionUtil = new ReflectionUtil(this);
    }

    public static ApplicationContext init(Class<?> mainClass) {
        if (ApplicationContext.applicationContext == null) {
            System.out.println("Starting application...");
            long startTime = System.currentTimeMillis();
            ApplicationContext applicationContext = new ApplicationContext();
            ApplicationContext.applicationContext = applicationContext;
            applicationContext.createComponents(mainClass);
            long endTime = System.currentTimeMillis();
            System.out.println("Application started in " + (endTime - startTime) + " milliseconds");
        }
        return ApplicationContext.applicationContext;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getBean(Class<T> beanClass) {
        if (beanCache.containsKey(beanClass)) {
            return (T) beanCache.get(beanClass);
        }

        try {
            T newInstance = this.reflectionUtil.createInstanceOf(beanClass);
            this.reflectionUtil.invokePostConstruct(newInstance);
            beanCache.put(beanClass, newInstance);

            return newInstance;
        } catch (Throwable e) {
            throw new BeanCreationException(this.reflectionUtil.getName(beanClass), e);
        }
    }

    private void createComponents(Class<?> mainClass) {
        String appPackage = mainClass.getPackageName().replace(".", "/");
        Collection<Class<?>> componentClasses = ClasspathScanner.getClassesAnnotatedWith(appPackage, Component.class);
        
        for(Class<?> componentClass : componentClasses) {
            getBean(componentClass);
        }
    }

}
