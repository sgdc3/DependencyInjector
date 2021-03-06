package ch.jalu.injector.extras.handlers;

import ch.jalu.injector.context.ResolutionContext;
import ch.jalu.injector.extras.AllTypes;
import ch.jalu.injector.handlers.dependency.TypeSafeAnnotationHandler;
import ch.jalu.injector.handlers.instantiation.Resolution;
import ch.jalu.injector.handlers.instantiation.SimpleResolution;
import ch.jalu.injector.utils.InjectorUtils;
import ch.jalu.injector.utils.ReflectionUtils;
import org.reflections.Reflections;

import java.util.Set;

/**
 * Annotation handler for {@link AllTypes}. Dependencies with this annotation will be
 * assigned a collection of all known subtypes in the project's package.
 * <p>
 * Requires that you add the <a href="https://github.com/ronmamo/reflections">reflections project</a>
 * as dependency to be able to use this.
 */
public class AllTypesAnnotationHandler extends TypeSafeAnnotationHandler<AllTypes> {

    private Reflections reflections;

    public AllTypesAnnotationHandler(String rootPackage) {
        reflections = new Reflections(rootPackage);
    }

    @Override
    protected Class<AllTypes> getAnnotationType() {
        return AllTypes.class;
    }

    @Override
    public Resolution<?> resolveValueSafely(ResolutionContext context, AllTypes annotation) {
        InjectorUtils.checkNotNull(annotation.value(), "Annotation value may not be null");
        Set<?> subTypes = reflections.getSubTypesOf(annotation.value());

        Class<?> rawType = context.getIdentifier().getTypeAsClass();
        return new SimpleResolution<>(ReflectionUtils.toSuitableCollectionType(rawType, subTypes));
    }
}
