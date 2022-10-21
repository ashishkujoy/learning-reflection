package org.example.di;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Optional;

import org.example.annotations.Autowired;
import org.example.annotations.PostContruct;

public class ReflectionUtil {

    public static Optional<Constructor<?>> getZeroArgsConstructor(Class<?> clazz) {
        Constructor<?>[] constructors = clazz.getConstructors();
        for(Constructor<?> constructor: constructors) {
            if(constructor.getParameterCount() == 0) {
                return Optional.of(constructor);
            }
        }
        return Optional.empty();
    }

    public static Optional<Constructor<?>> getNonZeroArgsConstructor(Class<?> clazz) {
        Constructor<?>[] constructors = clazz.getConstructors();
        
        if(constructors.length == 0) {
            return Optional.empty();
        } else {
            return Optional.of(constructors[0]);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> Optional<T> constructNewInstance(Class<T> clazz) {
        Constructor<?>[] constructors = clazz.getConstructors();
        if(constructors.length == 1) {
            return constructInstanceOf((Constructor<T>) constructors[0]);
        } else {
            ArrayList<Constructor<?>> construtorWithAutowired = new ArrayList<>();
            for(Constructor<?> constructor: constructors) {
                if(constructor.getAnnotation(Autowired.class) != null) {
                    construtorWithAutowired.add(constructor);
                }
            }
            if(construtorWithAutowired.size() == 1) {
                return constructInstanceOf((Constructor<T>) construtorWithAutowired.get(0));
            }
            if(construtorWithAutowired.size() == 0) {
                throw new RuntimeException(String.format("No constructor for bean %s has been annotated with @Autowired annotation, mark one of the constructor as @Autowired", clazz.getName()));
            }
            if(construtorWithAutowired.size() > 1) {
                throw new RuntimeException(String.format("%d constructor for bean %s has been annotated with @Autowired annotation, mark only one of the constructor as @Autowired", construtorWithAutowired.size() ,clazz.getName()));
            }
        }
        return Optional.empty();    
    }

    private static <T> Optional<T> constructInstanceOf(Constructor<T> constructor) {
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        Object[] constructorArgs = new Object[parameterTypes.length];
        
        for(int i =0; i < parameterTypes.length; i++) {
            Object arg = constructNewInstance(parameterTypes[i]).get();
            constructorArgs[i] = arg;  
        }

        try {
            T newInstance = constructor.newInstance(constructorArgs);
            runPostConstructMethod(newInstance);
            return Optional.of(newInstance);
        } catch(Throwable t) {
            return Optional.empty();
        }
    }

    private static void runPostConstructMethod(Object newlyInstantiatedBean) {
        Class<? extends Object> clazz = newlyInstantiatedBean.getClass();
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for(Method method : declaredMethods) {
            if(method.getAnnotation(PostContruct.class) != null) {
                try {
                    method.setAccessible(true);
                    method.invoke(newlyInstantiatedBean);    
                } catch(Throwable t) {
                    throw new RuntimeException(
                        String.format(
                            "Failed to invoke post construct method on bean: %s, method: %s",
                                    clazz.getName(),
                                    method.getName()
                            ),
                        t    
                    );
                }
            }
        }
    }
    
}
