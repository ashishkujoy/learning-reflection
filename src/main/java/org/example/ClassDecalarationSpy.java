package org.example;

import static java.lang.System.out;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class ClassDecalarationSpy {
    public static void forClassName(String className) throws ClassNotFoundException {
        Class<?> c = Class.forName(className);
        out.format("Class:%n  %s%n%n", c.getCanonicalName());
        out.format("Modifiers:%n  %s%n%n", Modifier.toString(c.getModifiers()));

        out.format("Type Parameters:%n");
        TypeVariable<?>[] tv = c.getTypeParameters();
        if (tv.length != 0) {
            out.format("  ");
            for (TypeVariable<?> t : tv) {
                out.format("%s ", t.getName());
            }
            out.format("%n%n");
        } else {
            out.format("  -- No Type Parameters --%n%n");
        }

        out.format("Implemented Interfaces:%n");
        Type[] intfs = c.getGenericInterfaces();
	    if (intfs.length != 0) {
		for (Type intf : intfs)
		    out.format("  %s%n", intf.toString());
		out.format("%n");
	    } else {
		out.format("  -- No Implemented Interfaces --%n%n");
	    }


        out.format("Inheritance Path:%n");
	    List<Class<?>> l = new ArrayList<Class<?>>();
	    printAncestor(c, l);
	    if (l.size() != 0) {
		for (Class<?> cl : l)
		    out.format("  %s%n", cl.getCanonicalName());
		out.format("%n");
	    } else {
		out.format("  -- No Super Classes --%n%n");
	    }

        out.format("Annotations:%n");
	    Annotation[] ann = c.getAnnotations();
	    if (ann.length != 0) {
		for (Annotation a : ann)
		    out.format("  %s%n", a.toString());
		out.format("%n");
	    } else {
		out.format("  -- No Annotations --%n%n");
	    }
    }

    private static void printAncestor(Class<?> c, List<Class<?>> l) {
        Class<?> ancestor = c.getSuperclass();
         if (ancestor != null) {
            l.add(ancestor);
            printAncestor(ancestor, l);
         }
        }

    @SuppressWarnings("UNCHECKED_CAST")
    public static <T> Optional<T> printFields(Class<T> c) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Constructor<?>[] cons = c.getConstructors();
        Constructor<?> zeroArgCons = null;

        for(Constructor<?> con : cons) {
            if(con.getParameterCount() == 0) {
                zeroArgCons = con;
                break;
            }
        }
        if(zeroArgCons != null) {
            T t = (T)zeroArgCons.newInstance();
            return Optional.of(t);
        } else {
            return Optional.empty();
        }
    }    
}
