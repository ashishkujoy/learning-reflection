package org.example.di;

import java.util.List;

public class ErrorFactory {

    public static BeanCreationException noPrimaryBeanException(String beanClassName, List<String> componentsName) {
        StringBuilder sb = new StringBuilder();
        sb.append("Error creating bean for type: " + beanClassName + " Found multiple component for this type\n");
        sb.append("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
        sb.append("Components which implements the required bean type\n");
        componentsName.stream().forEach(name -> sb.append("\t" + name + "\n"));
        sb.append("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
        sb.append("Make sure to mark one of the component as primary.");

        return new BeanCreationException(sb.toString());
    }

    public static BeanCreationException multiplePrimaryComponents(String beanClassName, List<String> primaryComponentsName) {
        StringBuilder sb = new StringBuilder();
        sb.append("Error creating bean for type: " + beanClassName + " Found multiple component for this type annotated with @Primary\n");
        sb.append("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
        sb.append("Components annotated with @Primary\n");
        primaryComponentsName.stream().forEach(name -> sb.append("\t" + name + "\n"));
        sb.append("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
        sb.append("Make sure to mark only of the component as primary.");

        return new BeanCreationException(sb.toString());
    }

}
