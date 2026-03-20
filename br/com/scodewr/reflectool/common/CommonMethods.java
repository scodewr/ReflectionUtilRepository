package br.com.loriens.reflectionutilrepository.common;

public class CommonMethods {
    /**
     * Capitalizes the first letter of the given string.
     *
     * @param str the string to capitalize
     * @return the capitalized string
     */
    public static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
