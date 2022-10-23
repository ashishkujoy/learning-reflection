package org.example.di;

import java.lang.annotation.Annotation;
import java.util.Collection;

import org.burningwave.core.assembler.ComponentContainer;
import org.burningwave.core.classes.ClassCriteria;
import org.burningwave.core.classes.ClassHunter;
import org.burningwave.core.classes.SearchConfig;

public class ClasspathScanner {
    
    public static Collection<Class<?>> getAllClassesAnnotatedWith(
            String packageToScan,
            Class<? extends Annotation> annotationClass) {
                
        ComponentContainer container = ComponentContainer.getInstance();
        ClassHunter classHunter = container.getClassHunter();
        ClassCriteria classesAnnotatedWithComponent = ClassCriteria.create()
                .allThoseThatMatch(clazz -> clazz.isAnnotationPresent(annotationClass));
        
        SearchConfig searchConfig = SearchConfig
                .forResources(packageToScan)
                .by(classesAnnotatedWithComponent);
        
        return classHunter.findBy(searchConfig).getClasses();
    }
}
