package ch.jalu.injector;

import ch.jalu.injector.exceptions.InjectorException;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.rules.ExpectedException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.function.Predicate;

import static org.hamcrest.Matchers.containsString;

/**
 * Utility class for testing.
 */
public final class TestUtils {

    private TestUtils() {
    }

    /**
     * Returns a custom matcher that checks an annotation's {@link Annotation#annotationType() type}
     * against the given argument.
     *
     * @param type the type to verify against
     * @return generated matcher
     */
    public static Matcher<? super Annotation> annotationOf(final Class<?> type) {
        return new TypeSafeMatcher<Annotation>() {
            @Override
            protected boolean matchesSafely(Annotation item) {
                return item.annotationType().equals(type);
            }

            @Override
            public void describeTo(Description description) {
                description.appendValue("Annotation of type @" + type.getSimpleName());
            }
        };
    }

    public static void assertIsProperUtilsClass(Class<?> clazz) {
        if (!Modifier.isFinal(clazz.getModifiers())) {
            throw new IllegalStateException("Class '" + clazz.getSimpleName() + "' should be declared final "
                + "if it is a utility class");
        }
        validateHasOnlyPrivateEmptyConstructor(clazz);
    }

    /**
     * Check that a class only has a hidden, zero-argument constructor, preventing the
     * instantiation of such classes (utility classes).
     *
     * @param clazz The class to validate
     */
    private static void validateHasOnlyPrivateEmptyConstructor(Class<?> clazz) {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        if (constructors.length > 1) {
            throw new IllegalStateException("Class " + clazz.getSimpleName() + " has more than one constructor");
        } else if (constructors[0].getParameterTypes().length != 0) {
            throw new IllegalStateException("Constructor of " + clazz + " does not have empty parameter list");
        } else if (!Modifier.isPrivate(constructors[0].getModifiers())) {
            throw new IllegalStateException("Constructor of " + clazz + " is not private");
        }

        // Ugly hack to get coverage on the private constructors
        // http://stackoverflow.com/questions/14077842/how-to-test-a-private-constructor-in-java-application
        try {
            constructors[0].setAccessible(true);
            constructors[0].newInstance();
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    public static <T> T findOrThrow(Collection<T> coll, Predicate<? super T> predicate) {
        return coll.stream().filter(predicate).findFirst()
            .orElseThrow(() -> new IllegalStateException("Could not find any matching item"));
    }

    public static ParameterizedType createParameterizedType(Type rawType, Type... actualTypeArguments) {
        return new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return actualTypeArguments;
            }

            @Override
            public Type getRawType() {
                return rawType;
            }

            @Override
            public Type getOwnerType() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public static final class ExceptionCatcher {
        private final ExpectedException expectedException;

        public ExceptionCatcher(ExpectedException expectedException) {
            this.expectedException = expectedException;
        }

        public void expect(String message) {
            expectedException.expect(InjectorException.class);
            expectedException.expectMessage(containsString(message));
        }
    }

}
