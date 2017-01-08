package ch.jalu.injector.handlers.instantiation;

import ch.jalu.injector.TestUtils.ExceptionCatcher;
import ch.jalu.injector.samples.BetaManager;
import ch.jalu.injector.samples.InjectOnDifferentMembersClass;
import ch.jalu.injector.samples.StaticFieldInjection;
import ch.jalu.injector.samples.inheritance.Child;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * Test for {@link DefaultInjectionProvider}.
 */
public class DefaultInjectionProviderTest {

    private InstantiationProvider provider = new DefaultInjectionProvider();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private ExceptionCatcher exceptionCatcher = new ExceptionCatcher(expectedException);

    @Test
    public void shouldProvideInstantiation() {
        // given / when
        Instantiation<BetaManager> injection = provider.get(BetaManager.class);

        // then
        assertThat(injection, not(nullValue()));
        assertThat(injection.getDependencies(), hasSize(3));
    }

    @Test
    public void shouldProvideInstantiationForClassWithInheritance() {
        // given / when
        Instantiation<Child> injection = provider.get(Child.class);

        // then
        assertThat(injection, not(nullValue()));
        assertThat(injection.getDependencies(), hasSize(5));
    }

    @Test
    public void shouldThrowForStaticField() {
        // expect
        exceptionCatcher.expect("@Inject may not be placed on static fields");

        // when
        provider.get(StaticFieldInjection.class);
    }

    @Test
    public void shouldThrowForMixedInjection() {
        // expect
        exceptionCatcher.expect("may not have @Inject constructor and @Inject fields");

        // when
        provider.get(InjectOnDifferentMembersClass.class);
    }

}