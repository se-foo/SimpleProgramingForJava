package sp.util.function;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * {@link FunctionWithThrown.OfObj} のテスト駆動開発.
 *
 * @author Se-foo
 * @since 0.1
 */
public class FunctionWithThrown_OfObjTddTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void test() {

        // Check exception from #apply(new Object())
        this.thrown.expect(ClassCastException.class);
        this.thrown.expectMessage("String");

        // #apply
        FunctionWithThrown.OfObj<Object, String, ClassCastException> instance = target -> (String) target;
        assertThat(instance.apply("test"), is("test"));

        // #compose
        assertThat(instance.compose((StringBuilder target) -> new String(target))
                .apply(new StringBuilder().append("test")), is("test"));

        // #composeFunction
        assertThat(instance.composeFunction((StringBuilder target) -> new String(target))
                .apply(new StringBuilder().append("test")), is("test"));

        // andThen
        char[] charArray = instance.andThen(target -> target.toCharArray()).apply("test");
        assertThat(Arrays.equals(charArray, new char[] { 't', 'e', 's', 't' }), is(true));

        // andThenFunction
        charArray = instance.andThenFunction(target -> target.toCharArray()).apply("test");
        assertThat(Arrays.equals(charArray, new char[] { 't', 'e', 's', 't' }), is(true));

        // #toFunction
        assertThat(instance.toFunction().apply("test"), is("test"));

        // #apply
        instance.toFunction().apply(new Object());
    }

}
