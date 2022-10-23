package org.example.di;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.burningwave.core.assembler.ComponentContainer;
import org.burningwave.core.classes.ClassCriteria;
import org.burningwave.core.classes.ClassHunter;
import org.burningwave.core.classes.ClassHunter.SearchResult;
import org.burningwave.core.classes.SearchConfig;

public class ApplicationContext implements BeanReadCache {
    private static ApplicationContext applicationContext;
    private final ReflectionUtil reflectionUtil;
    private final HashMap<Class<?>, Object> beanCache;
    private Collection<Class<?>> componentsClasses;

    private ApplicationContext() {
        this.beanCache = new HashMap<>();
        this.reflectionUtil = new ReflectionUtil(this);
        this.componentsClasses = new ArrayList<>();
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

        boolean requiredTypeIsInterface = this.reflectionUtil.isInterface(beanClass);

        if(requiredTypeIsInterface) {
            return createConcreteImplementationOf(beanClass);
        }

        return createBean(beanClass);
        
    }

    
    private void createComponents(Class<?> mainClass) {
        String appPackage = mainClass.getPackageName().replace(".", "/");
        Collection<Class<?>> componentClasses = ClasspathScanner.getClassesAnnotatedWith(appPackage, Component.class);
        this.componentsClasses = componentClasses;
        for(Class<?> componentClass : componentClasses) {
            getBean(componentClass);
        }
    }

    private <T> T createBean(Class<T> beanClass) {
        try {
            T newInstance = this.reflectionUtil.createInstanceOf(beanClass);
            this.reflectionUtil.invokePostConstruct(newInstance);
            beanCache.put(beanClass, newInstance);

            return newInstance;
        } catch (Throwable e) {
            throw new BeanCreationException(this.reflectionUtil.getName(beanClass), e);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T createConcreteImplementationOf(Class<T> beanClass) {
        List<Class<?>> components = this.reflectionUtil.filterClassImplementing(this.componentsClasses, beanClass);
        return getBean((Class<T>) components.get(0));
    }

}
