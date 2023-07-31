package io.github.primelib.primecodegenlib.java.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Utility class for loading classes using JDK {@link ServiceLoader} facility.
 */
public class ServiceLoaderUtil {

    /**
     * Method for locating available classes, using JDK {@link ServiceLoader} facility, along with module-provided SPI.
     * <p>
     * This method does not do any caching, so calls should be considered potentially expensive.
     */
    public static <T> List<T> find(Class<T> clazz) {
        return find(clazz, null);
    }

    /**
     * Method for locating available classes, using JDK {@link ServiceLoader} facility, along with module-provided SPI.
     * <p>
     * This method does not do any caching, so calls should be considered potentially expensive.
     */
    public static <T> List<T> find(Class<T> clazz, ClassLoader classLoader) {
        ArrayList<T> modules = new ArrayList<>();
        ServiceLoader<T> loader = ServiceLoader.load(clazz, classLoader == null ? Thread.currentThread().getContextClassLoader() : classLoader);

        for (T extension : loader) {
            modules.add(extension);
        }

        return modules;
    }
}
