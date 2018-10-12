package com.util;

import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.*;

public class ReflectionUtil {

    public static List<Class<?>> scan(Class<?> clazz, Class<? extends Annotation> annotation, String... paths) {
        List<String> list = new ArrayList<>(Arrays.asList(paths));
        return scan(clazz, annotation, list);
    }

    public static List<Class<?>> scan(Class<?> clazz, Class<? extends Annotation> annotation, List<String> paths) {
        if (paths.size() < 1) {
            return Collections.emptyList();
        }

        Reflections reflections = new Reflections(paths);
        Set<Class<?>> classesSet = reflections.getTypesAnnotatedWith(annotation);
        return new ArrayList<>(classesSet);
//                classesSet.stream().map(x ->
//        {
//
//            try {
//                return x.newInstance();
//            } catch (InstantiationException | IllegalAccessException e) {
//                e.printStackTrace();
//            }
//
//            return null;
//
//        }).collect(Collectors.toList());

    }


    public static void main(String[] args) {

    }

}
