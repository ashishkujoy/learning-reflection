package org.example.di;

import java.util.Collection;

import org.burningwave.core.assembler.ComponentContainer;
import org.burningwave.core.classes.ClassCriteria;
import org.burningwave.core.classes.ClassHunter;
import org.burningwave.core.classes.ClassHunter.SearchResult;
import org.burningwave.core.classes.SearchConfig;

public class ClasspathScanner {

    public static Collection<Class<?>> getClassesAnnotatedWith(String appPackage, Class<Component> class1) {
        ComponentContainer componentContainer = ComponentContainer.getInstance();
        ClassHunter classHunter = componentContainer.getClassHunter();
        ClassCriteria classCriteria = ClassCriteria.create()
                .allThoseThatMatch(clazz -> clazz.isAnnotationPresent(Component.class));
        SearchConfig searchConfig = SearchConfig.forResources(appPackage)
                .by(classCriteria);
        SearchResult searchResult = classHunter.findBy(searchConfig);
        
        return searchResult.getClasses();
    }

}
