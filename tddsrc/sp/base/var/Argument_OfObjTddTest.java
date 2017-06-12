package sp.base.var;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * {@link Argument.OfObj} のテスト駆動開発.
 *
 * @author Se-foo
 * @since 0.1
 */
public class Argument_OfObjTddTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void test() {

        // Variable#arg
        String target = "test";
        Argument.OfObj<String> instance = Variable.arg(target);
        assertThat(instance, notNullValue());
        System.out.println(instance);

        // #orElse
        assertThat(instance.orElse("other"), is(target));

        // #requireNonNull
        assertThat(instance.requireNonNull(), is(instance));
        assertThat(instance.requireNonNull("Null."), is(instance));

        // #requireInstanceOf
        assertThat(instance.requireInstanceOf(CharSequence.class), is(instance));
        assertThat(instance.requireInstanceOf(CharSequence.class, "Implements No CharSequence."), is(instance));

        // #nonNull
        assertThat(instance.nonNull(), is(instance));

        // #instanceOf
        assertThat(instance.instanceOf(CharSequence.class), is(instance));
    }

}
