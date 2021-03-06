package ch.jalu.injector.testing.runner;

import ch.jalu.injector.Injector;
import ch.jalu.injector.InjectorBuilder;
import ch.jalu.injector.handlers.Handler;
import ch.jalu.injector.handlers.postconstruct.PostConstructMethodInvoker;
import ch.jalu.injector.utils.ReflectionUtils;
import org.junit.runners.model.FrameworkField;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Statement for initializing {@link ch.jalu.injector.testing.InjectDelayed} fields. These fields are
 * constructed after {@link ch.jalu.injector.testing.BeforeInjecting} and before JUnit's &#064;Before.
 */
public class RunDelayedInjects extends Statement {

    private final Statement next;
    private TestClass testClass;
    private Object target;
    private List<FrameworkField> fields;

    public RunDelayedInjects(Statement next, TestClass testClass, Object target, List<FrameworkField> fields) {
        this.next = next;
        this.testClass = testClass;
        this.target = target;
        this.fields = fields;
    }

    @Override
    public void evaluate() throws Throwable {
        Injector injector = getInjector();
        for (FrameworkField frameworkField : fields) {
            Field field = frameworkField.getField();
            if (ReflectionUtils.getFieldValue(field, target) != null) {
                throw new IllegalStateException("Field with @InjectDelayed must be null on startup. "
                    + "Field '" + field.getName() + "' is not null");
            }
            Object object = injector.getSingleton(field.getType());
            ReflectionUtils.setField(field, target, object);
        }

        this.testClass = null;
        this.target = null;
        this.fields = null;
        next.evaluate();
    }

    /**
     * Override this method to provide your own injector in the test runner, e.g. if your application uses
     * custom instantiation methods or annotation behavior.
     *
     * @return the injector used to set {@link ch.jalu.injector.testing.InjectDelayed} fields
     */
    protected Injector getInjector() {
        List<Handler> instantiationProviders = InjectorBuilder.createInstantiationProviders("");
        return new InjectorBuilder()
            .addHandlers(
                new AnnotationResolver(testClass, target),
                new MockDependencyHandler(testClass, target),
                new PostConstructMethodInvoker())
            .addHandlers(instantiationProviders)
            .create();
    }
}
