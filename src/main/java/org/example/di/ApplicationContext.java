package org.example.di;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.example.ticket.Primary;

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

        if (requiredTypeIsInterface) {
            return createConcreteImplementationOf(beanClass);
        }

        return createBean(beanClass);

    }

    private void createComponents(Class<?> mainClass) {
        String appPackage = mainClass.getPackageName().replace(".", "/");
        Collection<Class<?>> componentClasses = ClasspathScanner.getClassesAnnotatedWith(appPackage, Component.class);
        this.componentsClasses = componentClasses;
        for (Class<?> componentClass : componentClasses) {
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
        if (components.size() == 0) {
            String beanTypeName = this.reflectionUtil.getName(beanClass);
            throw new BeanCreationException(
                    String.format("Error creating bean for type: %s, found no component implementing %s", beanTypeName,
                            beanTypeName));
        }
        if (components.size() > 1) {
            List<Class<?>> primaryComponents = this.reflectionUtil.filterClassAnnotatedWith(components, Primary.class);
            if (primaryComponents.isEmpty()) {
                String beanTypeName = this.reflectionUtil.getName(beanClass);
                List<String> componentNames = this.reflectionUtil.getNames(components);
                throw ErrorFactory.noPrimaryBeanException(beanTypeName, componentNames);
            }
            return this.createInstancePrimaryComponent(primaryComponents, beanClass);

        }
        return getBean((Class<T>) components.get(0));
    }

    @SuppressWarnings("unchecked")
    private <T> T createInstancePrimaryComponent(List<Class<?>> primaryComponents, Class<T> beanClass) {
        if (primaryComponents.size() > 1) {
            String beanTypeName = this.reflectionUtil.getName(beanClass);
            List<String> primaryComponentsName = this.reflectionUtil.getNames(primaryComponents);
            throw ErrorFactory.multiplePrimaryComponents(beanTypeName, primaryComponentsName);
        }
        return getBean((Class<T>)primaryComponents.get(0));
    }

}
