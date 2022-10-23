package org.example.di;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ReflectionUtil {
    private final BeanReadCache beanCache;

    public ReflectionUtil(BeanReadCache beanCache) {
        this.beanCache = beanCache;
    }

    @SuppressWarnings("unchecked")
    public <T> T createInstanceOf(Class<T> beanClass) {
        Constructor<?> primaryConstructor = getConstructor(beanClass);
        Class<?>[] constructorArgClasses = primaryConstructor.getParameterTypes();
        ArrayList<Object> constructorParameters = new ArrayList<>(constructorArgClasses.length);
        
        for(Class<?> argType : constructorArgClasses) {
            constructorParameters.add(beanCache.getBean(argType).get());
        }
        try {
            Object newInstance = primaryConstructor.newInstance(constructorParameters.toArray());
            return (T) newInstance;
        } catch(Throwable t) {
            throw new RuntimeException(t);
        }
    }
    
    private Constructor<?> getConstructor(Class<?> beanClass) {
        Constructor<?>[] constructors = beanClass.getConstructors();
        if(constructors.length == 1) {
            return constructors[0];
        } else {
            ArrayList<Constructor<?>> constructorWithAutowiredAnnotation = new ArrayList<>();
            for(Constructor<?> constructor: constructors) {
                if(constructor.isAnnotationPresent(Autowired.class)) {
                    constructorWithAutowiredAnnotation.add(constructor);
                }
            }
            if(constructorWithAutowiredAnnotation.size() == 1) {
                return constructorWithAutowiredAnnotation.get(0);
            } else {
                throw new RuntimeException(
                    String.format("For bean %s: Required exactly one constructor to be annotated with @Autowired found %d",
                     beanClass.getName(),
                     constructorWithAutowiredAnnotation.size()
                     )
                );
            }
        }
    }

    public void invokePostConstruct(Object newInstance) {
        Method[] declaredMethods = newInstance.getClass().getDeclaredMethods();
        for(Method method: declaredMethods) {
            if(method.isAnnotationPresent(PostConstruct.class)) {
                try {
                    method.invoke(newInstance);
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public boolean isInterfaceOrAbstractClass(Class<?> beanClass) {        
        return beanClass.isInterface() || Modifier.isAbstract(beanClass.getModifiers());
    }

    public List<Class<?>> getComponentsImplementing(Collection<Class<?>> components, Class<?> interfaceClass) {
        ArrayList<Class<?>> componentsImplementingInterface = new ArrayList<>();
        for (Class<?> component : components) {
            for (Class<?> implementedClass : component.getInterfaces()) {
                if (interfaceClass.equals(implementedClass)) {
                    componentsImplementingInterface.add(component);
                }
            };
        }
        return componentsImplementingInterface;
    }
}
