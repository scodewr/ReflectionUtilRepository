package br.com.loriens.reflectionutilrepository.reflection;


import static br.com.loriens.reflectionutilrepository.common.CommonMethods.capitalize;

/**
 * Utility class for setting attributes on objects using reflection.
 */
public class SetterReflect {

    /**
     * Sets the value of the specified attribute on the given object.
     *
     * @param obj the object on which to set the attribute
     * @param attributeName the name of the attribute to set
     * @param value the value to set on the attribute
     */
    public static void setAttribute(Object obj, String attributeName, Object value) {
        try {
            obj.getClass().getDeclaredMethod("set" + capitalize(attributeName)).invoke(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}