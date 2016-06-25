package ch.jalu.injector.handlers.annotations;

import ch.jalu.injector.Injector;
import ch.jalu.injector.handlers.Handler;
import ch.jalu.injector.instantiation.DependencyDescription;

import javax.annotation.Nullable;

/**
 * Allows providing a field with a value based on its annotations.
 * <p>
 * Annotation handlers may use fields marked with {@link javax.inject.Inject} to obtain certain objects:
 * <ul>
 *   <li>String field will be assigned the root package</li>
 *   <li>Injector field will be assigned the injector that uses the annotation handler</li>
 * </ul>
 * Annotation handler fields of any other type annotated with {@link javax.inject.Inject}
 * will cause an exception to be thrown.
 */
public interface AnnotationHandler extends Handler {

    /**
     * Resolves the value of a dependency based on the present annotations and the declared type.
     * Returns {@code null} if the given annotations and field type do not apply
     * to the handler. May throw an exception if a given annotation is being used wrong.
     * <p>
     * Note that you are you not forced to check if the returned Object is valid for the given
     * dependency {@code type}, unless you want to show a specific error message.
     *
     * @param injector the injector
     * @param dependencyDescription description of the dependency
     * @return the resolved value, or null if not applicable
     * @throws Exception for invalid usage of annotation
     */
    @Nullable
    Object resolveValue(Injector injector, DependencyDescription dependencyDescription) throws Exception;

}
