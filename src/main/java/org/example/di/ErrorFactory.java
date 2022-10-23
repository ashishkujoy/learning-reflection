package org.example.di;

import java.util.List;

public class ErrorFactory {

    public static RuntimeException noImplementationOfInterface(String beanClassName) {
        String message = String.format("Failed to create bean for %s, no implementation of this interface found annotated with @Component ", beanClassName);
        return new RuntimeException(message);
    }

    public static RuntimeException noPrimaryBeansFound(String beanClassName, List<String> componentNames) {
        StringBuilder sb = new StringBuilder();
        sb.append("Failed to create bean for %s, found %d canditate for Autowiring:\n");
        componentNames.stream().forEach(name -> sb.append("\t" + name + "\n"));
        sb.append("None of the candidate marked with @Primary, mark one of them as primary\n");
        return new RuntimeException(sb.toString());
    }

    public static RuntimeException moreThanOnePrimaryBeansFound(String beanClassName, int numberOfPrimaryBeans) {
        String message = String.format("Failed to create bean for %s, found %d implementation annotated with @Primary required only 1", beanClassName, numberOfPrimaryBeans);
        return new RuntimeException(message);
    }
    
}
