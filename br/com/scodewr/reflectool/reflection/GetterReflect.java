package br.com.loriens.reflectionutilrepository.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import static br.com.loriens.reflectionutilrepository.common.CommonMethods.capitalize;

/**
 * Utility class for reflecting and invoking getter methods on nested fields.
 */
public class GetterReflect {

    // Private constructor to prevent instantiation
    private GetterReflect() {}

    /**
     * Splits a class hierarchy string into a list of class parent names.
     *
     * @param classHierarchy the class hierarchy string, separated by dots
     * @return a list of class parent names
     */
    public static List<String> getClassParents(String classHierarchy) {
        return Arrays.asList(classHierarchy.split("\\."));
    }

    /**
     * Retrieves the value of a nested field by traversing the specified class hierarchy.
     *
     * @param obj the root object
     * @param classParents the list of class parent names defining the hierarchy
     * @param fieldName the name of the field to retrieve
     * @return the value of the nested field, or null if not found
     */
    public static Object getNestedFieldValue(Object obj, List<String> classParents, String fieldName) {
        if (classParents.isEmpty()) {
            return null;
        }

        String currentClassParent = classParents.get(0);
        List<String> remainingClassParents = classParents.subList(1, classParents.size());

        for (Field field : obj.getClass().getDeclaredFields()) {
            try {
                Object fieldValue = invokeGetter(obj, field.getName());
                if (fieldValue != null && field.getType().getSimpleName().equals(currentClassParent)) {
                    if (remainingClassParents.isEmpty()) {
                        return invokeGetter(fieldValue, fieldName);
                    } else {
                        return getNestedFieldValue(fieldValue, remainingClassParents, fieldName);
                    }
                }
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Constructs the getter method name for a given field name.
     *
     * @param fieldName the field name
     * @return the getter method name
     */
    private static String getterMethodName(String fieldName) {
        return "get" + capitalize(fieldName);
    }

    /**
     * Invokes the getter method for a given field on the specified object.
     *
     * @param obj the object
     * @param fieldName the field name
     * @return the value returned by the getter method
     * @throws NoSuchMethodException if the getter method does not exist
     * @throws InvocationTargetException if the getter method throws an exception
     * @throws IllegalAccessException if the getter method is inaccessible
     */
    public static Object invokeGetter(Object obj, String fieldName) throws NoSuchMethodException,
            InvocationTargetException, IllegalAccessException {
        return obj.getClass().getDeclaredMethod(getterMethodName(fieldName)).invoke(obj);
    }
}