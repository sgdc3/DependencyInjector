package ch.jalu.injector.testing.runner;

import ch.jalu.injector.utils.ReflectionUtils;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import java.util.List;

/**
 * Statement for running {@link ch.jalu.injector.testing.BeforeInjecting} methods. Such methods are run
 * after Mockito's &#064;Mock, &#064;Spy and &#064;InjectMocks have taken effect,
 * but before {@link ch.jalu.injector.testing.InjectDelayed} fields are handled.
 */
public class RunBeforeInjectings extends Statement {

    private final Statement next;
    private final List<FrameworkMethod> beforeInjectings;
    private final Object target;

    public RunBeforeInjectings(Statement next, List<FrameworkMethod> beforeInjectings, Object target) {
        this.next = next;
        this.beforeInjectings = beforeInjectings;
        this.target = target;
    }

    @Override
    public void evaluate() throws Throwable {
        for (FrameworkMethod method : beforeInjectings) {
            ReflectionUtils.invokeMethod(method.getMethod(), target);
        }
        next.evaluate();
    }
}
