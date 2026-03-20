package br.com.scodewr.reflectool.reflection;

import br.com.scodewr.reflectool.exception.PackageNamePatternException;

import static br.com.scodewr.reflectool.common.CommonMethods.capitalize;

/**
 * Reflection utility for resolving a {@link Class} from a simple class name
 * and a target package.
 * <p>
 * This class:
 * <ul>
 *   <li>normalizes the package name (supports slashes and removes trailing dot),</li>
 *   <li>validates the package format against a regex,</li>
 *   <li>builds the fully qualified class name using a capitalized class name,</li>
 *   <li>loads the class through the current thread context {@link ClassLoader}.</li>
 * </ul>
 * <p>
 * If class loading fails, the exception is printed and {@code null} is returned.
 */
public class ClassReflect {

    private static final String CLASS_REFLECT = "br.com.scodewr.reflectool.reflection.ClassReflect";
    private static final String PACKAGE_PATTERN = "^[a-zA-Z_]\\w*+(?:\\.\\w+)*+$";

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private ClassReflect(){}

    /**
     * Resolves a class by its simple name and package.
     * <p>
     * The method validates and normalizes the package name, capitalizes
     * {@code className}, and tries to load the class using the current thread
     * context class loader. If the context loader is {@code null}, it falls back
     * to this utility class loader.
     *
     * @param className simple class name (for example, {@code "user"} for {@code "User"})
     * @param classPackage package where the class is located
     *                     (for example, {@code "br.com.example.model"})
     * @return the resolved {@link Class}; {@code null} if not found or if a package
     *         validation error occurs
     */
    public static Class<?> getClassByNameAndPackage(String className, String classPackage){
        // Tries to use the caller context class loader (safer for frameworks)
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Class<?> aclass = null;

        classPackage = validatePackageNamePattern(classPackage);

        var classFullName = classPackage.concat(".").concat(capitalize(className));

        try{
            // If context loader is null (rare), fallback to this library class loader
            if (classLoader == null) {
                var aClass = Class.forName(CLASS_REFLECT);
                classLoader = aClass.getClassLoader();
            }
            aclass= classLoader.loadClass(classFullName);
        } catch (ClassNotFoundException | PackageNamePatternException e){
            e.printStackTrace();
        }

        return aclass;
    }

    /**
     * Validates the provided package name.
     * <p>
     * Rules:
     * <ul>
     *   <li>must not be {@code null} or empty,</li>
     *   <li>is normalized before validation,</li>
     *   <li>must match {@code PACKAGE_PATTERN}.</li>
     * </ul>
     *
     * @param packageName package name to validate
     * @return normalized and valid package name
     * @throws PackageNamePatternException if package name is null, empty, or invalid
     */
    private static String validatePackageNamePattern(String packageName){

        if(null == packageName || packageName.isEmpty()){
            throw new PackageNamePatternException("PackageName empty or null");
        }

        packageName = normalizePackageName(packageName);

        if(!packageName.matches(PACKAGE_PATTERN)){
            throw new PackageNamePatternException("PackageName don't follow de pattern: " + PACKAGE_PATTERN);
        }

        return packageName;
    }

    /**
     * Normalizes a package name to Java dot notation.
     * <p>
     * Transformations:
     * <ul>
     *   <li>replaces {@code "/"} and {@code "\\"} with {@code "."},</li>
     *   <li>trims leading/trailing spaces,</li>
     *   <li>removes a trailing dot, if present.</li>
     * </ul>
     *
     * @param packageName raw package name
     * @return normalized package name
     */
    private static String normalizePackageName(String packageName) {
        return packageName
                .replace("/", ".")
                .replace("\\", ".")
                .trim()
                .replaceAll("\\.$", "");
    }
}
