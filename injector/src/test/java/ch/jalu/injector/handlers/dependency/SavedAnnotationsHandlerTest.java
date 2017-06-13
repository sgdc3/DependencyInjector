package ch.jalu.injector.handlers.dependency;

import ch.jalu.injector.exceptions.InjectorException;
import ch.jalu.injector.handlers.instantiation.DependencyDescription;
import ch.jalu.injector.samples.Duration;
import ch.jalu.injector.samples.Size;
import org.junit.Test;

import java.lang.annotation.Annotation;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * Test for {@link SavedAnnotationsHandler}.
 */
public class SavedAnnotationsHandlerTest {

    private SavedAnnotationsHandler savedAnnotationsHandler = new SavedAnnotationsHandler();

    @Test
    public void shouldReturnRegisteredValue() {
        // given
        Object object = "value for @Duration";
        savedAnnotationsHandler.onAnnotation(Duration.class, object);
        Annotation[] annotations = {
                newSizeAnnotation("value"), newDurationAnnotation()
        };
        DependencyDescription dependencyDescription = new DependencyDescription( null, annotations);

        // when
        // Injector param not needed -> null
        Object result = savedAnnotationsHandler.resolveValue(null, dependencyDescription);

        // then
        assertThat(result, equalTo(object));
    }

    @Test
    public void shouldReturnNullForUnregisteredAnnotation() {
        // given
        Annotation[] annotations = {
            newSizeAnnotation("value"), newDurationAnnotation()
        };
        DependencyDescription dependencyDescription = new DependencyDescription(null, annotations);
        // register some object under another annotation for the heck of it
        savedAnnotationsHandler.onAnnotation(Test.class, new Object());

        // when
        Object result = savedAnnotationsHandler.resolveValue(null, dependencyDescription);

        // then
        assertThat(result, nullValue());
    }

    @Test(expected = InjectorException.class)
    public void shouldThrowForSecondAnnotationRegistration() {
        // given
        savedAnnotationsHandler.onAnnotation(Size.class, 12);

        // when
        savedAnnotationsHandler.onAnnotation(Size.class, -8);

        // then - exception
    }

    @Test(expected = InjectorException.class)
    public void shouldThrowForNullValueAssociatedToAnnotation() {
        // given / when
        savedAnnotationsHandler.onAnnotation(Duration.class, null);

        // then - exception
    }

    private static Size newSizeAnnotation(final String value) {
        return new Size() {
            @Override
            public Class<Size> annotationType() {
                return Size.class;
            }

            @Override
            public String value() {
                return value;
            }
        };
    }

    private static Duration newDurationAnnotation() {
        return new Duration() {
            @Override
            public Class<Duration> annotationType() {
                return Duration.class;
            }
        };
    }

}