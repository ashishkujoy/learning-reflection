package org.example.di;

import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.burningwave.core.assembler.ComponentContainer;
import org.burningwave.core.classes.ClassCriteria;
import org.burningwave.core.classes.ClassHunter;
import org.burningwave.core.classes.ClassHunter.SearchResult;
import org.burningwave.core.classes.SearchConfig;

public class ApplicationContext implements BeanReadCache {
    private static Logger logger;
    private static ApplicationContext applicationContext;
    private final ReflectionUtil reflectionUtil;
    private final HashMap<Class<?>, Object> beanCache;

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

        try {
            T newInstance = this.reflectionUtil.createInstanceOf(beanClass);
            this.reflectionUtil.invokePostConstruct(newInstance);
            beanCache.put(beanClass, newInstance);

            return Optional.of(newInstance);
        } catch (Throwable e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public void initializeBeans(Class<?> appClass) {
        ComponentContainer container = ComponentContainer.getInstance();
        ClassHunter classHunter = container.getClassHunter();
        ClassCriteria classesAnnotatedWithComponent = ClassCriteria.create()
                .allThoseThatMatch(clazz -> clazz.getAnnotation(Component.class) != null);
        String path = appClass.getPackageName().replace(".", "/");
        System.out.println(path);
        SearchConfig searchConfig = SearchConfig
                .forResources(path)
                .by(classesAnnotatedWithComponent);

        try (SearchResult searchResult = classHunter.findBy(searchConfig)) {
            Collection<Class<?>> beanClasses = searchResult.getClasses();
            
            for (Class<?> beanClass : beanClasses) {
                getBean(beanClass);
            }
        }
    }
}
