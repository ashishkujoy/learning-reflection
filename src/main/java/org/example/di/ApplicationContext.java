package org.example.di;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ApplicationContext implements BeanReadCache {
    private static Logger logger;
    private static ApplicationContext applicationContext;
    private final ReflectionUtil reflectionUtil;
    private final HashMap<Class<?>, Object> beanCache;
    private Collection<Class<?>> components;

    private ApplicationContext() {
        this.beanCache = new HashMap<>();
        this.reflectionUtil = new ReflectionUtil(this);

    }

    public static ApplicationContext init(Class<?> appClass) {
        if (ApplicationContext.applicationContext == null) {
            logger = Logger.getLogger(ApplicationContext.class.getName());
            logger.log(Level.INFO, "String application context");
            long startedAt = System.currentTimeMillis();
            ApplicationContext.applicationContext = new ApplicationContext();
            ApplicationContext.applicationContext.initializeBeans(appClass);
            long completedAt = System.currentTimeMillis();
            logger.log(Level.INFO, String.format("Application started in %d", completedAt - startedAt));
        }
        return ApplicationContext.applicationContext;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Optional<T> getBean(Class<T> beanClass) {
        if (beanCache.containsKey(beanClass)) {
            return Optional.of((T) beanCache.get(beanClass));
        }

        boolean isNonInitializable = this.reflectionUtil.isInterfaceOrAbstractClass(beanClass);

        if (isNonInitializable) {
            return createConcreteImplementationOf(beanClass);
        }

        return createBean(beanClass);
    }

    @SuppressWarnings("unchecked")
    private <T> Optional<T> createConcreteImplementationOf(Class<T> beanClass) {
        List<Class<?>> componentsImplementing = this.reflectionUtil.getComponentsImplementing(this.components,
                beanClass);

        if (componentsImplementing.size() == 0) {
            throw ErrorFactory.noImplementationOfInterface(beanClass.getName());
        }
        if (componentsImplementing.size() == 1) {
            return createBean(componentsImplementing.get(0)).map(bean -> (T) bean);
        }
        List<Class<?>> primaryBeans = this.reflectionUtil.filterAnnotatedWith(componentsImplementing, Primary.class);

        if (primaryBeans.size() == 1) {
            return createBean(componentsImplementing.get(0)).map(bean -> (T) bean);
        }

        if (primaryBeans.size() == 0) {
            throw ErrorFactory.noPrimaryBeansFound(
                    beanClass.getName(),
                    this.reflectionUtil.getNames(componentsImplementing));
        } else {
            throw ErrorFactory.moreThanOnePrimaryBeansFound(beanClass.getName(), primaryBeans.size());
        }
    }

    private <T> Optional<T> createBean(Class<T> beanClass) {
        try {
            T newInstance = this.reflectionUtil.createInstanceOf(beanClass);
            this.reflectionUtil.invokePostConstruct(newInstance);
            Class<?>[] classesImplementedByBean = beanClass.getClasses();
            for (Class<?> superClass : classesImplementedByBean) {
                beanCache.put(superClass, newInstance);
            }
            beanCache.put(beanClass, newInstance);
            return Optional.of(newInstance);
        } catch (Throwable e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public void initializeBeans(Class<?> appClass) {
        String path = appClass.getPackageName().replace(".", "/");
        this.components = ClasspathScanner.getAllClassesAnnotatedWith(path, Component.class);

        for (Class<?> beanClass : this.components) {
            getBean(beanClass);
        }
    }
}
