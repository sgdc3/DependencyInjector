package ch.jalu.injector.utils;

import ch.jalu.injector.exceptions.InjectorException;

/**
 * Class with simple utility methods.
 */
public final class InjectorUtils {

    private InjectorUtils() {
    }

    public static void checkNotNull(Object o, Class<?> clazz) {
        checkNotNull(o, "Object may not be null", clazz);
    }

    public static void checkNotNull(Iterable<?> collection, Class<?> clazz) {
        checkNotNull((Object) collection, clazz);
        for (Object o : collection) {
            checkNotNull(o, null);
        }
    }

    public static <T> void checkNotNull(T[] arr, Class<?> clazz) {
        checkNotNull((Object) arr, clazz);
        for (Object o : arr) {
            checkNotNull(o, clazz);
        }
    }

    public static void checkNotNull(Object o, String errorMessage, Class<?> clazz) {
        if (o == null) {
            throw new InjectorException(errorMessage, clazz);
        }
    }

    public static void checkArgument(boolean expression, String errorMessage, Class<?> clazz) {
        if (!expression) {
            throw new InjectorException(errorMessage, clazz);
        }
    }

    public static void rethrowException(Exception e) throws InjectorException {
        throw (e instanceof InjectorException)
                ? (InjectorException) e
                : new InjectorException("An error occurred (see cause)", e, null);
    }
}
